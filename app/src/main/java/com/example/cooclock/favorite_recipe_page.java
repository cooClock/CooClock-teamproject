package com.example.cooclock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class favorite_recipe_page extends Fragment {
    View rootView;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    ArrayList<recipeItem> favoriteRecipe = new ArrayList<recipeItem>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.activity_favorite_recipe_page,container,false);

        updataUserInfo();
        getRecipeLists();


        //return inflater.inflate(R.layout.activity_favorite_recipe_page,container,false);
        return rootView;
    }

    // 추천 레시피 리스트 업데이트 코드
    private void updateFavoriteList(){
        RecyclerView favoriteList = rootView.findViewById(R.id.favorite_list);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<recipeItem> items = favoriteRecipe;


        BasicRecipeListAdapter rlAdapter = new BasicRecipeListAdapter(items);
        favoriteList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        favoriteList.setLayoutManager(layoutManager);
    }


    public void updataUserInfo() {

        // 유저이름 반영하기
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock").child("UserAccount");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            TextView username = rootView.findViewById(R.id.user_name);
            mDatabaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
//                        UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                        String usernamestr = dataSnapshot.child("username").getValue(String.class);
                        if (usernamestr != null) {
                            username.setText(usernamestr + "님");
                            Log.d("Firebase_user",usernamestr);

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // onCancelled 메서드를 구현하여 오류 처리
                    Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                    // 오류 처리를 추가하거나 필요한 작업 수행
                }
            });
        }
        else{
            Intent intent = new Intent(requireContext(), login_page.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    /*
    파이어베이스에서 찾아서 가져오기
     */
    private void getRecipeLists() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 즐겨찾는 레시피 추가
                ArrayList<String> tmp = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("favoriteRecipe").getChildren()) {
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
                            favoriteRecipe.add(item);
                        }
                    }
                }
                // 리스트 뷰 배치
                updateFavoriteList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}