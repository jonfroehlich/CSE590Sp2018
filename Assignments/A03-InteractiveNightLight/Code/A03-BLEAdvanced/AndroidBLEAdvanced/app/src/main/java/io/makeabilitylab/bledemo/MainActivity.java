/* BLE demo: Demonstrate how to bi-directionally communicate with the Duo board through BLE. In this
 * example code, it shows how to send digital and analog data from the Android app to the Duo board
 * and how to receive data from the board.
 *
 * The app is built based on the example code provided by the RedBear Team:
 * https://github.com/RedBearLab/Android
 */

package io.makeabilitylab.bledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import io.makeabilitylab.bledemo.ble.BLEDevice;
import io.makeabilitylab.bledemo.ble.BLEListener;
import io.makeabilitylab.bledemo.ble.BLEUtil;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BLEListener {

    // TODO: Define your device name and the length of the name. For your assignment, do not use the
    // default name or you will not be able to discriminate your board from everyone else's board.
    // Note the device name and the length should be consistent with the ones defined in the Duo sketch
    private final String TARGET_DEVICE_NAME = "MakeLab";

    // Declare all variables associated with the UI components
    private Button mConnectBtn = null;
    private TextView mDeviceName = null;
    private TextView mRssiValue = null;
    private TextView mUUID = null;
    private TextView mAnalogInValue = null;
    private ToggleButton mDigitalOutBtn, mDigitalInBtn, mAnalogInBtn;
    private SeekBar mServoSeekBar, mPWMSeekBar;

    // Declare all Bluetooth stuff
    private BLEDevice mBLEDevice;

    private void setButtonDisable() {
        mDigitalOutBtn.setEnabled(false);
        mAnalogInBtn.setEnabled(false);
        mServoSeekBar.setEnabled(false);
        mPWMSeekBar.setEnabled(false);
        mConnectBtn.setText("Connect");
        mRssiValue.setText("");
        mDeviceName.setText("");
        mUUID.setText("");
    }

    private void setButtonEnable() {
        mDigitalOutBtn.setEnabled(true);
        mAnalogInBtn.setEnabled(true);
        mServoSeekBar.setEnabled(true);
        mPWMSeekBar.setEnabled(true);
        mConnectBtn.setText("Disconnect");
    }

    // Display the received RSSI on the interface
    private void displayData(int rssi) {
        mRssiValue.setText(String.format(Locale.US, "%d", rssi));
        mDeviceName.setText(mBLEDevice.getName());
        mUUID.setText(mBLEDevice.getUUID().toString());
    }

    // Display the received Analog/Digital read on the interface
    private void readAnalogInValue(byte[] data) {
        for (int i = 0; i < data.length; i += 3) {
            if (data[i] == 0x0A) {
                if (data[i + 1] == 0x01)
                    mDigitalInBtn.setChecked(false);
                else
                    mDigitalInBtn.setChecked(true);
            } else if (data[i] == 0x0B) {
                int value;
                value = ((data[i + 1] << 8) & 0x0000ff00)
                        | (data[i + 2] & 0x000000ff);
                mAnalogInValue.setText(String.format(Locale.US, "%d", value));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Associate all UI components with variables
        mConnectBtn = findViewById(R.id.connectBtn);
        mDeviceName = findViewById(R.id.deviceName);
        mRssiValue = findViewById(R.id.rssiValue);
        mAnalogInValue = findViewById(R.id.ananlogIn);
        mDigitalOutBtn = findViewById(R.id.DOutBtn);
        mDigitalInBtn = findViewById(R.id.DInBtn);
        mAnalogInBtn = findViewById(R.id.AInBtn);
        mServoSeekBar = findViewById(R.id.ServoSeekBar);
        mPWMSeekBar = findViewById(R.id.PWMSeekBar);
        mUUID = findViewById(R.id.uuidValue);

        // Connection button click event
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBLEDevice.getState() == BLEDevice.State.CONNECTED) {
                    mBLEDevice.disconnect();
                } else {
                    //TODO: Ideally we would present the user with a "wait" message until
                    //a connection was made or a timeout occurred... but this is better than nothing for now
                    Toast toast = Toast.makeText(
                            MainActivity.this,
                            "Attempting to connect to '" + TARGET_DEVICE_NAME + "'",
                            Toast.LENGTH_LONG);
                    toast.show();

                    mBLEDevice.connect();
                }
            }
        });

        // Send data to Duo board
        // It has three bytes: maker, data value, reserved
        mDigitalOutBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte buf[] = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00 };
                if (isChecked)
                    buf[1] = 0x01;
                else
                    buf[1] = 0x00;
                mBLEDevice.sendData(buf);
            }
        });

        mAnalogInBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte[] buf = new byte[] { (byte) 0xA0, (byte) 0x00, (byte) 0x00 };
                if (isChecked)
                    buf[1] = 0x01;
                else
                    buf[1] = 0x00;
                mBLEDevice.sendData(buf);
            }
        });

        // Configure the servo Seekbar
        mServoSeekBar.setEnabled(false);
        mServoSeekBar.setMax(180);  // Servo can rotate from 0 to 180 degree
        mServoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                byte[] buf = new byte[] { (byte) 0x03, (byte) 0x00, (byte) 0x00 };
                buf[1] = (byte) mServoSeekBar.getProgress();
                mBLEDevice.sendData(buf);
            }
        });

        // Configure the PWM Seekbar
        mPWMSeekBar.setEnabled(false);
        mPWMSeekBar.setMax(255);
        mPWMSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                byte[] buf = new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x00 };
                buf[1] = (byte) mPWMSeekBar.getProgress();
                mBLEDevice.sendData(buf);
            }
        });

        // Make sure that Bluetooth is supported.
        if (!BLEUtil.isSupported(this)) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        // Make sure that we have required permissions.
        if (!BLEUtil.hasPermission(this)) {
            BLEUtil.requestPermission(this);
        }

        // Make sure that Bluetooth is enabled.
        if (!BLEUtil.isBluetoothEnabled(this)) {
            BLEUtil.requestEnableBluetooth(this);
        }

        mBLEDevice = new BLEDevice(this, TARGET_DEVICE_NAME);
        mBLEDevice.addListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!BLEUtil.isBluetoothEnabled(this)) {
            BLEUtil.requestEnableBluetooth(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBLEDevice != null) {
            mBLEDevice.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == BLEUtil.REQUEST_ENABLE_BLUETOOTH
                && !BLEUtil.isBluetoothEnabled(this)) {
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        // User chose not to grant required permissions.
        if (requestCode == BLEUtil.REQUEST_BLUETOOTH_PERMISSIONS
                && !BLEUtil.hasPermission(this)) {
            finish();
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBleConnected() {
        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
        setButtonEnable();
    }

    @Override
    public void onBleConnectFailed() {
        Toast toast = Toast
                .makeText(
                        MainActivity.this,
                        "Couldn't find the BLE device with name '" + TARGET_DEVICE_NAME + "'!",
                        Toast.LENGTH_SHORT);
        toast.setGravity(0, 0, Gravity.CENTER);
        toast.show();
    }

    @Override
    public void onBleDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        setButtonDisable();
    }

    @Override
    public void onBleDataReceived(byte[] data) {
        readAnalogInValue(data);
    }

    @Override
    public void onBleRssiChanged(int rssi) {
        displayData(rssi);
    }
}
