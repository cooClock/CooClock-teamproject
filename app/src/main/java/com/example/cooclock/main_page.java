package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class main_page extends AppCompatActivity {
    private String TAG = "MAIN_PAGE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        // 카테고리 선택 버튼(라디오 그룹)
        MaterialButtonToggleGroup categorySelect = findViewById(R.id.category_select);
        Button foodType = findViewById(R.id.food_type);
        Button foodSituation = findViewById(R.id.food_situation);
        Log.d(TAG,"asdaf");
        // 그리드 뷰 배치
        updateSubCategories();
        // 리스트 뷰 배치
        updateRecommendedList();
        // 리사이클러뷰 배치
        updateMyRefrigeratorCategoryList();

        categorySelect.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                updateSubCategories();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FrameLayout, new home_page())
                .commit();
    }



    /*
    세부 카테고리
     */
    // 세부 카테고리 리사이클러 뷰를 위한 뷰 홀더
    public static class SubCategoryCustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView category_icon;
        private TextView category_name;
        public SubCategoryCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            category_icon = itemView.findViewById(R.id.subCategory_icon);
            category_name = itemView.findViewById(R.id.subCategory_name);
        }
    }

    // 세부 카테고리 관리할 adapter
    public static class SubCategoryCustomAdapter extends RecyclerView.Adapter<SubCategoryCustomViewHolder> {
        ArrayList<subCategroyItem> items;

        public SubCategoryCustomAdapter(ArrayList<subCategroyItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public SubCategoryCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategroy_item, parent, false);
            return new SubCategoryCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubCategoryCustomViewHolder holder, int position) {
            subCategroyItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResId());
            holder.category_name.setText(item.getSubCategoryName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        // 어댑터의 내용...
    }

    // 세부 카테고리 업데이트 코드
    public void updateSubCategories(){
        RecyclerView subCategory = findViewById(R.id.subCategory);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<subCategroyItem> items = new ArrayList<subCategroyItem>();
        MaterialButtonToggleGroup categoryToggleGroup = findViewById(R.id.category_select);
        int selectedMainCategoryId = categoryToggleGroup.getCheckedButtonId(); // 어떤 것이 눌려져있는지 확인
        if (selectedMainCategoryId != R.id.food_situation){
            Button btn = findViewById(R.id.food_type);
            Button btn2 = findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pc_yello)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            items.add(new subCategroyItem("한식",R.drawable.food_type_korea));
            items.add(new subCategroyItem("일식",R.drawable.food_type_japan));
            items.add(new subCategroyItem("중식",R.drawable.food_type_china));
            items.add(new subCategroyItem("양식",R.drawable.food_type_western));
            items.add(new subCategroyItem("분식",R.drawable.food_type_flour));
            items.add(new subCategroyItem("간식",R.drawable.food_type_snack));
            items.add(new subCategroyItem("면류",R.drawable.food_type_noodle));
            items.add(new subCategroyItem("반찬류",R.drawable.food_type_side));
        }
        else{
            Button btn = findViewById(R.id.food_type);
            Button btn2 = findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pc_yello)));

            items.add(new subCategroyItem("소주상",R.drawable.food_situation_soju));
            items.add(new subCategroyItem("맥주상",R.drawable.food_situation_beer));
            items.add(new subCategroyItem("대접용",R.drawable.food_situation_hospitality));
            items.add(new subCategroyItem("데이트",R.drawable.food_situation_date));
            items.add(new subCategroyItem("보양식",R.drawable.food_situation_health));
            items.add(new subCategroyItem("도시락",R.drawable.food_situation_lunchbox));
            items.add(new subCategroyItem("아이용",R.drawable.food_situation_child));
            items.add(new subCategroyItem("이유식",R.drawable.food_situation_baby));
        }

        SubCategoryCustomAdapter scAdapter = new SubCategoryCustomAdapter(items);
        subCategory.setAdapter(scAdapter);


        // fix to grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        subCategory.setLayoutManager(layoutManager);
    }


    /*
    추천 레시피 리스트
    */
    // 추천 레시피 리스트리사이클러 뷰를 위한 뷰 홀더
    public static class RecipeListCustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipe_image;
        private TextView recipe_title;
        private TextView recipe_time;
        private TextView recipe_like;

        public RecipeListCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_image = itemView.findViewById(R.id.recipe_item_image);
            recipe_title = itemView.findViewById(R.id.recipe_item_title);
            recipe_time = itemView.findViewById(R.id.recipe_item_time);
            recipe_like = itemView.findViewById(R.id.recipe_item_favorite);
        }
    }

    // 추천 레시피 리스트를 관리할 adapter
    public static class RecipeListCustomAdapter extends RecyclerView.Adapter<RecipeListCustomViewHolder> {
        ArrayList<recipeItem> items;

        public RecipeListCustomAdapter(ArrayList<recipeItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public RecipeListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new RecipeListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeListCustomViewHolder holder, int position) {
            recipeItem item = items.get(position);
            holder.recipe_image.setImageResource(item.getResId());
            holder.recipe_title.setText(item.getTitle());
            holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
            holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        // 어댑터의 내용...
    }

    // 추천 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = findViewById(R.id.recommend_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));


        RecipeListCustomAdapter rlAdapter = new RecipeListCustomAdapter(items);
        recommendedList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }


    /*
    나의 냉장고 재료 카테고리
     */
    // 재료 카테고리 리사이클러 뷰를 위한 뷰 홀더
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView category_icon;
        private TextView category_name;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            category_icon = itemView.findViewById(R.id.my_refrigerator_category_icon);
            category_name = itemView.findViewById(R.id.my_refrigerator_category_name);
        }
    }

    // 재료 카테고리 관리할 adapter
    public static class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        ArrayList<myRefrigeratorCategoryItem> items;

        public CustomAdapter(ArrayList<myRefrigeratorCategoryItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_refrigerator_category_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            myRefrigeratorCategoryItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResID());
            holder.category_name.setText(item.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        // 어댑터의 내용...
    }

    // 재료 카테고리 업데이트 코드
    private void updateMyRefrigeratorCategoryList(){
        RecyclerView myRefrigeratorCategory = findViewById(R.id.my_refrigerator_category);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<myRefrigeratorCategoryItem> items = new ArrayList<myRefrigeratorCategoryItem>();

        items.add(new myRefrigeratorCategoryItem("과일-채소",R.drawable.refrigerator_category_fruit));
        items.add(new myRefrigeratorCategoryItem("육류",R.drawable.refrigerator_category_meat));
        items.add(new myRefrigeratorCategoryItem("어패류",R.drawable.refrigerator_fishs));
        items.add(new myRefrigeratorCategoryItem("곡류",R.drawable.refrigerator_grains));
        items.add(new myRefrigeratorCategoryItem("난류",R.drawable.refrigerator_eggs));
        items.add(new myRefrigeratorCategoryItem("유제품",R.drawable.refrigerator_dairys));
        items.add(new myRefrigeratorCategoryItem("기타",R.drawable.refrigerator_etc));

        CustomAdapter rAdapter = new CustomAdapter(items);
        myRefrigeratorCategory.setAdapter(rAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        myRefrigeratorCategory.setLayoutManager(layoutManager);
    }

}

