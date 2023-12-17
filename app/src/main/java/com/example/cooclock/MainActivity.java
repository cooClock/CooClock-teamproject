package com.example.cooclock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView; //바텀 네비게이션9 뷰
    String gotoWhere = null;
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스 연동
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);
        String nickname = sharedPreferences.getString("nickname",null);
        Log.d("MAIN", savedUsername+savedPassword+nickname);

        if (savedUsername != null && savedPassword != null) {

            mFirebaseAuth = FirebaseAuth.getInstance();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock");
            mFirebaseAuth.signInWithEmailAndPassword(savedUsername, savedPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                    } else {
                        Intent intent = new Intent(MainActivity.this, login_page.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else{
            Intent intent = new Intent(MainActivity.this, login_page.class);
            startActivity(intent);
            finish();
        }


        bottomNavigationView = findViewById(R.id.navigationView);

        Intent beforeIntent = getIntent();
        gotoWhere = beforeIntent.getStringExtra("goto");

        if(gotoWhere!=null){
            //처음화면
            bottomNavigationView.setSelectedItemId(R.id.nav_item_refrigerator);
            my_refrigerator_page ref =  new my_refrigerator_page();
            Bundle bundle2 = new Bundle();
            if (nickname != null){
                bundle2.putString("nickname",nickname);
                ref.setArguments(bundle2);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.main_FrameLayout, ref).commit(); //FrameLayout에 fragment.xml 띄우기
        }else{
            //처음화면
            home_page home =  new home_page();
            Bundle bundle = new Bundle();
            if (nickname != null){
                bundle.putString("nickname",nickname);
                home.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.main_FrameLayout, home).commit(); //FrameLayout에 fragment.xml 띄우기
        }


        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.nav_item_home:
                        home_page home =  new home_page();
                        Bundle bundle = new Bundle();
                        if (nickname != null){
                            bundle.putString("nickname",nickname);
                            home.setArguments(bundle);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, home).commit();
                        break;
                    case R.id.nav_item_write:
                        Intent intent = new Intent(MainActivity.this, recipe_write_page.class);
                        startActivity(intent);
                        finish();
                        //nav_item_write를 false로 바꾸는 코드 추가
                        bottomNavigationView.setSelectedItemId(R.id.nav_item_home); // Set home as selected
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new home_page()).commit();
                        break;
                    case R.id.nav_item_refrigerator:
                        my_refrigerator_page ref =  new my_refrigerator_page();
                        Bundle bundle2 = new Bundle();
                        if (nickname != null){
                            bundle2.putString("nickname",nickname);
                            ref.setArguments(bundle2);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, ref).commit();
                        break;
                    case R.id.nav_item_heart:
                        favorite_recipe_page heart =  new favorite_recipe_page();
                        Bundle bundle3 = new Bundle();
                        if (nickname != null){
                            bundle3.putString("nickname",nickname);
                            heart.setArguments(bundle3);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, heart).commit();
                        break;
                    case R.id.nav_item_profile:
                        profile_page prof =  new profile_page();
                        Bundle bundle4 = new Bundle();
                        if (nickname != null){
                            bundle4.putString("nickname",nickname);
                            prof.setArguments(bundle4);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, prof).commit();
                        break;
                }
                return true;
            }
        });

//        ImageView goto_my_refrigerator = findViewById(R.id.go_my_refrigerator);

    }
}