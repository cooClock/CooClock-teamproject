package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class result_page extends AppCompatActivity {

    public ArrayList<String> filterItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        // Received from the filter page.
        Intent beforeIntent = getIntent();
        String beforeIntentTitle = beforeIntent.getStringExtra("intentTitle");
        String titleText = beforeIntent.getStringExtra("titleText");
        TextView topTitle = (TextView) findViewById(R.id.topTitleText);
        topTitle.setText(titleText);

        if(beforeIntentTitle.equals("filtering_page")){
            filterItem = beforeIntent.getStringArrayListExtra("filter");
            if (filterItem != null) {
                String intentTitle = beforeIntent.getStringExtra("intentTitle");
                Log.d("logcat",intentTitle);
                Log.d("logcat",filterItem.toString());

                //recycler view
                List<filterItemBtnModel> buttonList = new ArrayList<>();
                // Iterate through filterItem and add each string to buttonList
                if (filterItem != null) {
                    for (String item : filterItem) {
                        buttonList.add(new filterItemBtnModel(item));
                    }
                }

                RecyclerView recyclerView = findViewById(R.id.filter_item_recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

                filterItemButtonAdapter buttonAdapter = new filterItemButtonAdapter(buttonList);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(buttonAdapter);

                // 레시피 리스트 뷰 배치
                updateRecommendedList();

            } else {
                Log.e("logcat", "filterItem is null");
                // Handle the case where filterItem is null
            }
        } else if(beforeIntentTitle.equals("home_page")) { //filtering_page에서 넘어오지 않은 경우.
            Log.d("logcat", beforeIntentTitle);
            LinearLayout filterTitleLayout = (LinearLayout)findViewById(R.id.filter_item_layout);
            filterTitleLayout.setVisibility(View.GONE);

            // 레시피 리스트 뷰 배치
            updateRecommendedList();
        } else {
            Log.e("logcat", "Exception while processing Intent data");
        }

//        // 레시피 리스트 뷰 배치
//        updateRecommendedList();
    }

    // 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = findViewById(R.id.result_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));
//        items.add(new recipeItem("젓갈", R.drawable.recipe_list_test1,40,200));

        main_page.RecipeListCustomAdapter rlAdapter = new main_page.RecipeListCustomAdapter(items);
        recommendedList.setAdapter(rlAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }
}