package com.example.cooclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class favorite_recipe_page extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.activity_favorite_recipe_page,container,false);

        // 리스트 뷰 배치
        updateFavoriteList();

        //return inflater.inflate(R.layout.activity_favorite_recipe_page,container,false);
        return rootView;
    }

    // 추천 레시피 리스트 업데이트 코드
    private void updateFavoriteList(){
        RecyclerView recommendedList = rootView.findViewById(R.id.favorite_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));


        BasicRecipeListAdapter rlAdapter = new BasicRecipeListAdapter(items);
        recommendedList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }
}