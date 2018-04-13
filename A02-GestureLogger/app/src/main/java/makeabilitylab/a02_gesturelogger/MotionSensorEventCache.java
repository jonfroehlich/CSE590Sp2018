package makeabilitylab.a02_gesturelogger;

public class MotionSensorEventCache extends SensorEventCache{

    private float[] _motionValues = new float[3];

    public MotionSensorEventCache(String sensorType, long currentTimeMillis, long sensorTimestampNano,
                            float [] motionValues){
        super(sensorType, currentTimeMillis, sensorTimestampNano);
        System.arraycopy(motionValues, 0, _motionValues, 0, motionValues.length);
    }

    @Override
    public String getCsvHeaderString(){
        return "Sensor, CurrentTimeMs, SensorTimestampNano, X, Y, Z\n";
    }

    @Override
    public String toCsvString(){
        StringBuilder sb = new StringBuilder();

        sb.append(_sensorEventType);
        sb.append(",");

        sb.append(_currentTimeMs);
        sb.append(",");
        sb.append(_sensorTimeStampNano);
        sb.append(",");

        sb.append(_motionValues[0]);
        sb.append(",");
        sb.append(_motionValues[1]);
        sb.append(",");
        sb.append(_motionValues[2]);

        sb.append("\n");
        return sb.toString();
    }


}
