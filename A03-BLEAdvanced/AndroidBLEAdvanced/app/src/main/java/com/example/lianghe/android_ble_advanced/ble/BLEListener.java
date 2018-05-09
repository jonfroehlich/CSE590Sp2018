package com.example.lianghe.android_ble_advanced.ble;

/** Listens to events related to the device. */
public interface BLEListener {
  /** Invoked when the device is connected. */
  void onConnected();

  /** Invoked when a connection attempt to the device is not successful. */
  void onConnectFailed();

  /** Invoked when the device is disconnected. */
  void onDisconnected();

  /** Invoked when data is received from the device. */
  void onDataReceived(byte[] data);

  /** Invoked when the RSSI for the connected device changes. */
  void onRssiChanged(int rssi);
}
