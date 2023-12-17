package com.example.cooclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class profile_page extends Fragment {
    private static String TAG = "PROFILE";
    View ProfileRootView;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    UserAccount userAccount = new UserAccount();
    ArrayList<knowHowItem> myRecipe = new ArrayList<knowHowItem>();
    ArrayList<knowHowItem> recentRecipe = new ArrayList<knowHowItem>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ProfileRootView = inflater.inflate(R.layout.activity_profile_page,container,false);
        updateUserInfo();
        getRecipeLists();

        LinearLayout goto_result_page_MyRecipie = ProfileRootView.findViewById(R.id.layout_MyRecipie);
        goto_result_page_MyRecipie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(requireContext(), result_page.class);
                resultIntent.putExtra("intentTitle","myRecipe");
                resultIntent.putExtra("titleText","내가 올린 레시피입니다.");
                startActivity(resultIntent);
            }
        });

        LinearLayout goto_result_page_RecentView = ProfileRootView.findViewById(R.id.layout_RecentViewRecipie);
        goto_result_page_RecentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(requireContext(), result_page.class);
                resultIntent.putExtra("intentTitle","recentRecipe");
                resultIntent.putExtra("titleText","최근 본 레시피입니다.");
                startActivity(resultIntent);
            }
        });

        LinearLayout gotoAccount = ProfileRootView.findViewById(R.id.go_account);
        gotoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), setting_page.class);
                startActivity(intent);
            }
        });

        LinearLayout goto_logout = ProfileRootView.findViewById(R.id.logout);
        goto_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //!TODO 로그아웃 부분 코드 추가
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(requireContext(),login_page.class);
                startActivity(intent);
                getActivity().finish();
                Log.d("logcat", "logout");
            }
        });

        return ProfileRootView;
    }


    /*
    내가 올린 레시피
    */
    // 내가 올린 레시피 뷰를 위한 뷰 홀더
    public static class MyRecipeListViewHolder extends RecyclerView.ViewHolder {
        private TextView short_recipe_title;
        private ImageView short_recipe_image;

        public MyRecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            short_recipe_title = itemView.findViewById(R.id.short_recipe_itme_title);
            short_recipe_image = itemView.findViewById(R.id.short_recipe_itme_image);
        }
    }

    // 내가 올린 레시피를 관리할 adapter
    public static class MyRecipeListAdapter extends RecyclerView.Adapter<profile_page.MyRecipeListViewHolder> {
        ArrayList<knowHowItem> items;

        public MyRecipeListAdapter(ArrayList<knowHowItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public profile_page.MyRecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_short_item, parent, false);
            return new profile_page.MyRecipeListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull profile_page.MyRecipeListViewHolder holder, int position) {
            knowHowItem item = items.get(position);
            holder.short_recipe_title.setText(item.getTitle());
            holder.short_recipe_image.setImageResource(item.getResId());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 내가 올린 레시피 업데이트 코드
    private void updateRecipeShortList(){

        RecyclerView RecipeShortList = ProfileRootView.findViewById(R.id.my_recipe_short_list);

        ArrayList<knowHowItem> items = myRecipe;

        profile_page.MyRecipeListAdapter rlAdapter = new profile_page.MyRecipeListAdapter(items);
        RecipeShortList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false);
        RecipeShortList.setLayoutManager(layoutManager);
    }


    /*
    최근 본 레시피
    */
    // 최근 본 레시피 뷰를 위한 뷰 홀더
    public static class CurrentRecipeListViewHolder extends RecyclerView.ViewHolder {
        private TextView short_recipe_title;
        private ImageView short_recipe_image;

        public CurrentRecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            short_recipe_title = itemView.findViewById(R.id.short_recipe_itme_title);
            short_recipe_image = itemView.findViewById(R.id.short_recipe_itme_image);
        }
    }

    // 최근 본 레시피를 관리할 adapter
    public static class CurrentRecipeListAdapter extends RecyclerView.Adapter<profile_page.CurrentRecipeListViewHolder> {
        ArrayList<knowHowItem> items;

        public CurrentRecipeListAdapter(ArrayList<knowHowItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public profile_page.CurrentRecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_short_item, parent, false);
            return new profile_page.CurrentRecipeListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull profile_page.CurrentRecipeListViewHolder holder, int position) {
            knowHowItem item = items.get(position);
            holder.short_recipe_title.setText(item.getTitle());
            holder.short_recipe_image.setImageResource(item.getResId());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 최근 본 레시피 업데이트 코드
    private void updateCurrentShortList(){

        RecyclerView RecipeShortList = ProfileRootView.findViewById(R.id.current_recipe_short_list);

        ArrayList<knowHowItem> items = recentRecipe;

        profile_page.CurrentRecipeListAdapter rlAdapter = new profile_page.CurrentRecipeListAdapter(items);
        RecipeShortList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false);
        RecipeShortList.setLayoutManager(layoutManager);
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
                // 나의 레시피 추가
                ArrayList<String> tmp = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("myRecipe").getChildren()) {
                    tmp.add((String) snapshot.getValue());
                }
                for (int i = 0; i < tmp.size(); ++i) {
                    String name = tmp.get(i);
                    for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {
                        if (snapshot.getKey().equals(name)) {
                            knowHowItem item = new knowHowItem();
                            for (DataSnapshot detail : snapshot.getChildren()) {
                                if (detail.getKey().equals("title"))
                                    item.setTitle(name);
                                // 사진 추가
                            }
                            item.setResId(R.drawable.sample_img);
                            myRecipe.add(item);
                        }
                    }
                }

                // 최근 본 레시피 추가
                ArrayList<String> tmp2 = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.child("UserAccount").child(currentUser.getUid()).child("recentRecipe").getChildren()) {
                    tmp2.add((String) snapshot.getValue());
                }
                for (int i = 0; i < tmp2.size(); ++i) {
                    String name = tmp2.get(i);
                    for (DataSnapshot snapshot : dataSnapshot.child("Recipe").getChildren()) {
                        if (snapshot.getKey().equals(name)) {
                            knowHowItem item = new knowHowItem();
                            for (DataSnapshot detail : snapshot.getChildren()) {
                                if (detail.getKey().equals("title"))
                                    item.setTitle(name);
                                // 사진 추가
                            }
                            item.setResId(R.drawable.sample_img);
                            recentRecipe.add(item);
                        }
                    }
                }
                updateRecipeShortList();
                updateCurrentShortList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void updateUserInfo() {
        // 유저이름 반영하기
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock").child("UserAccount");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            TextView username = ProfileRootView.findViewById(R.id.username);
            RecyclerView RecipeShortList = ProfileRootView.findViewById(R.id.current_recipe_short_list);

            mDatabaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String usernamestr = dataSnapshot.child("username").getValue(String.class);
                        if (usernamestr != null) {
                            username.setText(usernamestr + "님");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
    }
}