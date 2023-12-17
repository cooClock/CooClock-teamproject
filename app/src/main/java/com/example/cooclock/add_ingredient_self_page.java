package com.example.cooclock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class add_ingredient_self_page extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<ingredientItem> items;
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

    public ingredientItem ing_items = new ingredientItem();

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
                fetchFirebaseData(ing_items);
                finish();
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

        ing_items.name = ingredient_name.getText().toString();
        ing_items.weight = ingredient_weight.getText().toString();
        ing_items.kind = buttons[idx].getText().toString();
    }


    private void fetchFirebaseData(ingredientItem ing_items) {
        database = FirebaseDatabase.getInstance(); // firebase 연동
        databaseReference = database.getReference("cooclock").child("ingredientItem");  // DB테이블 연결
        String databaseReferenceString = databaseReference.toString(); // DatabaseReference를 문자열로 변환

        Log.d("logcat", databaseReferenceString); // 디버그 로그에 DatabaseReference 출력
        // Firebase에 새 데이터 추가 (push() 메서드를 사용하여 자동으로 고유한 ID 부여)
        DatabaseReference newIngredientRef = databaseReference.push();
        newIngredientRef.setValue(ing_items);
    }
}