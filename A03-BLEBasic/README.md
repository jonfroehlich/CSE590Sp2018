BasicBLE is a simplification of AdvancedBLE and demonstrates one-way communication from an Android app to the RedBear Duo. After getting BasicBLE to work, then try AdvancedBLE would illustrates two-way communication between Android <-> the RedBear Duo.

# Arduino Sketch
The basic configuration is set up ble_config.h and ble_config.cpp.
In the Arduino sketch, you need to define a name for your Duo board in the variable BLE_SHORT_NAME so that your Android app could find your board. The length of the name should not be longer than 8 chars.
Besides the device name, you need to explicitly define the length of the device name in the variable BLE_SHORT_NAME_LEN in Hex. Note the value is one char longer than the actual length of the device name. For example, if your device name is “Biscuit”, BLE_SHORT_NAME_LEN should be set as 0x08.
Keep UUID, TX UUID, RX UUID unchanged.
There are three bytes in the message for BLE communication. The first byte indicates what operations (e.g., Digital Out, Digital In, PWM, etc.). The rest stores the data (16 bits).


# Android Development
The package BLE includes two files (RBLGattAttributes and RELService) that contains all required configurations for establishing the Bluetooth communication. 
Add markups for bluetooth permissions in manifests:

<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-feature android:name="android.hardware.bluetooth_le"  android:required="true" />

Add markup <service> under <application> (this is important because otherwise we cannot use RBLService):

<service android:name=".RBLService" android:enabled="true" />

In MainActivity, change mTargetDeviceName and mNameLen to the device name and the length defined in the Arduino code, so that your Android app could scan and find your Duo board.
It might take about one minute to scan all available BLE-enabled devices and connect the paired Duo board the first time after you upload the sketch to the board and launch the Android app. 
