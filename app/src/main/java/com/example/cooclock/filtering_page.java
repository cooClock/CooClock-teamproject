package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class filtering_page extends AppCompatActivity {
    private Button cookTime1Btn;
    private Button cookTime2Btn;
    private Button cookTime3Btn;

    private boolean isButton1On = false;
    private boolean isButton2On = false;
    private boolean isButton3On = false;

    private EditText filterSearch;
    private int editTextItemCount=0;

    public ArrayList<String> filterItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering_page);

        // Assuming your EditText has an id of filter_search
        filterSearch = findViewById(R.id.filter_search);

        // Assuming your ImageView has an id of filter_search_icon
        ImageView filterSearchIcon = findViewById(R.id.filter_search_icon);

        // Set a click listener for the ImageView
        filterSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String searchText = filterSearch.getText().toString();

                // Check if the EditText has text
                if (!searchText.isEmpty()) {
                    // Log the text
                    Log.d("logcat", searchText);

                    //filterItem에 필터값 추가
                    filterItem.add(new String(searchText.toString()));
                    editTextItemCount++;

                    //create a new button
                    Button dynamicButton = new Button(filtering_page.this);
                    dynamicButton.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    // Set margins
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(15, 0, 15, 15); // left, top, right, bottom
                    dynamicButton.setLayoutParams(params);

                    // Round the corners using a GradientDrawable
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius(80); // Adjust the radius as needed
                    gradientDrawable.setColor(ContextCompat.getColor(filtering_page.this, R.color.buttonOnColor)); // Replace with your actual color
                    // Set the background of the Button
                    dynamicButton.setBackground(gradientDrawable);


                    dynamicButton.setPadding(5,0,5,0);
                    dynamicButton.setText(searchText);
                    dynamicButton.setTextSize(13); // textSize in sp
                    dynamicButton.setTypeface(ResourcesCompat.getFont(filtering_page.this, R.font.notosanskr_medium));
                    dynamicButton.setIncludeFontPadding(false);
                    dynamicButton.setTextColor(ContextCompat.getColor(filtering_page.this, R.color.btn_text));
                    dynamicButton.setTextColor(getResources().getColor(R.color.btn_text));


                    //add the button to your layout
                    if(editTextItemCount%3==1) {
                        LinearLayout addItemBtnLayout = findViewById(R.id.addfilterItem_btn_layout_1);
                        addItemBtnLayout.addView(dynamicButton);
                    }else if(editTextItemCount%3==2) {
                        LinearLayout addItemBtnLayout = findViewById(R.id.addfilterItem_btn_layout_2);
                        addItemBtnLayout.addView(dynamicButton);
                    }else{
                        LinearLayout addItemBtnLayout = findViewById(R.id.addfilterItem_btn_layout_3);
                        addItemBtnLayout.addView(dynamicButton);
                    }


                    // Clear the EditText
                    filterSearch.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


        //recycler view
        List<ingredientBtnModel> buttonList = new ArrayList<>();
        buttonList.add(new ingredientBtnModel("당근"));
        buttonList.add(new ingredientBtnModel("양파"));
        buttonList.add(new ingredientBtnModel("무"));
        buttonList.add(new ingredientBtnModel("감자"));
        buttonList.add(new ingredientBtnModel("멸치"));
        buttonList.add(new ingredientBtnModel("만두"));
        buttonList.add(new ingredientBtnModel("어묵"));
        buttonList.add(new ingredientBtnModel("조미김"));
        buttonList.add(new ingredientBtnModel("김치"));
        buttonList.add(new ingredientBtnModel("계란"));
        buttonList.add(new ingredientBtnModel("양상추"));
        buttonList.add(new ingredientBtnModel("파프리카"));
        buttonList.add(new ingredientBtnModel("오이"));
        buttonList.add(new ingredientBtnModel("소고기 국거리"));
        buttonList.add(new ingredientBtnModel("삼겹살"));

        RecyclerView recyclerView = findViewById(R.id.ingredient_btn_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        IngredientButtonAdapter buttonAdapter = new IngredientButtonAdapter(buttonList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(buttonAdapter);
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
            Log.d("logcat",   cookTime1Btn.getText().toString());
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

            //log 찍기
            Log.d("logcat", String.valueOf(isButtonOn)+clickedBtn.getText().toString());

            //filterItem에 필터값 추가
            filterItem.add(new String(clickedBtn.getText().toString()));
            clickedBtn.setTag(false);
        } else {
            // Button is already on, set it to off
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));

            //filterItem에 필터값 삭제
            if (filterItem.contains(clickedBtn.getText().toString())){
                filterItem.remove(clickedBtn.getText().toString());
            }
            clickedBtn.setTag(true);
        }
    }

    /*
    complete btn
    */
    //초기화 버튼 클릭시
    public void filterSelectInitial(View view){
        cookTime1Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime2Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime3Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        isButton1On = false;
        isButton2On = false;
        isButton3On = false;

        filterItem = new ArrayList<>();
        Toast.makeText(getApplicationContext(), "필터가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }
    //필터 선택 완료 버튼 클릭시
    public void filterSelectCompleteClicked(View view){
//        Button clickedBtn = (Button) view;
        if(isButton1On==true && !filterItem.contains(cookTime1Btn.getText().toString())){ //filterItem에 필터값 추가
            filterItem.add(new String(cookTime1Btn.getText().toString()));
        } else{ //filterItem에 필터값 삭제
            filterItem.remove(cookTime1Btn.getText().toString());
        }
        if(isButton2On==true && !filterItem.contains(cookTime2Btn.getText().toString())){ //filterItem에 필터값 추가
            filterItem.add(new String(cookTime2Btn.getText().toString()));
        } else {//filterItem에 필터값 삭제
            filterItem.remove(cookTime2Btn.getText().toString());
        }
        if(isButton3On==true && !filterItem.contains(cookTime3Btn.getText().toString())){ //filterItem에 필터값 추가
            filterItem.add(new String(cookTime3Btn.getText().toString()));
        } else{ //filterItem에 필터값 삭제
            filterItem.remove(cookTime3Btn.getText().toString());
        }

        if(filterItem.isEmpty()){
            Toast.makeText(getApplicationContext(), "선택된 필터가 없습니다.", Toast.LENGTH_SHORT).show();
            Log.d("logcat", filterItem.toString());
        }else {
            Log.d("logcat", filterItem.toString());

            // activity 전환 + filterItem 내용 전달
            Intent resultIntent = new Intent(this, result_page.class);
            resultIntent.putExtra("intentTitle","filtering_page");
            resultIntent.putExtra("titleText","레시피를 확인해보세요.");
            resultIntent.putStringArrayListExtra("filter",filterItem);
            startActivity(resultIntent);

            // Terminate the current activity
            finish();
        }
    }
}