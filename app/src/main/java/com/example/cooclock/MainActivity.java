package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.navigationView);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_FrameLayout, new home_page()).commit(); //FrameLayout에 fragment.xml 띄우기

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.nav_item_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new home_page()).commit();
                        break;
                    case R.id.nav_item_write:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new recipe_write_page()).commit();
                        break;
                    case R.id.nav_item_refrigerator:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new my_refrigerator_page()).commit();
                        break;
                    case R.id.nav_item_heart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new favorite_recipe_page()).commit();
                        break;
                    case R.id.nav_item_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout, new profile_page()).commit();
                        break;
                }
                return true;
            }
        });
    }
}