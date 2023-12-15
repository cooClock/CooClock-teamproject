package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class setting_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        TextView nickname = (TextView) findViewById(R.id.setting_change_nickname);
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_change_nickname");
            }
        });

        TextView email = (TextView) findViewById(R.id.setting_change_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_change_email");
            }
        });

        TextView password = (TextView) findViewById(R.id.setting_change_password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_change_password");
            }
        });

        TextView delete_user = (TextView) findViewById(R.id.setting_delete_user);
        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_delete_user");
            }
        });

    }
}