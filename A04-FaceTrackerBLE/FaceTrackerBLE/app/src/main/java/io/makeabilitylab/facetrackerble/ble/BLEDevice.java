package io.makeabilitylab.facetrackerble.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Bi-directionally communicates with a device through BLE.
 *
 * <pre>{@code
 * public class MainActivity extends AppCompatActivity {
 *   private BLEDevice bleDevice;
 *
 *   @Override
 *   protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 *     setContentView(R.layout.activity_main);
 *
 *     bleDevice = new BLEDevice(this, "myname");
 *     bleDevice.addListener(
 *         new BLEListener() {
 *           @Override
 *           public void onConnected() {
 *             byte[] data = ...
 *             bleDevice.sendData(data);
 *           }
 *
 *           @Override
 *           public void onConnectFailed() { ... }
 *
 *           @Override
 *           public void onDisconnected() { ... }
 *
 *           @Override
 *           public void onDataReceived(byte[] data) { ... }
 *
 *           @Override
 *           public void onRssiChanged(int rssi) { ... }
 *         });
 *   }
 *
 *   @Override
 *   protected void onStart() {
 *     super.onStart();
 *     bleDevice.connect();
 *   }
 *
 *   @Override
 *   protected void onStop() {
 *     super.onStop();
 *     bleDevice.disconnect();
 *   }
 * }
 * }</pre>
 *
 * <p>This code was written by CSE590 student Chris Dziemborowicz based on the Bluetooth LE
 * code by CSE590 instructors Liang He and Jon Froehlich and the RedBearLab team.
 *
 * Said code was awesome and thus pulled into the main CSE590 code repo via a pull request
 * so all code benefit: :)
 *   - https://github.com/jonfroehlich/CSE590Sp2018/pull/7
 *
 * See:
 *  - https://github.com/RedBearLab/Android.
 */
public class BLEDevice {

  /**
   * A value indicating whether this instance is connecting to, connected to, or disconnected from
   * the device.
   */
  public enum State {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
  }

  private static final String TAG = BLEDevice.class.getSimpleName();

  private static UUID CLIENT_CHARACTERISTIC_CONFIG_UUID =
      UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

  private static UUID SERVICE_UUID = UUID.fromString("713d0000-503e-4c75-ba94-3148f18d941e");
  private static UUID RX_UUID = UUID.fromString("713d0002-503e-4c75-ba94-3148f18d941e");
  private static UUID TX_UUID = UUID.fromString("713d0003-503e-4c75-ba94-3148f18d941e");

  private static final int BLE_SCAN_TIME_MS = 2000;
  private static final int READ_RSSI_INTERVAL_MS = 500;

  private final Context context;
  private final Handler handler = new Handler();
  private final BluetoothLeScanner bluetoothLeScanner;

  private final String name;

  private State state = State.DISCONNECTED;
  private BluetoothDevice device;
  private BluetoothGatt bluetoothGatt;
  private BluetoothGattCharacteristic txCharacteristic;

  private final List<BLEListener> listeners = new ArrayList<>();

  private final ScanCallback scanCallback =
      new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
          if (matches(result, name, SERVICE_UUID)) {
            handler.post(() -> onDeviceFound(result));
          }
        }
      };

  private final BluetoothGattCallback gattCallback =
      new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
          switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
              handler.post(BLEDevice.this::onGattConnected);
              break;

            case BluetoothProfile.STATE_DISCONNECTED:
              handler.post(BLEDevice.this::onGattDisconnected);
              break;
          }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
          if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.w(TAG, "BLEDevice onServicesDiscovered reported " + status);
            return;
          }

          handler.post(BLEDevice.this::onGattServicesDiscovered);
        }

        @Override
        public void onCharacteristicRead(
                BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
          if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.w(TAG, "BLEDevice onCharacteristicRead reported " + status);
            return;
          }

          if (characteristic.getUuid().equals(RX_UUID)) {
            handler.post(() -> onDataReceived(characteristic.getValue()));
          }
        }

        @Override
        public void onCharacteristicChanged(
                BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
          if (characteristic.getUuid().equals(RX_UUID)) {
            handler.post(() -> onDataReceived(characteristic.getValue()));
          }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
          if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.w(TAG, "BLEDevice onReadRemoteRssi reported " + status);
            return;
          }

          handler.post(() -> onRssiChanged(rssi));
        }
      };

  /**
   * Creates a new instance of the {@see BLEDevice} class.
   *
   * @param context a {@see Context}
   * @param name the name of the device to connect to (must be 8 characters or less)
   */
  public BLEDevice(Context context, String name) {
    if (context == null || name == null || name.length() > 8) {
      throw new IllegalArgumentException();
    }

    this.context = context.getApplicationContext();
    this.name = name;

    // Set up Bluetooth. It is the caller's responsibility to make sure it is supported.
    BluetoothManager bluetoothManager =
        (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    if (bluetoothManager == null) {
      throw new UnsupportedOperationException();
    }

    BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
    if (bluetoothAdapter == null) {
      throw new UnsupportedOperationException();
    }

    bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
  }

  /** Returns the name of the device that this instance communicates with. */
  public String getName() {
    return name;
  }

  /**
   * Returns the address of the device that is currently connected, or null if no board is currently
   * connected.
   */
  public @Nullable
  String getAddress() {
    if (state == State.CONNECTED) {
      return device.getAddress();
    } else {
      return null;
    }
  }

  /** Returns the service UUID of the device. This value is a constant. */
  public UUID getUUID() {
    return SERVICE_UUID;
  }

  /**
   * Returns a {@see State} value indicating whether this instance is connecting to, connected to,
   * or disconnected from the device.
   */
  public State getState() {
    return state;
  }

  /** Adds a listener. */
  public void addListener(BLEListener listener) {
    listeners.add(listener);
  }

  /** Removes a listener. */
  public void removeListener(BLEListener listener) {
    listeners.remove(listener);
  }

  private void notifyConnected() {
    for (BLEListener listener : listeners) {
      listener.onBleConnected();
    }
  }

  private void notifyConnectFailed() {
    for (BLEListener listener : listeners) {
      listener.onBleConnectFailed();
    }
  }

  private void notifyDisconnected() {
    for (BLEListener listener : listeners) {
      listener.onBleDisconnected();
    }
  }

  private void notifyDataReceived(byte[] data) {
    for (BLEListener listener : listeners) {
      listener.onBleDataReceived(data);
    }
  }

  private void notifyRssiChanged(int rssi) {
    for (BLEListener listener : listeners) {
      listener.onBleRssiChanged(rssi);
    }
  }

  /**
   * Attempts to connect to the device.
   *
   * <p>{@see Listener#onConnected} is called when the connection attempt succeeds. If the
   * connection attempt does not succeed, {@see Listener#onConnectFailed} is called instead.
   *
   * <p>Calling this method when this instance is already connecting or connected does nothing.
   */
  public void connect() {
    if (state != State.DISCONNECTED) {
      Log.w(TAG, "BLEDevice already connecting or connected.");
      return;
    }

    state = State.CONNECTING;
    Log.i(TAG, "BLEDevice connecting...");
    startScan();
  }

  private void startScan() {
    bluetoothLeScanner.startScan(scanCallback);
    handler.postDelayed(this::stopScan, BLE_SCAN_TIME_MS);
  }

  private void stopScan() {
    handler.removeCallbacks(this::stopScan);
    bluetoothLeScanner.stopScan(scanCallback);

    // If we did not find a device while connecting, stop trying to connect.
    if (state == State.CONNECTING && device == null) {
      Log.e(TAG, "Failed to find BLEDevice device.");
      disconnect();
    }
  }

  private void onDeviceFound(ScanResult result) {
    // Only process the result if we are still connecting, and only for the first matching device.
    if (state != State.CONNECTING || device != null) {
      return;
    }

    // Connect Bluetooth GATT.
    device = result.getDevice();
    bluetoothGatt = device.connectGatt(context, false /* autoConnect */, gattCallback);
    Log.i(TAG, "BLEDevice found (" + device.getAddress() + ").");
  }

  private void onGattConnected() {
    if (state != State.CONNECTING || bluetoothGatt == null) {
      return;
    }

    boolean success = bluetoothGatt.discoverServices();
    if (!success) {
      Log.e(TAG, "Failed to discover GATT services for BLEDevice device.");
      disconnect();
    }
  }

  private void onGattServicesDiscovered() {
    if (state != State.CONNECTING || bluetoothGatt == null) {
      return;
    }

    // Get the GATT service.
    BluetoothGattService service = bluetoothGatt.getService(SERVICE_UUID);
    if (service == null) {
      Log.e(TAG, "Failed to get GATT service for BLEDevice.");
      disconnect();
      return;
    }

    // Configure the RX characteristic.
    BluetoothGattCharacteristic rxCharacteristic = service.getCharacteristic(RX_UUID);
    if (rxCharacteristic == null) {
      Log.e(TAG, "Failed to get RX characteristic for BLEDevice.");
      disconnect();
      return;
    }
    bluetoothGatt.setCharacteristicNotification(rxCharacteristic, true);
    BluetoothGattDescriptor descriptor =
        rxCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    bluetoothGatt.writeDescriptor(descriptor);
    bluetoothGatt.readCharacteristic(rxCharacteristic);

    // Configure the TX characteristic.
    txCharacteristic = service.getCharacteristic(TX_UUID);
    if (txCharacteristic == null) {
      Log.e(TAG, "Failed to get TX characteristic for BLEDevice.");
      disconnect();
      return;
    }

    // Start reading RSSI at regular intervals.
    readRssi();

    state = State.CONNECTED;
    Log.i(TAG, "BLEDevice GATT connected (" + device.getAddress() + ").");
    notifyConnected();
  }

  private void onGattDisconnected() {
    disconnect();
  }

  /**
   * Sends data to the connected device.
   *
   * <p>If no device is currently connected, this method does nothing.
   */
  public void sendData(byte[] data) {
    if (state != State.CONNECTED) {
      Log.w(TAG, "Attempted to send data when BLEDevice is not connected.");
      return;
    }

    txCharacteristic.setValue(data);
    bluetoothGatt.writeCharacteristic(txCharacteristic);
  }

  private void onDataReceived(byte[] data) {
    if (state == State.CONNECTED) {
      notifyDataReceived(data);
    }
  }

  private void onRssiChanged(int rssi) {
    if (state == State.CONNECTED) {
      notifyRssiChanged(rssi);
    }
  }

  private void readRssi() {
    bluetoothGatt.readRemoteRssi();
    handler.postDelayed(this::readRssi, READ_RSSI_INTERVAL_MS);
  }

  /**
   * Disconnects from the currently connected device.
   *
   * <p>If no device is currently connected, this method does nothing.
   */
  public void disconnect() {
    // Clear our fields. This does nothing if we are already disconnected.
    device = null;

    if (bluetoothGatt != null) {
      bluetoothGatt.close();
      bluetoothGatt = null;
    }

    txCharacteristic = null;

    // Remove everything from the handler. This does nothing if we are already disconnected.
    handler.removeCallbacksAndMessages(null);

    // Update state and notify listeners.
    switch (state) {
      case CONNECTING:
        state = State.DISCONNECTED;
        Log.i(TAG, "BLEDevice failed to connect.");
        notifyConnectFailed();
        break;

      case CONNECTED:
        state = State.DISCONNECTED;
        Log.i(TAG, "BLEDevice disconnected.");
        notifyDisconnected();
        break;
    }
  }

  private static boolean matches(ScanResult result, String name, UUID serviceUUID) {
    ScanRecord scanRecord = result.getScanRecord();
    if (scanRecord == null) {
      return false;
    }

    if (!name.equals(result.getScanRecord().getDeviceName())) {
      return false;
    }

    for (ParcelUuid parcelUuid : scanRecord.getServiceUuids()) {
      UUID uuid = parcelUuid.getUuid();
      if (uuid.equals(serviceUUID)) {
        return true;
      }
    }

    return false;
  }
}
