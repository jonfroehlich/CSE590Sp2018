package io.makeabilitylab.facetrackerble.ble;

/** Listens to events related to the device. */
public interface BLEListener {

  /** Invoked when the device is connected. */
  void onBleConnected();

  /** Invoked when a connection attempt to the device is not successful. */
  void onBleConnectFailed();

  /** Invoked when the device is disconnected. */
  void onBleDisconnected();

  /** Invoked when data is received from the device. */
  void onBleDataReceived(byte[] data);

  /** Invoked when the RSSI for the connected device changes. */
  void onBleRssiChanged(int rssi);
}
