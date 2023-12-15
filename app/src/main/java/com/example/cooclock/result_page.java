package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class result_page extends AppCompatActivity {

    public ArrayList<String> filterItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        //필터페이지에서 넘겨받음.
        Intent filterintent = getIntent();
        String te = filterintent.getStringExtra("test");
        Log.d("logcat",te);
        filterItem = filterintent.getStringArrayListExtra("filter");
        Log.d("logcat",filterItem.toString());

        //recycler view
        List<filterItemBtnModel> buttonList = new ArrayList<>();
        buttonList.add(new filterItemBtnModel("당근"));
        buttonList.add(new filterItemBtnModel("양파"));
        buttonList.add(new filterItemBtnModel("무"));
        buttonList.add(new filterItemBtnModel("감자"));
        buttonList.add(new filterItemBtnModel("멸치"));
        buttonList.add(new filterItemBtnModel("만두"));
        buttonList.add(new filterItemBtnModel("어묵"));

        RecyclerView recyclerView = findViewById(R.id.filter_item_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        filterItemButtonAdapter buttonAdapter = new filterItemButtonAdapter(buttonList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(buttonAdapter);

        // 레시피 리스트 뷰 배치
        updateRecommendedList();
    }

    // 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = findViewById(R.id.result_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));


        main_page.RecipeListCustomAdapter rlAdapter = new main_page.RecipeListCustomAdapter(items);
        recommendedList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }
}