package makeabilitylab.l03_savingdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    // Keep track of the default directory so that the save as dialog appropriately switches
    private String _strDefaultDirectory = Environment.DIRECTORY_DOWNLOADS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonSave(View view) {

        CheckBox checkBoxFilenamePrompt = (CheckBox)findViewById(R.id.checkBoxPromptForFilename);
        long currentTimeMs = System.currentTimeMillis();
        String strDefaultFilename = "MakeLabExample_" + currentTimeMs + ".csv";

        if (checkBoxFilenamePrompt.isChecked()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Get the layout inflater. LayoutInflaters take a layout XML file and create its
            // corresponding View objects. Never create LayoutInflaters directly. Always use the
            // factory method getLayoutInflater. See https://developer.android.com/reference/android/view/LayoutInflater.html
            LayoutInflater inflater = this.getLayoutInflater();

            // Inflate the dialog xml layout & setup the widgets
            final View dialogView = inflater.inflate(R.layout.dialog_save_file, null);
            EditText editText = (EditText) dialogView.findViewById(R.id.editTextFilename);
            editText.setText(strDefaultFilename);

            Spinner spinnerDirectories = (Spinner) dialogView.findViewById(R.id.spinnerDirectories);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, FileUtils.STANDARD_EXTERNAL_ROOT_DIRECTORIES); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDirectories.setAdapter(spinnerArrayAdapter);

            //set the default according to value
            int defaultDirPos = spinnerArrayAdapter.getPosition(_strDefaultDirectory);
            spinnerDirectories.setSelection(defaultDirPos);

            // This is the method that allows us to use our own custom view for an alert dialog.
            // We set the AlertDialog builder to the view we created with the inflater above.
            builder.setView(dialogView)
                    // Add action buttons
                    .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Activity activity = (Activity) dialogView.getContext();

                            EditText editText = (EditText) dialogView.findViewById(R.id.editTextFilename);
                            String updatedFilename = editText.getText().toString();

                            _strDefaultDirectory = ((Spinner)dialogView.findViewById(R.id.spinnerDirectories)).getSelectedItem().toString();
                            File path = Environment.getExternalStoragePublicDirectory(_strDefaultDirectory);
                            saveToFile(path, updatedFilename);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Activity activity = (Activity) dialogView.getContext();
                            Toast toast = Toast.makeText(activity, "Saving canceled...", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            File path = Environment.getExternalStoragePublicDirectory(_strDefaultDirectory);
            saveToFile(path, strDefaultFilename);
        }
    }

    /**
     * Saves the R.id.editTextToSaveToFile contents to the given file
     * @param saveToFilename
     */
    private void saveToFile(File path, String saveToFilename){
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOWNLOADS);

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

        File file = new File(path, saveToFilename);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            String strToSaveToFile = findViewById(R.id.editTextToSaveToFile).toString();
            fileWriter.write(strToSaveToFile);
            fileWriter.close();

            Toast toast = Toast.makeText(this, String.format("Saved %s to %s", saveToFilename, path.getAbsolutePath()), Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
