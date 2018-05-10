package io.makeabilitylab.opencvtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public final class CameraUtils {
    private CameraUtils(){}

    /**
     * Checks if the app has permission to use the camera
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * From: https://stackoverflow.com/a/44087946
     * @param activity
     */
    public static void verifyAndAskForExternalStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            // For API 23+ you need to request camera permissions even if they are already in your manifest.
            // See: http://developer.android.com/training/permissions/requesting.html
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    1);

        }
    }
}
