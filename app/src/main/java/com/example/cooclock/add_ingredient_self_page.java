package com.example.cooclock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class add_ingredient_self_page extends AppCompatActivity {
    private static String TAG = "ADD_INGREDIENT";
    EditText ingredient_name;
    TextView ingredient_weight;
    TextView add_ingredient_btn;
    ImageView sub_ingredient_weight;
    ImageView add_ingredient_weight;
    Button[] buttons = new Button[7];
    int[] btnIds = {
            R.id.fruit, R.id.meat, R.id.fish, R.id.grain,
            R.id.egg, R.id.milk, R.id.etc
    };

    public ingredientItem ingredientItem = new ingredientItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_self_page);
        ingredient_name = (EditText)findViewById(R.id.input_ingredient_name);
        ingredient_weight = (TextView) findViewById(R.id.input_ingredient_weight);
        add_ingredient_btn = (TextView) findViewById(R.id.add_ingredient);
        sub_ingredient_weight = (ImageView)findViewById(R.id.ingredient_weight_sub);
        add_ingredient_weight = (ImageView) findViewById(R.id.ingredient_weight_add);
        for(int i=0; i<7; ++i){
            buttons[i] = (Button) findViewById(btnIds[i]);
            int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleButtonClick(finalI);
                }
            });

        }
        add_ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ""+ingredientItem.name + ingredientItem.weight+ingredientItem.kind);
                finish();
                // data 전달하는 코드 있어야함.
            }
        });

        sub_ingredient_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient_weight.setText(String.format("%.1f",Float.parseFloat(ingredient_weight.getText().toString())-0.1));
            }
        });

        add_ingredient_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient_weight.setText(String.format("%.1f",Float.parseFloat(ingredient_weight.getText().toString())+0.1));
            }
        });

    }

    private void handleButtonClick(int idx){
        for(int i=0; i<7; ++i){
            buttons[i].setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
            buttons[i].setEnabled(true);
        }
        buttons[idx].setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
        buttons[idx].setEnabled(false);

        ingredientItem.name = ingredient_name.getText().toString();
        ingredientItem.weight = ingredient_weight.getText().toString();
        ingredientItem.kind = buttons[idx].getText().toString();
    }
}