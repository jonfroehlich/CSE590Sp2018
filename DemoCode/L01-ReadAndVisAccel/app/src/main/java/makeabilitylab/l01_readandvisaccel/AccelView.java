package makeabilitylab.l01_readandvisaccel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jonf on 3/26/2018.
 * Represents accelerometer values as circles
 * Draws raw accel values in top row and smoothed accel values in bottom row
 */

public class AccelView extends View implements SensorEventListener {

    private static int MAX_ACCEL_VALUE = 30;

    // Increasing the size of the smoothing window will increasingly smooth the accel signal; however,
    // at a cost of responsiveness. Play around with different window sizes: 20, 50, 100...
    // Note that I've implemented a simple Mean Filter smoothing algorithm
    private static int SMOOTHING_WINDOW_SIZE = 20;

    // https://developer.android.com/training/custom-views/custom-drawing.html#createobject
    // Creating objects ahead of time is an important optimization. Views are redrawn very frequently, and many
    // drawing objects require expensive initialization. Creating drawing objects within your onDraw() method
    // significantly reduces performance and can make your UI appear sluggish.
    private Paint _paintXAccel = new Paint();
    private Paint _paintYAccel = new Paint();
    private Paint _paintZAccel = new Paint();
    
    // Setup text paint objects
    private Paint _paintRawXText = new Paint();
    private Paint _paintRawYText = new Paint();
    private Paint _paintRawZText = new Paint();

    private Paint _paintSmoothXText = new Paint();
    private Paint _paintSmoothYText = new Paint();
    private Paint _paintSmoothZText = new Paint();

    // keep track of our center points
    private PointF _ptRawXCenter = new PointF();
    private PointF _ptRawYCenter = new PointF();
    private PointF _ptRawZCenter = new PointF();

    private PointF _ptSmoothXCenter = new PointF();
    private PointF _ptSmoothYCenter = new PointF();
    private PointF _ptSmoothZCenter = new PointF();
    private float _maxRadius = 10f;

    // accelerometer stuff
    private SensorManager _sensorManager;
    private Sensor _accelSensor;
    private float _rawAccelValues[] = new float[3];

    // smoothing accelerometer signal stuff
    private float _accelValueHistory[][] = new float[3][SMOOTHING_WINDOW_SIZE];
    private float _runningAccelTotal[] = new float[3];
    private float _curAccelAvg[] = new float[3];
    private int _curReadIndex = 0;

    public AccelView(Context context) {
        super(context);
        init(null, null, 0);
    }

    public AccelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AccelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {

        // Was just playing around with logging on the Honor7X. I could only get
        // i, w, and e levels to work (corresponding to information, warning, and
        // error). See: https://developer.android.com/reference/android/util/Log.html
        Log.e("init", "ERROR init()");
        Log.w("init", "WARN init()");
        Log.i("init", "INFO init()");
        Log.d("init", "DEBUG init()");
        Log.v("init", "VERBOSE init()");

        // Typically, I would use the static function Color.argb but this was only added in API 26,
        // so using parseColor which takes in a hex string, added in API 1
        // See: https://developer.android.com/reference/android/graphics/Color.html#parseColor(java.lang.String)
        _paintXAccel.setColor(Color.parseColor("#22EE0000")); //reddish
        _paintYAccel.setColor(Color.parseColor("#2200EE00")); //greenish
        _paintZAccel.setColor(Color.parseColor("#220000EE")); //blueish
        _paintRawXText.setColor(Color.BLACK);
        _paintRawYText.setColor(Color.BLACK);
        _paintRawZText.setColor(Color.BLACK);
        _paintSmoothXText.setColor(Color.BLACK);
        _paintSmoothYText.setColor(Color.BLACK);
        _paintSmoothZText.setColor(Color.BLACK);
        
        // See https://developer.android.com/guide/topics/sensors/sensors_motion.html
        _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        _accelSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // The official Google accelerometer example code found here:
        //   https://github.com/android/platform_development/blob/master/samples/AccelerometerPlay/src/com/example/android/accelerometerplay/AccelerometerPlayActivity.java
        // explains that it is not necessary to get accelerometer events at a very high rate, by using a slower rate (SENSOR_DELAY_UI), we get an
        // automatic low-pass filter, which "extracts" the gravity component of the acceleration. As an added benefit, we use less power and
        // CPU resources. I haven't experimented with this, so can't be sure.
        // See also: https://developer.android.com/reference/android/hardware/SensorManager.html#SENSOR_DELAY_UI
        _sensorManager.registerListener(this, _accelSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    // Friendly tip, select Ctrl+O to see methods to override
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Split screen into thirds. Put center x points of each x, y, z circle in
        // center of each third (respectively)
        float maxDiameter = w / 3f;
        _maxRadius = maxDiameter / 2f;

        // Since we are drawing both raw and smoothed versions of signal, split up
        // height for both
        float yRawCenter = h / 2f - 120;
        float ySmoothedCenter = h / 2f + 120;

        _ptRawXCenter.set(_maxRadius, yRawCenter);
        _ptRawYCenter.set(maxDiameter + _maxRadius, yRawCenter);
        _ptRawZCenter.set(2 * maxDiameter + _maxRadius, yRawCenter);

        _ptSmoothXCenter.set(_maxRadius, ySmoothedCenter);
        _ptSmoothYCenter.set(maxDiameter + _maxRadius, ySmoothedCenter);
        _ptSmoothZCenter.set(2 * maxDiameter + _maxRadius, ySmoothedCenter);
    }

    @Override
    public void onDraw(Canvas canvas){
        //canvas.drawCircle(_curTouchPt.x, _curTouchPt.y, _touchPtRadius, _touchPaint);
        float xRawRadius = Math.abs(_rawAccelValues[0]) / MAX_ACCEL_VALUE * _maxRadius;
        float yRawRadius = Math.abs(_rawAccelValues[1]) / MAX_ACCEL_VALUE * _maxRadius;
        float zRawRadius = Math.abs(_rawAccelValues[2]) / MAX_ACCEL_VALUE * _maxRadius;

        canvas.drawCircle(_ptRawXCenter.x, _ptRawXCenter.y, xRawRadius, _paintXAccel);
        canvas.drawCircle(_ptRawYCenter.x, _ptRawYCenter.y, yRawRadius, _paintYAccel);
        canvas.drawCircle(_ptRawZCenter.x, _ptRawZCenter.y, zRawRadius, _paintZAccel);

        // Now draw smoothed circle
        float xSmoothedRadius = Math.abs(_curAccelAvg[0]) / MAX_ACCEL_VALUE * _maxRadius;
        float ySmoothedRadius = Math.abs(_curAccelAvg[1]) / MAX_ACCEL_VALUE * _maxRadius;
        float zSmoothedRadius = Math.abs(_curAccelAvg[2]) / MAX_ACCEL_VALUE * _maxRadius;

        canvas.drawCircle(_ptSmoothXCenter.x, _ptSmoothXCenter.y, xSmoothedRadius, _paintXAccel);
        canvas.drawCircle(_ptSmoothYCenter.x, _ptSmoothYCenter.y, ySmoothedRadius, _paintYAccel);
        canvas.drawCircle(_ptSmoothZCenter.x, _ptSmoothZCenter.y, zSmoothedRadius, _paintZAccel);

        // Need to measure text bounds to properly center text within each circle
        Rect xTextBounds = new Rect();
        Rect yTextBounds = new Rect();
        Rect zTextBounds = new Rect();
        String xText = "x";
        String yText = "y";
        String zText = "z";

        // Dynamically scale text based on accel values
        _paintRawXText.setTextSize(xRawRadius);
        _paintRawYText.setTextSize(yRawRadius);
        _paintRawZText.setTextSize(zRawRadius);
        
        _paintRawXText.getTextBounds(xText, 0, xText.length(), xTextBounds);
        _paintRawYText.getTextBounds(xText, 0, yText.length(), yTextBounds);
        _paintRawZText.getTextBounds(xText, 0, zText.length(), zTextBounds);
        
        canvas.drawText(xText, _ptRawXCenter.x - xTextBounds.width()/2f, _ptRawXCenter.y + xTextBounds.height() / 2f, _paintRawXText);
        canvas.drawText(yText, _ptRawYCenter.x - yTextBounds.width()/2f, _ptRawYCenter.y + yTextBounds.height() / 2f, _paintRawYText);
        canvas.drawText(zText, _ptRawZCenter.x - zTextBounds.width()/2f, _ptRawZCenter.y + zTextBounds.height() / 2f, _paintRawZText);

        // Draw smooth text
        _paintSmoothXText.setTextSize(xSmoothedRadius);
        _paintSmoothYText.setTextSize(ySmoothedRadius);
        _paintSmoothZText.setTextSize(zSmoothedRadius);

        _paintSmoothXText.getTextBounds(xText, 0, xText.length(), xTextBounds);
        _paintSmoothYText.getTextBounds(xText, 0, yText.length(), yTextBounds);
        _paintSmoothZText.getTextBounds(xText, 0, zText.length(), zTextBounds);

        canvas.drawText(xText, _ptSmoothXCenter.x - xTextBounds.width()/2f, _ptSmoothXCenter.y + xTextBounds.height() / 2f, _paintSmoothXText);
        canvas.drawText(yText, _ptSmoothYCenter.x - yTextBounds.width()/2f, _ptSmoothYCenter.y + yTextBounds.height() / 2f, _paintSmoothYText);
        canvas.drawText(zText, _ptSmoothZCenter.x - zTextBounds.width()/2f, _ptSmoothZCenter.y + zTextBounds.height() / 2f, _paintSmoothZText);
    }
    
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                _rawAccelValues[0] = sensorEvent.values[0];
                _rawAccelValues[1] = sensorEvent.values[1];
                _rawAccelValues[2] = sensorEvent.values[2];

                // Smoothing algorithm adapted from: https://www.arduino.cc/en/Tutorial/Smoothing
                for (int i = 0; i < 3; i++) {
                    _runningAccelTotal[i] = _runningAccelTotal[i] - _accelValueHistory[i][_curReadIndex];
                    _accelValueHistory[i][_curReadIndex] = _rawAccelValues[i];
                    _runningAccelTotal[i] = _runningAccelTotal[i] + _accelValueHistory[i][_curReadIndex];
                    _curAccelAvg[i] = _runningAccelTotal[i] / SMOOTHING_WINDOW_SIZE;
                }

                _curReadIndex++;
                if(_curReadIndex >= SMOOTHING_WINDOW_SIZE){
                    _curReadIndex = 0;
                }

                invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
