package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class filtering_page extends AppCompatActivity {
    private Button cookTime1Btn;
    private Button cookTime2Btn;
    private Button cookTime3Btn;

    private boolean isButton1On = false;
    private boolean isButton2On = false;
    private boolean isButton3On = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering_page);

        cookTime1Btn = findViewById(R.id.cookTime1);
        cookTime2Btn = findViewById(R.id.cookTime2);
        cookTime3Btn = findViewById(R.id.cookTime3);

        View.OnClickListener commonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(v);
            }
        };

        // Set the common click listener for each button
        cookTime1Btn.setOnClickListener(commonClickListener);
        cookTime2Btn.setOnClickListener(commonClickListener);
        cookTime3Btn.setOnClickListener(commonClickListener);

    }

    public void handleButtonClick(View view) {
        Button clickedButton = (Button) view;

        // Reset all buttons to off state
        cookTime1Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime2Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime3Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));

        // Determine which button was clicked
        switch (clickedButton.getId()) {
            case R.id.cookTime1:
                isButton1On = !isButton1On;
                isButton2On = false;
                isButton3On = false;
                break;
            case R.id.cookTime2:
                isButton2On = !isButton2On;
                isButton1On = false;
                isButton3On = false;
                break;
            case R.id.cookTime3:
                isButton3On = !isButton3On;
                isButton1On = false;
                isButton2On = false;
                break;
        }

        // Set the background color based on the button state
        if (isButton1On) {
            cookTime1Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime1Btn.getText().toString());
        }
        if (isButton2On) {
            cookTime2Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime2Btn.getText().toString());
        }
        if (isButton3On) {
            cookTime3Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime3Btn.getText().toString());
        }
    }

    public void btnClicked(View view){
        Button clickedBtn = (Button) view;

        // Check if the button has a tag indicating its state
        Boolean isButtonOn = (Boolean) clickedBtn.getTag();

        // Toggle the button state
        if (isButtonOn == null || isButtonOn) {
            // Button is off, set it to on
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", String.valueOf(isButtonOn)+clickedBtn.getText().toString());
            clickedBtn.setTag(false);
        } else {
            // Button is already on, set it to off
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
            clickedBtn.setTag(true);
        }
    }
}