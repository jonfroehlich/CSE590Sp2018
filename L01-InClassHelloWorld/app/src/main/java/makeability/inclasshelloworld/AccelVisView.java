package makeability.inclasshelloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jonf on 3/29/18.
 */

public class AccelVisView extends View implements SensorEventListener{

    PointF _touchPt = new PointF();
    Paint _paint = new Paint();

    // We didn't have a chance to finish this in class
    // We just started adding sensor support
    // See: https://developer.android.com/guide/topics/sensors/sensors_motion.html
    // and the other examples for Lecture1 in github
    private SensorManager _sensorManager;
    private Sensor _accelSensor;

    public AccelVisView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AccelVisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AccelVisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        _paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas canvas){

        canvas.drawCircle(_touchPt.x, _touchPt.y, 150, _paint);
    }

    @Override
    /**
     * See https://developer.android.com/training/custom-views/making-interactive.html#inputgesture
     * Note: in class, I was using the onTouch method as part of the OnTouchListener interface;
     * however, as I briefly (in passing) mentioned in class, this was unnecessary because
     * the View superclass already has a method onTouchEvent
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {

        _touchPt.set(motionEvent.getX(), motionEvent.getY());
        invalidate();
        Log.d("onTouch", "_touchPt: " + _touchPt);

        // Return true if you handled the event and false if you want the event to further
        // propagate up the UI component hierarchy. See: https://developer.android.com/training/gestures/viewgroup.html
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
