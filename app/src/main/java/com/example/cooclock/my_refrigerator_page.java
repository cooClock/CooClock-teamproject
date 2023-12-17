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

public class my_refrigerator_page extends Fragment {
    View MyRefrigeratorView;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    ArrayList<ingredientItem> myIngredient = new ArrayList<ingredientItem>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        MyRefrigeratorView = inflater.inflate(R.layout.activity_my_refrigerator_page,container,false);
        TextView go_add_self = MyRefrigeratorView.findViewById(R.id.go_add_ingredient_self_page);
        TextView go_photo = MyRefrigeratorView.findViewById(R.id.go_photo_scan_page);

        getMyIngredient();
        updataUserInfo();


        go_add_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), add_ingredient_self_page.class);
                startActivity(intent);
            }
        });

        go_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), photo_scan_page.class);
                startActivity(intent);
            }
        });

        return MyRefrigeratorView;
    }


    // 재료 카테고리 업데이트 코드
    private void initializeIngredients(){
        RecyclerView myIngredientList = MyRefrigeratorView.findViewById(R.id.my_refrigerator_ingredient_list);

        ArrayList<ingredientItem> items = myIngredient;

        IngredientListAdapter rAdapter = new IngredientListAdapter(items);
        myIngredientList.setAdapter(rAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        myIngredientList.setLayoutManager(layoutManager);
    }
    public void updataUserInfo() {

        // 유저이름 반영하기
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock").child("UserAccount");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            TextView username = MyRefrigeratorView.findViewById(R.id.user_name);
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
    private void getMyIngredient() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
//                    items.add(new ingredientItem("소고기 국거리", "0.1","육류"));
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 즐겨찾는 레시피 추가
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("myIngredient").getChildren()) {
                    ingredientItem item = new ingredientItem();
                    ArrayList<String> tmp = ((ArrayList<String>) snapshot.getValue());
                    item.setName(tmp.get(0));
                    item.setWeight(tmp.get(1));
                    item.setKind(tmp.get(2));
                    myIngredient.add(item);
                    Log.d("MY", snapshot.getKey() + " : " + item.kind);

                }
                Log.d("MY", String.valueOf(myIngredient.size()));
                initializeIngredients();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}