package com.example.cooclock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private FirebaseAuth mAuth;

    TextView userEmailTextView;
    LinearLayout emailEditLayout;
    EditText emailEditView;
    Button changeEmailButton;

    LinearLayout nicknameEditLayout;
    EditText nicknameEditView;
    Button changeNicknameButton;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        ///////////
        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);
        String nickname = sharedPreferences.getString("nickname",null);
        Log.d("MAIN", savedUsername+savedPassword+nickname);

        if (savedUsername != null && savedPassword != null) {

            mFirebaseAuth = FirebaseAuth.getInstance();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock");
            mFirebaseAuth.signInWithEmailAndPassword(savedUsername, savedPassword).addOnCompleteListener(setting_page.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("MAIN","aaa");
                    } else {
                        Intent intent = new Intent(setting_page.this, login_page.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else{
            Intent intent = new Intent(setting_page.this, login_page.class);
            startActivity(intent);
            finish();
        }

        //////////

        TextView nicknameChange = (TextView) findViewById(R.id.setting_change_nickname);
        TextView emailChange = (TextView) findViewById(R.id.setting_change_email);

        userEmailTextView = findViewById(R.id.user_email);
        emailEditLayout = findViewById(R.id.layout_email_Change);
        emailEditView = findViewById(R.id.change_email);
        changeEmailButton = findViewById(R.id.change_email_btn);

        nicknameEditLayout = findViewById(R.id.layout_nickname_Change);
        nicknameEditView = findViewById(R.id.change_nickname);
        changeNicknameButton = findViewById(R.id.change_nickname_btn);

        nicknameChange.setOnClickListener(view -> {
            Log.d("logcat","click setting_change_nickname");
            nicknameEditLayout.setVisibility(View.VISIBLE);
            changeNicknameButton.setVisibility(View.VISIBLE);
        });

        changeNicknameButton.setOnClickListener(view -> {
            nicknameEditLayout.setVisibility(View.GONE);
            changeNicknameButton.setVisibility(View.GONE);


            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String currentUserId = currentUser.getUid();
                mDatabaseRef.child(currentUserId).child("username").setValue(nicknameEditView.getText().toString());
                Toast.makeText(getApplicationContext(),"닉네임을 변경하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        emailChange.setOnClickListener(view -> {
            Log.d("logcat","click setting_change_nickname");
            emailEditLayout.setVisibility(View.VISIBLE);
            changeEmailButton.setVisibility(View.VISIBLE);
        });

        changeEmailButton.setOnClickListener(view -> {
            emailEditLayout.setVisibility(View.GONE);
            changeEmailButton.setVisibility(View.GONE);
            userEmailTextView.setText(emailEditView.getText().toString());

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String currentUserId = currentUser.getUid();
                mDatabaseRef.child(currentUserId).child("emailId").setValue(emailEditView.getText().toString());
                Toast.makeText(getApplicationContext(),"이메일을 변경하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });

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

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String currentUserId = currentUser.getUid();

                    // Get a reference to the child with currentUserId in mDatabaseRef
                    DatabaseReference userRef = mDatabaseRef.child(currentUserId);

                    // Remove the value at the reference
                    userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Deletion successful
                            Log.d("logcat", "User data deleted successfully");
                            // Add any additional logic if needed
                            Toast.makeText(getApplicationContext(),"계정이 삭제 되어 로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(setting_page.this, login_page.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Deletion failed
                            Log.e("logcat", "Error deleting user data: " + e.getMessage());
                            // Add any error handling logic if needed
                            Toast.makeText(getApplicationContext(),"계정이 삭제에 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

                        nicknameEditView.setText(userAccount.getUsername().toString());
                        userEmailTextView.setText(userAccount.getEmailId().toString());
                        emailEditView.setText(userAccount.getEmailId().toString());

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