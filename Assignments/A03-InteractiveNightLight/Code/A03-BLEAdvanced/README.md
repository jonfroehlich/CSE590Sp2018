AdvancedBLE is a more sophisticated version of [BasicBLE](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/A03-BLEBasic) that demonstrates two-way communication between Android and the RedBear Duo. We recommend starting with [BasicBLE](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/A03-BLEBasic) first and then playing with AdvancedBLE.

[![BLEAdvanced Demo](https://github.com/jonfroehlich/CSE590Sp2018/blob/master/A03-BLEAdvanced/YouTubeDemoScreenshot.png)](https://youtu.be/FX6jjLPIqnI "BLEAdvanced Demo")

# Arduino Sketch
1. The basic configuration is set up in ble_config.h and ble_config.cpp.
2. In the Arduino sketch, you need to define a name for your Duo board in the variable BLE_SHORT_NAME so that your Android app could find your board. The length of the name should not be longer than 8 chars.
Besides the device name, you need to explicitly define the length of the device name in the variable BLE_SHORT_NAME_LEN in Hex. Note the value is one char longer than the actual length of the device name. For example, if your device name is “Biscuit”, BLE_SHORT_NAME_LEN should be set as 0x08.
3. Do not change: UUID, TX UUID, and RX UUID.
4. There are three bytes in the message for BLE communication. The first byte indicates the operation (*e.g.,* Digital Out, Digital In, PWM, etc.) and the remaining two bytes (16 bits) stores the data (*i.e.,* the payload).

# Android Development
1. The package BLE includes two files (RBLGattAttributes and RELService) that contains the required configurations for establishing Bluetooth communication. 
2. Add the following XML for bluetooth permissions in the app manifest:
```
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-feature android:name="android.hardware.bluetooth_le"  android:required="true" />
```
3. Also add the markup `<service>` under `<application>` (this is important because otherwise we cannot use RBLService):
```
<service android:name=".RBLService" android:enabled="true" />
```
4. Currently the Android program works with targetSdkVersion 19. If the targetSdkVersion is 21 or above, add location permissions in the app manifest:
```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
5. In MainActivity, change mTargetDeviceName and mNameLen to the device name and the length defined in the Arduino code, so that your Android app could scan and find your Duo board.
6. It might take a minute or two to scan all available BLE-enabled devices and connect the paired Duo board the first time after you upload the sketch to the board and launch the Android app. 
