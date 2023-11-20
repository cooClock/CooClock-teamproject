package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class recipe_write_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write_page);


        LinearLayout get_picture = findViewById(R.id.input_picture);
        get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("test","picture");
            }
        });
    }


}