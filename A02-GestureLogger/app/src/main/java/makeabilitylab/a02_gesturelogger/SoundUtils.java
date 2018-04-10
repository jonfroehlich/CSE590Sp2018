package makeabilitylab.a02_gesturelogger;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Playing beeps
 *   - https://stackoverflow.com/questions/29509010/how-to-play-a-short-beep-to-android-phones-loudspeaker-programmatically?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 *   - https://stackoverflow.com/questions/6462105/how-do-i-access-androids-default-beep-sound
 */
public final class SoundUtils {
    private SoundUtils() {
    }

    // AudioManager.STREAM_NOTIFICATION is the Android volume set for notifications
    private static ToneGenerator _toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    /**
     * Plays a tone
     * TODO: potential bug: there is some sort of delay in executing the tone
     * See: https://stackoverflow.com/a/49514035
     * @param durationMs
     */
    public static void playTone(int durationMs) {
        long startTime = System.currentTimeMillis();

        // TONE_PROP_PROMPT or TONE_PROP_BEEP
        _toneGenerator.startTone(ToneGenerator.TONE_PROP_PROMPT, durationMs);
        Log.i("playTone","Took " + (System.currentTimeMillis() - startTime) + " ms to complete startTone");

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                _toneGenerator.stopTone();
//                _toneGenerator.release();
//            }
//        }, (long)(durationMs + 10));
    }
}
