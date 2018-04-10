package makeabilitylab.a02_gesturelogger;

public class SensorEventCache {


    // Saves TYPE_ACCELEROMETER
    private float[] _accelValues = new float[3];

    // Saves TYPE_GYROSCOPE
    private float[] _gyroValues = new float[3];

    private long _currentTimeMs = -1;

    // The time in nanoseconds at which the sensorevent happened. See https://developer.android.com/reference/android/hardware/SensorEvent.html
    private long _sensorTimeStampNano = -1;
    private String _sensorEventType = null;

    public SensorEventCache(String sensorType, long currentTimeMillis, long sensorTimestampNano,
                            float [] accelValues, float [] gyroValues){
        _sensorEventType = sensorType;
        _currentTimeMs = currentTimeMillis;
        _sensorTimeStampNano = sensorTimestampNano;
        System.arraycopy(accelValues, 0, _accelValues, 0, accelValues.length);
        System.arraycopy(gyroValues, 0, _gyroValues, 0, gyroValues.length);
    }

    public static String getCsvHeaderString(){
        return "Sensor, CurrentTimeMs, SensorTimestampNano, AccelX, AccelY, AccelZ, GyroX, GyroY, GyroZ\n";
    }

    public String toCsvString(){
        StringBuilder sb = new StringBuilder();

        sb.append(_sensorEventType);
        sb.append(",");

        sb.append(_currentTimeMs);
        sb.append(",");
        sb.append(_sensorTimeStampNano);
        sb.append(",");

        sb.append(_accelValues[0]);
        sb.append(",");
        sb.append(_accelValues[1]);
        sb.append(",");
        sb.append(_accelValues[2]);
        sb.append(",");

        sb.append(_gyroValues[0]);
        sb.append(",");
        sb.append(_gyroValues[1]);
        sb.append(",");
        sb.append(_gyroValues[2]);
       
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString(){
        return toCsvString();
    }
}
