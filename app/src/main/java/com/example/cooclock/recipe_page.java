package com.example.cooclock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.willy.ratingbar.BaseRatingBar;

import java.util.ArrayList;

public class recipe_page extends AppCompatActivity {
    static String TAG = "RECIPE";
    Recipe_step recipe_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        initializeRecipe();
        updateRecipeIngredientList();

        InitialRecipeStepList(); // 레시피 단계 나타내기
    }

    /*
    레시피 기본 정보 수정(작성자, 몇 인분, 제목, 좋아요 상태, 후기
     */
    public void initializeRecipe() {
        Log.d(TAG, "Aa");
        TextView recipe_writer = (TextView) findViewById(R.id.recipe_writer); // 작성자
        TextView recipe_servings = (TextView) findViewById(R.id.recipe_servings); // 기준 인원
        TextView recipe_title = (TextView) findViewById(R.id.recipe_title); // 제목
        BaseRatingBar is_favorite = (BaseRatingBar) findViewById(R.id.recipe_add_favorite); // 좋아요 버튼
        TextView rating_average = (TextView) findViewById(R.id.rating_average); // 평균 평점
        BaseRatingBar rating_average_star = (BaseRatingBar) findViewById(R.id.raring_bar_average); // 평균 평점 별표시
        TextView rating_cnt = (TextView) findViewById(R.id.rating_count); // 리뷰 개수
        LinearProgressIndicator five_point = (LinearProgressIndicator) findViewById(R.id.rating_5); // 5점
        LinearProgressIndicator four_point = (LinearProgressIndicator) findViewById(R.id.rating_4); // 4점
        LinearProgressIndicator three_point = (LinearProgressIndicator) findViewById(R.id.rating_3); // 3점
        LinearProgressIndicator two_point = (LinearProgressIndicator) findViewById(R.id.rating_2); // 2점
        LinearProgressIndicator one_point = (LinearProgressIndicator) findViewById(R.id.rating_1); // 1점

        recipe_init_item item = new recipe_init_item("이재원", "4", "둘이 먹다 하나 죽어 그냥 몰라", true, 3.6, 15, 5, 4, 3, 2, 1, 4);

        recipe_writer.setText(item.getRecipe_writer()); // 레시피 작성자 설정
        recipe_servings.setText(item.getRecipe_servings()); // 기준 인원 설정
        recipe_title.setText(item.getRecipe_title()); // 제목 설정
        // 좋아요 버튼 설정
        if (item.getFavorite())
            is_favorite.setRating(1);
        else
            is_favorite.setRating(0);
        rating_average.setText(String.valueOf(item.getRating_average())); // 평균 평점 설정
        rating_average_star.setRating((int) item.getRating_average()); // 평균 평점 별표시 설정
        rating_cnt.setText(String.valueOf(item.getRating_cnt())); // 리뷰 개수 설정

        int tot = item.getRating_cnt();
        int five = item.getFive_point();
        int four = item.getFour_point();
        int three = item.getThree_point();
        int two = item.getTwo_point();
        int one = item.getOne_point();

        five_point.setProgress((int) ((float) five / tot * 100)); // 5점 프로그래스바 설정
        four_point.setProgress((int) ((float) four / tot * 100)); // 4점 프로그래스바 설정
        three_point.setProgress((int) ((float) three / tot * 100)); // 3점 프로그래스바 설정
        two_point.setProgress((int) ((float) two / tot * 100)); // 2점 프로그래스바 설정
        one_point.setProgress((int) ((float) one / tot * 100)); // 1점 프로그래스바 설정
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
            Log.d(TAG, "ingredient_name: " + ingredient_name);
            Log.d(TAG, "ingredient_weight: " + ingredient_weight);
        }
    }

    // 레시피 재료 리스트를 관리할 adapter
    public static class RecipeIngredientListCustomAdapter extends RecyclerView.Adapter<recipe_page.RecipeIngredientListCustomViewHolder> {
        ArrayList<ingredientItem> items;

        public RecipeIngredientListCustomAdapter(ArrayList<ingredientItem> a_list) {
            items = a_list;
        }

        @NonNull
        @Override
        public recipe_page.RecipeIngredientListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_item, parent, false);
            return new recipe_page.RecipeIngredientListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recipe_page.RecipeIngredientListCustomViewHolder holder, int position) {
            ingredientItem item = items.get(position);
            holder.ingredient_name.setText(item.getName());
            holder.ingredient_weight.setText(item.getWeight());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 레시피 재료 리스트 업데이트 코드
    private void updateRecipeIngredientList() {
        RecyclerView IngredientList = findViewById(R.id.recipe_ingredient_list);

        ArrayList<ingredientItem> items = new ArrayList<ingredientItem>();

        items.add(new ingredientItem("당근", "0.1","과일-채소"));
        items.add(new ingredientItem("양파", "0.1","과일-채소"));
        items.add(new ingredientItem("애호박", "0.1","과일-채소"));
        items.add(new ingredientItem("만두", "0.1","기타"));
        items.add(new ingredientItem("소고기 국거리", "0.1","육류"));

        recipe_page.RecipeIngredientListCustomAdapter rlAdapter = new recipe_page.RecipeIngredientListCustomAdapter(items);
        IngredientList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        IngredientList.setLayoutManager(layoutManager);
    }


    /*
    요리 단계 리스트
    */

    public void InitialRecipeStepList() {

        ArrayList<recipeStepItem> items = new ArrayList<recipeStepItem>();
        items.add(new recipeStepItem(1, "달걀은 흰자, 노른자를 분리하고 약간의 소금을 넣어 풀고 대파는 어슷하게 썰어주세요.", 3, 0, R.drawable.recipe_img_test));
        items.add(new recipeStepItem(2, "달군 팬에 약간의 기름을 두르고 키친타월로 닦아 낸 후 달걀노른자와 흰자 순으로 약한 불에서 지단을 만들어 가늘게 채를 썰어주세요.", 3, 0, R.drawable.recipe_img_test));
        items.add(new recipeStepItem(3, "냄비에 육수를 넣고 끓으면 국물 양념 재료와 만두를 넣어주세요. 만두가 익어 떠오르면 대파를 넣고 한소끔 끓이고 마지막에 약간의 소금과 후춧가루를 넣어 간을 맞춰주세요.", 5, 0, R.drawable.recipe_img_test));
        items.add(new recipeStepItem(4, "그릇에 만두국을 담고 채썬 황백지단을 올려 맛있게 즐겨주세요. (tip 기호에 따라 고명으로 김가루 또는 양념해서 볶은 쇠고기, 송송 썬 실파 등을 곁들여주세요.", 1, 30, R.drawable.recipe_img_test));

        recipe_step = new Recipe_step(items);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recipe_step, recipe_step).commit();
    }



    /*
    파이어베이스에서 찾아서 가져오기
     */
    private void fetchFirebaseData(String title) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock").child("Recipe").child(title);  // DB테이블 연결
//        String databaseReferenceString = databaseReference.toString(); // DatabaseReference를 문자열로 변환


        // ingredient
        mDatabase.child("ingredient");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, String.valueOf(error.toException()));
            }
        });



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear(); // 기존 데이터 초기화
                Log.d("Children", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("SnapshotKey", snapshot.getKey()); // 해당 스냅샷의 키를 출력
                    Log.d("SnapshotValue", snapshot.getValue().toString()); // 해당 스냅샷의 값을 출력
                    recipeItem recipe = snapshot.getValue(recipeItem.class);
                    items.add(recipe); // Firebase에서 가져온 데이터를 ArrayList에 추가
                    Log.d("item", "item added");
                }
                adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알려 RecyclerView 갱신
                Log.d("TAG", "recent_recipe_page");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("recent_recipe_page", String.valueOf(error.toException()));
            }
        });
    }

    /*
    노하우 리스트
    // 노하우 리스트리사이클러 뷰를 위한 뷰 홀더
    public static class RecipeKnowHowListCustomViewHolder extends RecyclerView.ViewHolder {
        private TextView know_how_title;
        private ImageView know_how_image;

        public RecipeKnowHowListCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            know_how_title = itemView.findViewById(R.id.know_how_item_title);
            know_how_image = itemView.findViewById(R.id.know_how_item_image);
        }
    }

    // 노하우 리스트를 관리할 adapter
    public static class RecipeKnowHowListCustomAdapter extends RecyclerView.Adapter<recipe_page.RecipeKnowHowListCustomViewHolder> {
        ArrayList<knowHowItem> items;

        public RecipeKnowHowListCustomAdapter(ArrayList<knowHowItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public recipe_page.RecipeKnowHowListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_know_how_item, parent, false);
            return new recipe_page.RecipeKnowHowListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recipe_page.RecipeKnowHowListCustomViewHolder holder, int position) {
            knowHowItem item = items.get(position);
            holder.know_how_title.setText(item.getTitle());
            holder.know_how_image.setImageResource(item.getResId());
            holder.know_how_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeTitle = (String) holder.know_how_title.getText();
                    Intent recipeIntent = new Intent(v.getContext(), .class);
                    recipeIntent.putExtra("recipeTitle",recipeTitle);
                    v.getContext().startActivity(recipeIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 노하우 리스트 업데이트 코드
    private void updateRecipeKnowHowList(){
        RecyclerView KnowHowList = findViewById(R.id.know_how_list);

        ArrayList<knowHowItem> items = new ArrayList<knowHowItem>();
        items.add(new knowHowItem("달걀 지단 만드는 법",R.drawable.knowhow_test_img0));
        items.add(new knowHowItem("달걀 보관법" ,R.drawable.knowhow_test_img1));

        recipe_page.RecipeKnowHowListCustomAdapter rlAdapter = new recipe_page.RecipeKnowHowListCustomAdapter(items);
        KnowHowList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        KnowHowList.setLayoutManager(layoutManager);
    }
    */
}