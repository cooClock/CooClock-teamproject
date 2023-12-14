package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class my_recipe_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_page);
//        updateMyRecipeList();
    }


    /*
    내가 올린 레시피
    */
    // 내가 올린 레시피 리스트리사이클러 뷰를 위한 뷰 홀더
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

    // 내가 올린 레시피 리스트를 관리할 adapter
    public static class RecipeListCustomAdapter extends RecyclerView.Adapter<my_recipe_page.RecipeListCustomViewHolder> {
        ArrayList<recipeItem> items;

        public RecipeListCustomAdapter(ArrayList<recipeItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public my_recipe_page.RecipeListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new my_recipe_page.RecipeListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull my_recipe_page.RecipeListCustomViewHolder holder, int position) {
            recipeItem item = items.get(position);
//            holder.recipe_image.setImageResource(item.getResId());
            Glide.with(holder.itemView).load(item.getResId()).into(holder.recipe_image);
            holder.recipe_title.setText(item.getTitle());
            holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
            holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        // 어댑터의 내용...
    }

    // 내가 올린 레시피 리스트 업데이트 코드
    private void updateMyRecipeList(){
        RecyclerView myRecipeList = findViewById(R.id.my_recipe_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = new ArrayList<recipeItem>();
//        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
//        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));
//        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
//        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));
//        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
//        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));
//        items.add(new recipeItem("멸치 볶음", R.drawable.recipe_list_test2,20,100));
//        items.add(new recipeItem("된장 찌개", R.drawable.recipe_list_test1,30,500));

        my_recipe_page.RecipeListCustomAdapter rlAdapter = new my_recipe_page.RecipeListCustomAdapter(items);
        myRecipeList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        myRecipeList.setLayoutManager(layoutManager);
    }

}