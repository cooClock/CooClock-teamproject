package com.example.cooclock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        }
        else if(beforeIntentTitle.equals("home_page_category")){
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
        } else if(beforeIntentTitle.equals("home_page")) { // 추천 레시피
            Log.d("logcat", beforeIntentTitle);
            LinearLayout filterTitleLayout = (LinearLayout)findViewById(R.id.filter_item_layout);
            filterTitleLayout.setVisibility(View.GONE);

            // 레시피 리스트 뷰 배치
            updateRecommendedList();
        } else if(beforeIntentTitle.equals("profile_page_myRecipie")) { // 내가 올린 레시피
            Log.d("logcat", beforeIntentTitle);
            LinearLayout filterTitleLayout = (LinearLayout)findViewById(R.id.filter_item_layout);
            filterTitleLayout.setVisibility(View.GONE);

            // 레시피 리스트 뷰 배치
            updateRecommendedList();
        }  else if(beforeIntentTitle.equals("profile_page_RecentView")) { // 최근 본 레시피
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
        items.add(new recipeItem("젓갈", R.drawable.recipe_list_test2,40,200));

        BasicRecipeListAdapter rlAdapter = new BasicRecipeListAdapter(items);
        recommendedList.setAdapter(rlAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }
}