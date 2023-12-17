package com.example.cooclock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class setting_page extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        TextView nickname = (TextView) findViewById(R.id.setting_change_nickname);

        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_change_nickname");
            }
        });


//        TextView email = (TextView) findViewById(R.id.setting_change_email);
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("logcat","click setting_change_email");
//            }
//        });
//
//        TextView password = (TextView) findViewById(R.id.setting_change_password);
//        password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("logcat","click setting_change_password");
//            }
//        });

        TextView delete_user = (TextView) findViewById(R.id.setting_delete_user);
        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logcat","click setting_delete_user");
            }
        });

        // 유저 정보 업데이트
        updateUserInfo();
    }


    public void updateUserInfo() {
        // 유저이름 반영하기
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock").child("UserAccount");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            TextView username = findViewById(R.id.username);
            RecyclerView MyRecipeList = findViewById(R.id.my_recipe_short_list);
            RecyclerView RecentRecipeList = findViewById(R.id.current_recipe_short_list);

            mDatabaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserAccount userAccount = new UserAccount();

                        // 기본 필드 설정
                        userAccount.setIdToken(dataSnapshot.child("idToken").getValue(String.class));
                        userAccount.setEmailId(dataSnapshot.child("emailId").getValue(String.class));
                        userAccount.setUsername(dataSnapshot.child("username").getValue(String.class));

                        // Recipe 데이터 가져오기
                        ArrayList<knowHowItem> recipes = new ArrayList<knowHowItem>();
                        DataSnapshot recipeSnapshot = dataSnapshot.child("Recipe");
                        for (DataSnapshot itemSnapshot : recipeSnapshot.getChildren()) {
                            knowHowItem recipeItem = new knowHowItem();
                            recipes.add(recipeItem);
                        }

//                        // UserAccount에 레시피 목록 설정
//                        userAccount.setMyRecipes(recipes);
//                        profile_page.CurrentRecipeListAdapter rlAdapter = new profile_page.CurrentRecipeListAdapter(userAccount.getMyRecipes());
//                        MyRecipeList.setAdapter(rlAdapter);
//
//
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false);
//                        RecipeShortList.setLayoutManager(layoutManager);



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
    }
}