package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class my_refrigerator_page extends Fragment {
    View MyRefrigeratorView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        MyRefrigeratorView = inflater.inflate(R.layout.activity_my_refrigerator_page,container,false);
        TextView go_add_self = MyRefrigeratorView.findViewById(R.id.go_add_ingredient_self_page);
        TextView go_photo = MyRefrigeratorView.findViewById(R.id.go_photo_scan_page);
        initializeIngredients();

        go_add_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), add_ingredient_self_page.class);
                startActivity(intent);
            }
        });

        go_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), photo_scan_page.class);
                startActivity(intent);
            }
        });

        return MyRefrigeratorView;
    }


    // 재료 카테고리 업데이트 코드
    private void initializeIngredients(){
        RecyclerView myIngredientList = MyRefrigeratorView.findViewById(R.id.my_refrigerator_ingredient_list);

        ArrayList<ingredientItem> items = new ArrayList<ingredientItem>();
        items.add(new ingredientItem("당근", "0.1","과일-채소"));
        items.add(new ingredientItem("양파", "0.1","과일-채소"));
        items.add(new ingredientItem("애호박", "0.1","과일-채소"));
        items.add(new ingredientItem("만두", "0.1","기타"));
        items.add(new ingredientItem("소고기 국거리", "0.1","육류"));

        IngredientListAdapter rAdapter = new IngredientListAdapter(items);
        myIngredientList.setAdapter(rAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        myIngredientList.setLayoutManager(layoutManager);
    }
}