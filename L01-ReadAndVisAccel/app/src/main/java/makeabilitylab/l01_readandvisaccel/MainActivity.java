package makeabilitylab.l01_readandvisaccel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // Sensor stuff
    private SensorManager _sensorManager;
    private Sensor _accelSensor;
    private float _gravity[] = new float[3];
    private float _processedAcceleration[] = new float[3];
    private float _linearAcceleration[] = new float[3];
    private float _rawAccelerometerValues[] = new float[3];
    private long _lastTimeAccelSensorChangedInMs = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        _accelSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        switch(event.sensor.getType()){
//            case Sensor.TYPE_ACCELEROMETER:
//                if(_lastTimeAccelSensorChangedInMs == -1){
//                    _lastTimeAccelSensorChangedInMs = SystemClock.elapsedRealtime();
//                }
//                long curTimeInMs = SystemClock.elapsedRealtime();
//
//                // Set the raw values, the x accel value is in 0, the y accel value is in 1, and the z accel value is in 2
//                _rawAccelerometerValues[0] = event.values[0];
//                _rawAccelerometerValues[1] = event.values[1];
//                _rawAccelerometerValues[2] = event.values[2];
//
//                // smooth the accelerometer signal and remove gravity
//                // from https://developer.android.com/guide/topics/sensors/sensors_motion.html
//                // In this example, alpha is calculated as t / (t + dT),
//                // where t is the low-pass filter's time-constant and
//                // dT is the event delivery rate.
//                final float alpha = 0.8f;
//
//                // Isolate the force of gravity with the low-pass filter.
//                _gravity[0] = alpha * _gravity[0] + (1 - alpha) * _rawAccelerometerValues[0];
//                _gravity[1] = alpha * _gravity[1] + (1 - alpha) * _rawAccelerometerValues[1];
//                _gravity[2] = alpha * _gravity[2] + (1 - alpha) * _rawAccelerometerValues[2];
//
//                // Remove the gravity contribution with the high-pass filter.
//                _linearAcceleration[0] = _rawAccelerometerValues[0] - _gravity[0];
//                _linearAcceleration[1] = _rawAccelerometerValues[1] - _gravity[1];
//                _linearAcceleration[2] = _rawAccelerometerValues[2] - _gravity[2];
//
//                // Processed acceleration
//                final int multiplier = 50;
//                _processedAcceleration[0] = _linearAcceleration[0] * multiplier;
//                _processedAcceleration[1] = _linearAcceleration[1] * multiplier;
//                _processedAcceleration[2] = _linearAcceleration[2] * multiplier;
//
//
//                _lastTimeAccelSensorChangedInMs = curTimeInMs;
//                break;
//        }
//    }
}
