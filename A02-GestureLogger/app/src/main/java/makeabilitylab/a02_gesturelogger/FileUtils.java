package makeabilitylab.a02_gesturelogger;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jon on 3/16/2016.
 * Updated in April 2018 for CSE590 UbiComp
 *
 * You should read the official Android documentation about writing data to storage on Android:
 * https://developer.android.com/training/data-storage/files.html
 *
 * In particular, if you want to write data to the public external storage, you must modify
 * the permissions in your manifest file:
 * https://developer.android.com/training/data-storage/files.html#ExternalStoragePermissions
 */
public final class FileUtils {
    private FileUtils(){}

    /**
     * Checks if external storage is available for read and write
     * See: https://developer.android.com/training/data-storage/files.html#WriteExternalStorage
     *
     * If you want to write to the public external storage, you must request the WRITE_EXTERNAL_STORAGE permission in
     * your manifest file. See https://developer.android.com/training/data-storage/files.html#ExternalStoragePermissions
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     * See: https://developer.android.com/training/data-storage/files.html#WriteExternalStorage
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * From: http://stackoverflow.com/a/33292700
     * @param activity
     */
    public static void verifyAndAskForExternalStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            // For API 23+ you need to request the read/write permissions even if they are already in your manifest.
            // See: http://developer.android.com/training/permissions/requesting.html
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Checks if the app has permission to write to external storage.
     *
     * @param activity
     * @return
     */
    public static boolean checkPermissionToWriteToExternalStorage(Activity activity){
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Adds the saved image to the gallery.
     *
     * From: http://stackoverflow.com/a/20859733
     * @param filePath
     * @param context
     */
    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
