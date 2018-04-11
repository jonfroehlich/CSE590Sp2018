package makeabilitylab.a02_gesturelogger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon Froehlich, April 9, 2018
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static int COUNTDOWN_START_VALUE_IN_SECS = 3;

    private boolean _isRecordingGesture = false;
    private int _countDownValInSecs = COUNTDOWN_START_VALUE_IN_SECS;

    // Sensor stuff
    private SensorManager _sensorManager;
    private Sensor _accelSensor;
    private Sensor _gyroSensor;

    // Sensor logging stuff
    private List<SensorEventCache> _accelSensorEventLog = new ArrayList<>();
    private List<SensorEventCache> _gyroSensorEventLog = new ArrayList<>();
    private String _strSaveDirectory = Environment.DIRECTORY_DOWNLOADS;

    // Default gestures to record
    private static final String [] GESTURES = new String[]{
        "Backhand Tennis", "Forehand Tennis", "Underhand Bowling", "Baseball Throw", "At Rest", "Midair Clockwise 'O'", "Midair Counter Clockwise 'O'", "Midair Zorro 'Z'", "Midair 'S'", "Shake", "Your Custom Gesture"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainLayout = findViewById(R.id.layoutMain);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecordingGesture();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GESTURES);
        Spinner spinnerGestures = (Spinner) findViewById(R.id.spinnerGestures);
        spinnerGestures.setAdapter(arrayAdapter);

        spinnerGestures.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                TextView textViewCountdown = (TextView)findViewById(R.id.textViewCountdown);
                textViewCountdown.setText(String.format("Waiting to record a '%s' gesture...", parentView.getItemAtPosition(pos).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //set the default according to value
        int defaultDirPos = arrayAdapter.getPosition("Backhand Tennis");
        spinnerGestures.setSelection(defaultDirPos);

        // See https://developer.android.com/guide/topics/sensors/sensors_motion.html
        _sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        _accelSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _gyroSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // See: https://developer.android.com/reference/android/hardware/SensorManager.html#SENSOR_DELAY_UI
        // OK, I did some basic experiments, it looks like for:
        // SENSOR_DELAY_FASTEST : ~200 - 250Hz
        // SENSOR_DELAY_NORMAL : ~30 - 50Hz
        // Not sure how device dependent this is or what would be best for gesture recognition
        _sensorManager.registerListener(this, _accelSensor, SensorManager.SENSOR_DELAY_GAME);
        _sensorManager.registerListener(this, _gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onButtonStartRecording(View v){
        Button button = (Button)v;
        if(_isRecordingGesture){
            button.setEnabled(true);
            button.setText("Start Recording");
            stopRecordingGesture();
        }else{
            _countDownValInSecs = COUNTDOWN_START_VALUE_IN_SECS;
            button.setEnabled(false);
            button.setText("Stop Recording");

            TextView textViewCountdown = (TextView)findViewById(R.id.textViewCountdown);
            textViewCountdown.setText(String.format("%.1f", _countDownValInSecs * 1f));
            textViewCountdown.setTextSize(150);

            Spinner spinnerGestures = (Spinner)findViewById(R.id.spinnerGestures);
            String strSelectedGesture = spinnerGestures.getSelectedItem().toString();
            TextView textViewStatus = (TextView)findViewById(R.id.textViewStatus);
            textViewStatus.setText(String.format("Prepare to record a '%s' gesture", strSelectedGesture));

            // CountDownTimer is an abstract class so here we make an anonymous class extension
            // The two params here are the number of millis until onFinish is called and the interval along the way
            // to receive onTick
            new CountDownTimer((_countDownValInSecs * 1000) + 50, 10) {

                private long _timestampStartMs = System.currentTimeMillis();

                private void onTickInternal(){
                    Log.i("onTickInternal","currentTimeMillis " + System.currentTimeMillis());
                    long deltaMs = System.currentTimeMillis() - _timestampStartMs;
                    float countdownTimeSecs = _countDownValInSecs - deltaMs / 1000.0f;
                    if(countdownTimeSecs < 0){ countdownTimeSecs = 0; }

                    String strCountdownTime = String.format("%.1f", countdownTimeSecs);
                    TextView textViewCountdown = (TextView)findViewById(R.id.textViewCountdown);
                    textViewCountdown.setText(strCountdownTime);
                }

                public void onTick(long millisUntilFinished) {
                    Log.i("onTick","millisUntilFinished " + millisUntilFinished);
                    onTickInternal();
                }

                public void onFinish() {

                    TextView textViewCountdown = (TextView)findViewById(R.id.textViewCountdown);
                    _countDownValInSecs = 0;
                    textViewCountdown.setTextSize(50);
                    textViewCountdown.setText("Logging...\n\nTouch anywhere to stop!");

                    SoundUtils.playTone(1000);

                    Button buttonStartRecording = (Button)findViewById(R.id.buttonStartRecording);
                    buttonStartRecording.setEnabled(true);

                    TextView textViewStatus = (TextView)findViewById(R.id.textViewStatus);
                    Spinner spinnerGestures = (Spinner) findViewById(R.id.spinnerGestures);
                    textViewStatus.setText("Recording the '" + spinnerGestures.getSelectedItem().toString() + "' gesture...");
                    _isRecordingGesture = true;
                }
            }.start();
        }
    }

    private void stopRecordingGesture(){
        if(_isRecordingGesture){

            File path = Environment.getExternalStoragePublicDirectory(_strSaveDirectory);
            Spinner spinnerGestures = (Spinner)findViewById(R.id.spinnerGestures);
            String strSelectedGesture = spinnerGestures.getSelectedItem().toString();
            strSelectedGesture = strSelectedGesture.replace("'", "");

            long currentTimeMs = System.currentTimeMillis();
            String accelFilename = strSelectedGesture + "_Accelerometer_" + currentTimeMs + ".csv";
            saveSensorEventCacheToFile(path, accelFilename, _accelSensorEventLog);

            String gyroFilename = strSelectedGesture + "_Gyroscope_" + currentTimeMs + ".csv";
            saveSensorEventCacheToFile(path, gyroFilename, _gyroSensorEventLog);

            _accelSensorEventLog.clear();
            _gyroSensorEventLog.clear();

            _isRecordingGesture = false;
            Button buttonStartRecording = (Button)findViewById(R.id.buttonStartRecording);
            buttonStartRecording.setText("Start Recording");
            TextView textViewCountdown = (TextView)findViewById(R.id.textViewCountdown);
            textViewCountdown.setTextSize(50);
            textViewCountdown.setText(String.format("Waiting to record a '%s' gesture...", strSelectedGesture));

            TextView textViewStatus = (TextView)findViewById(R.id.textViewStatus);
            textViewStatus.setText("Select a gesture to record and hit 'Start Recording'");
        }
    }

    /**
     * Saves the provided sensor event cache to file
     * @param path
     * @param filename
     * @param listSensorEventCache
     */
    private void saveSensorEventCacheToFile(File path, String filename, List<SensorEventCache> listSensorEventCache){
        boolean wasPathCreated = path.mkdir();
        if(wasPathCreated){
            Log.i("saveToFile", "Created path '" + path.getAbsolutePath());
        }

        boolean isExternalStorageWritable = FileUtils.isExternalStorageWritable();
        boolean isExternalStorageReadable = FileUtils.isExternalStorageReadable();
        boolean permissionToWriteToExternalStorage = FileUtils.checkPermissionToWriteToExternalStorage(this);
        Log.i("saveToFile", String.format("isExternalStorageWritable=%s, isExternalStorageReadable=%s, permissionToWriteToExternalStorage=%s",
                isExternalStorageWritable, isExternalStorageReadable, permissionToWriteToExternalStorage));

        // Even with updating the manifest, I could not get Android to properly write to external storage
        // until I added this. I think this is a new Android requirement to force user involvement
        // when writing to disk
        FileUtils.verifyAndAskForExternalStoragePermissions(this);

        // This should now be true.
        boolean permissionToWriteToExternalStorageAfterUserRequest = FileUtils.checkPermissionToWriteToExternalStorage(this);
        Log.i("saveToFile", String.format("permissionToWriteToExternalStorageAfterUserRequest=%s", permissionToWriteToExternalStorageAfterUserRequest));

        File file = new File(path, filename);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);

            int line = 0;
            for(SensorEventCache sensorEventCacheItem : listSensorEventCache){
                if(line == 0){
                    fileWriter.write(sensorEventCacheItem.getCsvHeaderString());
                }
                fileWriter.write(sensorEventCacheItem.toCsvString());
                line++;
            }

            fileWriter.close();
            Toast toast = Toast.makeText(this, "Saved " + listSensorEventCache.size() + " sensor event records to " + file.getAbsolutePath(), Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // long nanoTimestamp = System.nanoTime();
        long currentTimeMs = System.currentTimeMillis();

        switch(sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if(_isRecordingGesture){
                    MotionSensorEventCache eventCache = new MotionSensorEventCache(sensorEvent.sensor.getStringType(),
                            currentTimeMs, sensorEvent.timestamp, sensorEvent.values);
                    _accelSensorEventLog.add(eventCache);
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                if(_isRecordingGesture){
                    MotionSensorEventCache eventCache = new MotionSensorEventCache(sensorEvent.sensor.getStringType(),
                            currentTimeMs, sensorEvent.timestamp, sensorEvent.values);
                    _gyroSensorEventLog.add(eventCache);
                }
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
