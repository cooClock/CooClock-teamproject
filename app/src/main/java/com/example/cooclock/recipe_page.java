package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.willy.ratingbar.BaseRatingBar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class recipe_page extends AppCompatActivity {
    String TAG = "RECIPE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        initializeRecipe();
//        updateIngredientList();
    }

    /*
    레시피 기본 정보 수정(작성자, 몇 인분, 제목, 좋아요 상태, 후기
     */
    public void initializeRecipe(){
        Log.d(TAG,"Aa");
        TextView recipe_writer = (TextView) findViewById(R.id.recipe_writer); // 작성자
        TextView recipe_servings = (TextView) findViewById(R.id.recipe_servings); // 기준 인원
        TextView recipe_title = (TextView) findViewById(R.id.recipe_title); // 제목
        BaseRatingBar is_favorite = (BaseRatingBar) findViewById(R.id.recipe_add_favorite); // 좋아요 버튼
        TextView rating_average = (TextView) findViewById(R.id.rating_average); // 평균 평점
        BaseRatingBar rating_average_star = (BaseRatingBar) findViewById(R.id.raring_bar_average); // 평균 평점 별표시
        TextView rating_cnt = (TextView) findViewById(R.id.rating_count); // 리뷰 개수
        ProgressBar five_point = (ProgressBar) findViewById(R.id.rating_5); // 5점
        ProgressBar four_point = (ProgressBar) findViewById(R.id.rating_4); // 4점
        ProgressBar three_point = (ProgressBar) findViewById(R.id.rating_3); // 3점
        ProgressBar two_point = (ProgressBar) findViewById(R.id.rating_2); // 2점
        ProgressBar one_point = (ProgressBar) findViewById(R.id.rating_1); // 1점

        recipe_init_item item = new recipe_init_item("이재원","4","둘이 먹다 하나 죽어 그냥 몰라", true,3.6,15,5,4,3,2,1);

        recipe_writer.setText(item.getRecipe_writer()); // 작성자 설정
        recipe_servings.setText(item.getRecipe_servings()); // 기준 인원 설정
        recipe_title.setText(item.getRecipe_title()); // 제목 설정
        // 좋아요 버튼 설정
        if (item.favorite)
            is_favorite.setRating(1);
        else
            is_favorite.setRating(0);
        rating_average.setText(String.valueOf(item.getRating_average())); // 평균 평점 설정
        rating_average_star.setRating((int) item.getRating_average()); // 평균 평점 별표시 설정
        rating_cnt.setText(String.valueOf(item.getRating_cnt())); // 리뷰 개수 설정
        five_point.setProgress((int)((item.getFive_point()/item.getRating_cnt())*100)); // 5점 프로그래스바 설정
        four_point.setProgress((int)((item.getFour_point()/item.getRating_cnt())*100)); // 4점 프로그래스바 설정
        three_point.setProgress((int)((item.getThree_point()/item.getRating_cnt())*100)); // 3점 프로그래스바 설정
        two_point.setProgress((int)((item.getTwo_point()/item.getRating_cnt())*100)); // 2점 프로그래스바 설정
        one_point.setProgress((int)((item.getOne_point()/item.getRating_cnt())*100)); // 1점 프로그래스바 설정
    }


    /*
    레시피 재료 리스트
    */
    // 레시피 재료 리스트리사이클러 뷰를 위한 뷰 홀더
    public static class RecipeIngredientListCustomViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredient_name;
        private TextView ingredient_weight;

        public RecipeIngredientListCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.recipe_ingredient_name);
            ingredient_weight = itemView.findViewById(R.id.recipe_ingredient_weight);
        }
    }

    // 레시피 재료 리스트를 관리할 adapter
    public static class RecipeIngredientListCustomAdapter extends RecyclerView.Adapter<recipe_page.RecipeIngredientListCustomViewHolder> {
        ArrayList<ingredientItem> items;

        public RecipeIngredientListCustomAdapter(ArrayList<ingredientItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public recipe_page.RecipeIngredientListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new recipe_page.RecipeIngredientListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recipe_page.RecipeIngredientListCustomViewHolder holder, int position) {
            ingredientItem item = items.get(position);
            holder.ingredient_name.setText(String.valueOf(item.getName()));
            holder.ingredient_weight.setText(String.valueOf(item.getWeight()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 레시피 재료 리스트 업데이트 코드
    private void updateIngredientList(){
        RecyclerView IngredientList = findViewById(R.id.recipe_ingredient_list);

        ArrayList<ingredientItem> items = new ArrayList<ingredientItem>();
        items.add(new ingredientItem("", ""));


        recipe_page.RecipeIngredientListCustomAdapter rlAdapter = new recipe_page.RecipeIngredientListCustomAdapter(items);
        IngredientList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        IngredientList.setLayoutManager(layoutManager);
    }

}