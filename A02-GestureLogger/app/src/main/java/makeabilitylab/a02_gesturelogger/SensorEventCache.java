package makeabilitylab.a02_gesturelogger;

public abstract class SensorEventCache{

    protected long _currentTimeMs = -1;

    // The time in nanoseconds at which the sensorevent happened. See https://developer.android.com/reference/android/hardware/SensorEvent.html
    protected long _sensorTimeStampNano = -1;
    protected String _sensorEventType = null;

    public SensorEventCache(String sensorEventType, long currentTimeMs, long sensorTimestampNano){
        _sensorEventType = sensorEventType;
        _currentTimeMs = currentTimeMs;
        _sensorTimeStampNano = sensorTimestampNano;
    }

    public abstract String getCsvHeaderString();
    public abstract String toCsvString();

    @Override
    public String toString(){
        return toCsvString();
    }
}

