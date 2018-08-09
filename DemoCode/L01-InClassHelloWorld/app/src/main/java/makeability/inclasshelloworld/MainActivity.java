package makeability.inclasshelloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)this.findViewById(R.id.buttonToggle);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                TextView textView = (TextView)findViewById(R.id.textViewHello);
                if(textView.getText().toString().startsWith("Hello")){
                    textView.setText("Goodbye World!");
                }else{
                    textView.setText("Hello World!");
                }
            }
        });
    }
}
