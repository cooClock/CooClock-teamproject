package com.example.cooclock;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class home_page extends Fragment {
    View rootView;
    private static String TAG = "HOME_PAGE";
    public ArrayList<String> filterItem = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.activity_home_page,container,false);
        // 카테고리 선택 버튼(라디오 그룹)
        MaterialButtonToggleGroup categorySelect = rootView.findViewById(R.id.category_select);
        Button foodType = rootView.findViewById(R.id.food_type);
        Button foodSituation = rootView.findViewById(R.id.food_situation);

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

        LinearLayout goto_filter = rootView.findViewById(R.id.goto_filtering_page);
        goto_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), filtering_page.class);
                startActivity(intent);
            }
        });

        LinearLayout goto_result_page = rootView.findViewById(R.id.goto_result_page);
        goto_result_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(requireContext(), result_page.class);
                resultIntent.putExtra("intentTitle","home_page");
                resultIntent.putExtra("titleText","오늘의 추천 레시피입니다.");
                startActivity(resultIntent);
            }
        });

//        ImageView goto_my_refrigerator = rootView.findViewById(R.id.go_my_refrigerator);
//        goto_my_refrigerator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return rootView;
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
    public class SubCategoryCustomAdapter extends RecyclerView.Adapter<home_page.SubCategoryCustomViewHolder> {
        ArrayList<subCategroyItem> items;

        public SubCategoryCustomAdapter(ArrayList<subCategroyItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.SubCategoryCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategroy_item, parent, false);
            return new home_page.SubCategoryCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.SubCategoryCustomViewHolder holder, int position) {
            subCategroyItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResId());
            holder.category_name.setText(item.getSubCategoryName());
            holder.category_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String subCategoryName = (String) holder.category_name.getText();
                    filterItem.add(subCategoryName);
                    Intent resultIntent = new Intent(requireContext(), result_page.class);
                    resultIntent.putExtra("intentTitle","home_page_category");
                    resultIntent.putExtra("titleText",subCategoryName+"를 확인해보세요.");
                    resultIntent.putStringArrayListExtra("filter", filterItem);
                    startActivity(resultIntent);
                    filterItem.clear();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 세부 카테고리 업데이트 코드
    public void updateSubCategories(){
        RecyclerView subCategory = rootView.findViewById(R.id.subCategory);

        ArrayList<subCategroyItem> items = new ArrayList<subCategroyItem>();
        MaterialButtonToggleGroup categoryToggleGroup = rootView.findViewById(R.id.category_select);
        int selectedMainCategoryId = categoryToggleGroup.getCheckedButtonId(); // 어떤 것이 눌려져있는지 확인
        if (selectedMainCategoryId != R.id.food_situation){
            Button btn = rootView.findViewById(R.id.food_type);
            Button btn2 = rootView.findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pc_yello)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
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
            Button btn = rootView.findViewById(R.id.food_type);
            Button btn2 = rootView.findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pc_yello)));

            items.add(new subCategroyItem("소주상",R.drawable.food_situation_soju));
            items.add(new subCategroyItem("맥주상",R.drawable.food_situation_beer));
            items.add(new subCategroyItem("대접용",R.drawable.food_situation_hospitality));
            items.add(new subCategroyItem("데이트",R.drawable.food_situation_date));
            items.add(new subCategroyItem("보양식",R.drawable.food_situation_health));
            items.add(new subCategroyItem("도시락",R.drawable.food_situation_lunchbox));
            items.add(new subCategroyItem("아이용",R.drawable.food_situation_child));
            items.add(new subCategroyItem("이유식",R.drawable.food_situation_baby));
        }

        home_page.SubCategoryCustomAdapter scAdapter = new home_page.SubCategoryCustomAdapter(items);
        subCategory.setAdapter(scAdapter);

        // fix to grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
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
    public class RecipeListCustomAdapter extends RecyclerView.Adapter<home_page.RecipeListCustomViewHolder> {
        ArrayList<recipeItem> items;

        public RecipeListCustomAdapter(ArrayList<recipeItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.RecipeListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new home_page.RecipeListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.RecipeListCustomViewHolder holder, int position) {
            recipeItem item = items.get(position);
            holder.recipe_image.setImageResource(item.getResId());
//            Glide.with(holder.itemView).load(item.getResId()).into(holder.recipe_image);
            holder.recipe_title.setText(item.getTitle());
            holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
            holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
            holder.recipe_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeTitle = (String) holder.recipe_title.getText();
                    Intent recipeIntent = new Intent(requireContext(), recipe_page.class);
                    recipeIntent.putExtra("recipeTitle",recipeTitle);
                    startActivity(recipeIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }

    // 추천 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = rootView.findViewById(R.id.recommend_list);

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));


        home_page.RecipeListCustomAdapter rlAdapter = new home_page.RecipeListCustomAdapter(items);
        recommendedList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
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
    public static class CustomAdapter extends RecyclerView.Adapter<home_page.CustomViewHolder> {
        ArrayList<myRefrigeratorCategoryItem> items;

        public CustomAdapter(ArrayList<myRefrigeratorCategoryItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_refrigerator_category_item, parent, false);
            return new home_page.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.CustomViewHolder holder, int position) {
            myRefrigeratorCategoryItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResID());
            holder.category_name.setText(item.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 재료 카테고리 업데이트 코드
    private void updateMyRefrigeratorCategoryList(){
        RecyclerView myRefrigeratorCategory = rootView.findViewById(R.id.my_refrigerator_category);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<myRefrigeratorCategoryItem> items = new ArrayList<myRefrigeratorCategoryItem>();

        items.add(new myRefrigeratorCategoryItem("과일-채소",R.drawable.refrigerator_category_fruit));
        items.add(new myRefrigeratorCategoryItem("육류",R.drawable.refrigerator_category_meat));
        items.add(new myRefrigeratorCategoryItem("어패류",R.drawable.refrigerator_fishs));
        items.add(new myRefrigeratorCategoryItem("곡류",R.drawable.refrigerator_grains));
        items.add(new myRefrigeratorCategoryItem("난류",R.drawable.refrigerator_eggs));
        items.add(new myRefrigeratorCategoryItem("유제품",R.drawable.refrigerator_dairys));
        items.add(new myRefrigeratorCategoryItem("기타",R.drawable.refrigerator_etc));

        home_page.CustomAdapter rAdapter = new home_page.CustomAdapter(items);
        myRefrigeratorCategory.setAdapter(rAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        myRefrigeratorCategory.setLayoutManager(layoutManager);
    }

}