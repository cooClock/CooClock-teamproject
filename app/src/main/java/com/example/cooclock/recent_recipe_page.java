package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class recent_recipe_page extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<recipeItem> items;
//    private RecipeListCustomAdapter adapter;
    private RecipeListCustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_recipe_page);
        RecyclerView recentRecipeList = findViewById(R.id.recent_recipe_list);
        items = new ArrayList<recipeItem>();

        adapter = new RecipeListCustomAdapter(items);
        recentRecipeList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recentRecipeList.setLayoutManager(layoutManager);
        fetchFirebaseData();
    }

    private void fetchFirebaseData() {
        database = FirebaseDatabase.getInstance(); // firebase 연동
        databaseReference = database.getReference("cooclock").child("Recipe");  // DB테이블 연결
        String databaseReferenceString = databaseReference.toString(); // DatabaseReference를 문자열로 변환

        Log.d("DB", databaseReferenceString); // 디버그 로그에 DatabaseReference 출력


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
    최근 본 레시피
    */
    // 최근 본 레시피 리스트리사이클러 뷰를 위한 뷰 홀더
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

    // 최근 본 레시피 리스트를 관리할 adapter
    public static class RecipeListCustomAdapter extends RecyclerView.Adapter<recent_recipe_page.RecipeListCustomViewHolder> {
        ArrayList<recipeItem> items;
        private Context context;

        public RecipeListCustomAdapter(ArrayList<recipeItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public recent_recipe_page.RecipeListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new recent_recipe_page.RecipeListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recent_recipe_page.RecipeListCustomViewHolder holder, int position) {
            recipeItem item = items.get(position);
//            holder.recipe_image.setImageResource(item.getResId());
            Glide.with(holder.itemView).load(item.getResId()).into(holder.recipe_image);
            holder.recipe_title.setText(item.getTitle());
            holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
            holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
        }

        @Override
        public int getItemCount() {
            return (items != null ? items.size() : 0);
        }

    }

    // 최근 본 레시피리스트 업데이트 코드
    private void updateRecentRecipeList(){
        RecyclerView recentRecipeList = findViewById(R.id.recent_recipe_list);
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

        recent_recipe_page.RecipeListCustomAdapter rlAdapter = new recent_recipe_page.RecipeListCustomAdapter(items);
        recentRecipeList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recentRecipeList.setLayoutManager(layoutManager);
    }

}