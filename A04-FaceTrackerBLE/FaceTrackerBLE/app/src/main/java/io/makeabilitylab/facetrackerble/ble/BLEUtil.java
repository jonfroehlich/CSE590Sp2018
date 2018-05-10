package io.makeabilitylab.facetrackerble.ble;

import android.Manifest.permission;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/** Provides helper methods for managing Bluetooth related permissions and state. */
public class BLEUtil {

  public static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
  public static final int REQUEST_ENABLE_BLUETOOTH = 0;

  /**
   * Returns a value indicating whether connection to a device is supported on this Android device.
   */
  public static boolean isSupported(Context context) {
    return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        && getBluetoothAdapter(context) != null;
  }

  /**
   * Returns a value indicating whether the user has granted the required permissions to connect to
   * the device.
   *
   * <p>Because connecting to the device requiring scanning for BLE devices, the user must grant the
   * ACCESS_COARSE_LOCATION permission.
   */
  public static boolean hasPermission(Context context) {
    return ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Requests that the user grant the required permissions to connect to the device.
   *
   * <p>Because connecting to the device requiring scanning for BLE devices, the user must grant the
   * ACCESS_COARSE_LOCATION permission.
   *
   * <p>The result of the request is delivered to the {@param activity} in its {@see
   * Activitiy#onRequestPermissionsResult} method with the {@see REQUEST_BLUETOOTH_PERMISSIONS}
   * request code.
   */
  public static void requestPermission(Activity activity) {
    ActivityCompat.requestPermissions(
        activity, new String[] {permission.ACCESS_COARSE_LOCATION}, REQUEST_BLUETOOTH_PERMISSIONS);
  }

  /** Returns a value indicating whether the Bluetooth adapter is enabled. */
  public static boolean isBluetoothEnabled(Context context) {
    BluetoothAdapter bluetoothAdapter = getBluetoothAdapter(context);
    return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
  }

  /**
   * Starts an activity that allows the user to enable the Bluetooth adapter.
   *
   * <p>The result of the request is delivered to the {@param activity} in its {@see
   * Activitiy#onActivityResult} method with the {@see REQUEST_ENABLE_BLUETOOTH} request code.
   */
  public static void requestEnableBluetooth(Activity activity) {
    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    activity.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
  }

  /** Returns the {@see BluetoothAdapter} for the device, or null if none is found. */
  private static @Nullable
  BluetoothAdapter getBluetoothAdapter(Context context) {
    BluetoothManager bluetoothManager =
        (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    return bluetoothManager != null ? bluetoothManager.getAdapter() : null;
  }
}
