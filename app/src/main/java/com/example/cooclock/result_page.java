package com.example.cooclock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class result_page extends AppCompatActivity {
    private static String TAG = "RESULT";
    public ArrayList<String> filterItem = new ArrayList<>();
    public ArrayList<String> filterTimeItem = new ArrayList<>();
    public ArrayList<String> filterCategoryItem = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    ArrayList<recipeItem> recipeList =  new ArrayList<recipeItem>();


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
            filterTimeItem = beforeIntent.getStringArrayListExtra("filter_time");
            if (filterTimeItem != null || filterItem != null) {
                String intentTitle = beforeIntent.getStringExtra("intentTitle");

                //recycler view
                List<filterItemBtnModel> buttonList = new ArrayList<>();
                // Iterate through filterItem and add each string to buttonList
                if (filterItem != null) {
                    for (String item : filterItem) {
                        buttonList.add(new filterItemBtnModel(item));
                    }
                }
                if (filterTimeItem != null && !filterTimeItem.isEmpty()){
                    buttonList.add(new filterItemBtnModel(filterTimeItem.get(0)));
                    String s = filterTimeItem.get(0);
                    filterTimeItem.remove(0);
                    switch (s) {
                        case "15분 이하":
                            filterTimeItem.add("15");
                            break;
                        case "30분":
                            filterTimeItem.add("30");
                            break;
                        case "60분 이상":
                            filterTimeItem.add("60");
                            break;
                    }
                }


                RecyclerView recyclerView = findViewById(R.id.filter_item_recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

                filterItemButtonAdapter buttonAdapter = new filterItemButtonAdapter(buttonList);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(buttonAdapter);

                getFilteringRecipeList();

            } else {
                Log.e("logcat", "filterItem is null");
            }
        }
        else if(beforeIntentTitle.equals("home_page_category")){
            filterCategoryItem = beforeIntent.getStringArrayListExtra("filterCategoryItem");
            if (filterItem != null) {
                String intentTitle = beforeIntent.getStringExtra("intentTitle");

                List<filterItemBtnModel> buttonList = new ArrayList<>();
                if (filterCategoryItem != null && !filterCategoryItem.isEmpty()) {
                    buttonList.add(new filterItemBtnModel(filterCategoryItem.get(0)));
                }

                RecyclerView recyclerView = findViewById(R.id.filter_item_recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

                filterItemButtonAdapter buttonAdapter = new filterItemButtonAdapter(buttonList);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(buttonAdapter);

                getCategoryRecipeList();

            } else {
                Log.e("logcat", "filterItem is null");
                // Handle the case where filterItem is null
            }
        } else if(beforeIntentTitle.equals("myRecipe")) { // 내가 올린 레시피
            LinearLayout filterTitleLayout = (LinearLayout)findViewById(R.id.filter_item_layout);
            filterTitleLayout.setVisibility(View.GONE);
            getmyRecipeList();

        }  else if(beforeIntentTitle.equals("recentRecipe")) { // 최근 본 레시피
            LinearLayout filterTitleLayout = (LinearLayout)findViewById(R.id.filter_item_layout);
            filterTitleLayout.setVisibility(View.GONE);
            getRecentRecipeList();
        } else {
            Log.e("logcat", "Exception while processing Intent data");
        }

    }

    // 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = findViewById(R.id.result_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = recipeList;

        BasicRecipeListAdapter rlAdapter = new BasicRecipeListAdapter(items);
        recommendedList.setAdapter(rlAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }



    /*
    파이어베이스에서 찾아서 가져오기
     */
    private void getmyRecipeList() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 나의 레시피 추가
                ArrayList<String> tmp = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("myRecipe").getChildren()) {
                    tmp.add((String) snapshot.getValue());
                }
                for (int i = 0; i < tmp.size(); ++i) {
                    String name = tmp.get(i);
                    for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {

                        if (snapshot.getKey().equals(name)) {
                            Log.d("FAVORITE", name);
                            recipeItem item = new recipeItem();
                            for (DataSnapshot detail : snapshot.getChildren()) {
                                if (detail.getKey().equals("title"))
                                    item.setTitle(detail.getValue().toString());
                                else if (detail.getKey().equals("totaltime"))
                                    item.setTotalTime((Long) detail.getValue());
                                else if (detail.getKey().equals("likeCnt"))
                                    item.setLikeCnt((Long) detail.getValue());
                                // 사진 추가
                            }
                            item.setResId(R.drawable.sample_img);
                            recipeList.add(item);
                        }
                    }
                }
                // 레시피 리스트 뷰 배치
                updateRecommendedList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }


    /*
    파이어베이스에서 찾아서 가져오기
     */
    private void getRecentRecipeList() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 나의 레시피 추가
                ArrayList<String> tmp = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("recentRecipe").getChildren()) {
                    tmp.add((String) snapshot.getValue());
                }
                for (int i = 0; i < tmp.size(); ++i) {
                    String name = tmp.get(i);
                    for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {
                        if (snapshot.getKey().equals(name)) {
                            Log.d("FAVORITE", name);
                            recipeItem item = new recipeItem();
                            for (DataSnapshot detail : snapshot.getChildren()) {
                                if (detail.getKey().equals("title"))
                                    item.setTitle(detail.getValue().toString());
                                else if (detail.getKey().equals("totaltime"))
                                    item.setTotalTime((Long) detail.getValue());
                                else if (detail.getKey().equals("likeCnt"))
                                    item.setLikeCnt((Long) detail.getValue());
                                // 사진 추가
                            }
                            item.setResId(R.drawable.sample_img);
                            recipeList.add(item);
                        }
                    }
                }
                // 레시피 리스트 뷰 배치
                updateRecommendedList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /*
파이어베이스에서 찾아서 가져오기
 */
    private void getFilteringRecipeList() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {
                    boolean ingredientMatch = true;
                    boolean timeMatch = true;

                    // Check if filterItem is not empty
                    if (!filterItem.isEmpty()) {
                        ArrayList<String> tmp = new ArrayList<String>();
                        Log.d(TAG, snapshot.child("recipeIngredient").toString());
                        List<List<String>> t = (List<List<String>>) snapshot.child("recipeIngredient").getValue();
                        for(List<String> s : t){
                            tmp.add(s.get(0));
                        }
                        for (String ingredient : filterItem) {
                            if (!tmp.contains(ingredient)) {
                                ingredientMatch = false;
                                break;
                            }
                        }
                    }

                    if (!filterTimeItem.isEmpty()) {
                        Long totalTime = snapshot.child("totaltime").getValue(Long.class);
                        if (totalTime == null || !filterTimeItem.contains(String.valueOf(totalTime))) {
                            timeMatch = false;
                        }
                    }

                    if (ingredientMatch && timeMatch) {
                        recipeItem item = new recipeItem();
                        for (DataSnapshot detail : snapshot.getChildren()) {
                            if (detail.getKey().equals("title"))
                                item.setTitle(detail.getValue().toString());
                            else if (detail.getKey().equals("totaltime"))
                                item.setTotalTime((Long) detail.getValue());
                            else if (detail.getKey().equals("likeCnt"))
                                item.setLikeCnt((Long) detail.getValue());
                            // Add more details as needed
                        }
                        item.setResId(R.drawable.sample_img);
                        recipeList.add(item);
                    }
                }
                // 레시피 리스트 뷰 배치
                updateRecommendedList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /*
    파이어베이스에서 찾아서 가져오기
    카테고리로 찾기
     */
    private void getCategoryRecipeList() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String category = filterCategoryItem.get(0);
                for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {
                    String recipe_cate = snapshot.child("category").getValue(String.class);
                    if (Objects.equals(recipe_cate, category)) {
                        recipeItem item = new recipeItem();
                        for (DataSnapshot detail : snapshot.getChildren()) {
                            if (detail.getKey().equals("title"))
                                item.setTitle(detail.getValue().toString());
                            else if (detail.getKey().equals("totaltime"))
                                item.setTotalTime((Long) detail.getValue());
                            else if (detail.getKey().equals("likeCnt"))
                                item.setLikeCnt((Long) detail.getValue());
                            // 사진 추가
                        }
                        item.setResId(R.drawable.sample_img);
                        recipeList.add(item);
                    }
                }
                updateRecommendedList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}