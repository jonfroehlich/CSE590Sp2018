package makeabilitylab.l01_basicinteractions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used in the alert dialog
    private int _bgColorSaved = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Demonstrating how to hook up a button in Java via an anonymous class that implements
        // OnClickListener
        Button buttonToggleMorning = (Button)findViewById(R.id.buttonToggleMorning);
        buttonToggleMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewMsgMorning = (TextView)findViewById(R.id.textViewGoodMorningMessage);
                if(textViewMsgMorning.getText().toString().startsWith("Good M")) {
                    textViewMsgMorning.setText("Goodnight!");
                    textViewMsgMorning.setTextColor(Color.WHITE);

                    ViewGroup viewGroup = (ViewGroup)findViewById(R.id.activity_main);
                    viewGroup.setBackgroundColor(Color.BLACK);
                }else{
                    textViewMsgMorning.setText("Good Morning!");
                    textViewMsgMorning.setTextColor(Color.DKGRAY);

                    ViewGroup viewGroup = (ViewGroup)findViewById(R.id.activity_main);
                    viewGroup.setBackgroundColor(Color.WHITE);
                }
            }
        });

        // Demonstrating how to play around with different listeners. In this case, a listener
        // for the SeekBar class. To learn about listeners for specific widgets, visit
        // the official API documentation. For SeekBar, that's:
        //   https://developer.android.com/reference/android/widget/SeekBar.html
        // And notice how it says at the top of the overview:
        //   "Clients of the SeekBar can attach a SeekBar.OnSeekBarChangeListener to be notified of the user's actions."
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarBgColor);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup viewGroup = (ViewGroup)findViewById(R.id.activity_main);
                //hue is value 0 - 360. Saturation and luminance are 0-1.
                float newBgColorHue = (progress / (float)seekBar.getMax()) * 360f;
                viewGroup.setBackgroundColor(ColorUtils.HSLToColor(new float[]{newBgColorHue, 0.5f, 0.5f}));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // This is just another SeekBar example that I created to further reinforce
        // how to use listeners. Remember, allow Android Studio to help you by using alt-enter (or
        // and clicking on the red lightbulb or red underlines as you write code (which will auto-generate code for you)
        SeekBar seekBarTestToastMsgs = (SeekBar)findViewById(R.id.seekBarTestToastMessages);
        seekBarTestToastMsgs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // not implemented
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // not implemented
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Context context = getApplicationContext();
                CharSequence text = "The seekbar value is " + seekBar.getProgress();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    /**
     * The method 'onClickToggleButton' is hooked up in the activity_main.xml file--see the line:
     *  android:onClick="onClickToggleHelloWorldButton"
     * Buttons are unique interactive widgets in Android in that you can hook up the event listener via
     * XML. See https://developer.android.com/reference/android/widget/Button.html
     * @param v
     */
    public void onClickToggleHelloWorldButton(View v) {
        TextView textViewMsg = (TextView)findViewById(R.id.textViewHelloWorldMessage);
        if(textViewMsg.getText().toString().startsWith("Hello")) {
            textViewMsg.setText("Goodbye World!");
        }else{
            textViewMsg.setText("Hello World!");
        }
    }

    /**
     * This method demonstrates the use of a simple AlertDialog to confirm that the user
     * actually wants to reset the background color. Read more about dialogs in Android
     * here: https://developer.android.com/guide/topics/ui/dialogs.html
     *
     * This method is hooked up in activity_main.xml
     *
     * @param v
     */
    public void onClickResetColor(View v){

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Reset color?")
                .setTitle("Do you want to reset the color?");

        // 3. Setup the 'positive' and 'negative' buttons along with their click listeners
        builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ViewGroup viewGroup = (ViewGroup)findViewById(R.id.activity_main);
                viewGroup.setBackgroundColor(Color.WHITE);
            }
        });

        builder.setNegativeButton("No Way!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    /**
     * Uses a more complicated AlertDialog with a custom XML. See:
     * https://developer.android.com/guide/topics/ui/dialogs.html#CustomLayout
     * 
     * @param v
     */
    public void onClickSetColor(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ViewGroup mainView = (ViewGroup)findViewById(R.id.activity_main);

        // Getting the background color of a View or ViewGroup is a bit weird.
        // See: http://stackoverflow.com/questions/14779259/get-background-color-of-a-layout
        Drawable background = mainView.getBackground();
        if (background instanceof ColorDrawable) {
            _bgColorSaved = ((ColorDrawable) background).getColor();
        }

        // Get the layout inflater. LayoutInflaters take a layout XML file and create its
        // corresponding View objects. Never create LayoutInflaters directly. Always use the
        // factory method getLayoutInflater. See https://developer.android.com/reference/android/view/LayoutInflater.html
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate the dialog_color.xml layout and create the View
        View dialogView = inflater.inflate(R.layout.dialog_color, null);

        // Get access to the seakbar on this dialog.
        SeekBar seekBar = (SeekBar)dialogView.findViewById(R.id.seekBarColorDialog);

        // Set progress bar to current hue value (in other words, when the user first sees
        // this seek bar, it's already set to the hue value of the background)
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(_bgColorSaved, hsl);
        int seekBarPosition = (int)((hsl[0] / 360) * (float)seekBar.getMax());
        seekBar.setProgress(seekBarPosition);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup mainView = (ViewGroup)findViewById(R.id.activity_main);
                //hue is value 0 - 360. Saturation and luminance are 0-1.
                float newBgColorHue = (progress / (float)seekBar.getMax()) * 360f;
                mainView.setBackgroundColor(ColorUtils.HSLToColor(new float[]{newBgColorHue, 0.5f, 0.5f}));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not implemented
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not implemented
            }
        });

        // This is the method that allows us to use our own custom view. We set the AlertDialog builder
        // to the view we created with the inflater above.
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewGroup mainView = (ViewGroup)findViewById(R.id.activity_main);
                        mainView.setBackgroundColor(_bgColorSaved);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
