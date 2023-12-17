package com.example.cooclock;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home_page extends Fragment {
    View rootView;
    private static String TAG = "HOME_PAGE";
    public ArrayList<String> filterItem = new ArrayList<>();
    ArrayList<Integer> showList  = new ArrayList<Integer>();
    ArrayList<recipeItem> recipeList = new ArrayList<recipeItem>();
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.activity_home_page,container,false);
        TextView username = rootView.findViewById(R.id.user_name);

        // 카테고리 선택 버튼(라디오 그룹)
        MaterialButtonToggleGroup categorySelect = rootView.findViewById(R.id.category_select);
        Button foodType = rootView.findViewById(R.id.food_type);
        Button foodSituation = rootView.findViewById(R.id.food_situation);
        // 유저 정보 업데이트
        updataUserInfo();
        getRecipeLists();

        categorySelect.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                updateSubCategories();
            }
        });

        LinearLayout goto_filter = rootView.findViewById(R.id.goto_filtering_page);
        goto_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), filtering_page.class);
                startActivity(intent);
            }
        });

        LinearLayout goto_result_page = rootView.findViewById(R.id.goto_result_page);
        goto_result_page.setOnClickListener(view -> {
            Intent resultIntent = new Intent(requireContext(), result_page.class);
            resultIntent.putExtra("intentTitle","home_page");
            resultIntent.putExtra("titleText","오늘의 추천 레시피입니다.");
            startActivity(resultIntent);
        });

        LinearLayout goto_my_refrigerator = rootView.findViewById(R.id.go_my_refrigerator);
        goto_my_refrigerator.setOnClickListener(view -> {
            // activity 전환 + filterItem 내용 전달
            Intent resultIntent = new Intent(requireContext(), MainActivity.class);
            resultIntent.putExtra("goto","refrigerator");
            startActivity(resultIntent);
            getActivity().finish();
        });

        return rootView;
    }

    /*
    세부 카테고리
     */
    // 세부 카테고리 리사이클러 뷰를 위한 뷰 홀더
    public static class SubCategoryCustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView category_icon;
        private TextView category_name;
        public SubCategoryCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            category_icon = itemView.findViewById(R.id.subCategory_icon);
            category_name = itemView.findViewById(R.id.subCategory_name);
        }
    }

    // 세부 카테고리 관리할 adapter
    public class SubCategoryCustomAdapter extends RecyclerView.Adapter<home_page.SubCategoryCustomViewHolder> {
        ArrayList<subCategroyItem> items;

        public SubCategoryCustomAdapter(ArrayList<subCategroyItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.SubCategoryCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategroy_item, parent, false);
            return new home_page.SubCategoryCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.SubCategoryCustomViewHolder holder, int position) {
            subCategroyItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResId());
            holder.category_name.setText(item.getSubCategoryName());
            holder.category_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String subCategoryName = (String) holder.category_name.getText();
                    filterItem.add(subCategoryName);
                    Intent resultIntent = new Intent(requireContext(), result_page.class);
                    resultIntent.putExtra("intentTitle","home_page_category");
                    resultIntent.putExtra("titleText",subCategoryName+"를 확인해보세요.");
                    resultIntent.putStringArrayListExtra("filter", filterItem);
                    startActivity(resultIntent);
                    filterItem.clear();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
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
    // 세부 카테고리 업데이트 코드
    public void updateSubCategories(){
        RecyclerView subCategory = rootView.findViewById(R.id.subCategory);

        ArrayList<subCategroyItem> items = new ArrayList<subCategroyItem>();
        MaterialButtonToggleGroup categoryToggleGroup = rootView.findViewById(R.id.category_select);
        int selectedMainCategoryId = categoryToggleGroup.getCheckedButtonId(); // 어떤 것이 눌려져있는지 확인
        if (selectedMainCategoryId != R.id.food_situation){
            Button btn = rootView.findViewById(R.id.food_type);
            Button btn2 = rootView.findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pc_yello)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
            items.add(new subCategroyItem("한식",R.drawable.food_type_korea));
            items.add(new subCategroyItem("일식",R.drawable.food_type_japan));
            items.add(new subCategroyItem("중식",R.drawable.food_type_china));
            items.add(new subCategroyItem("양식",R.drawable.food_type_western));
            items.add(new subCategroyItem("분식",R.drawable.food_type_flour));
            items.add(new subCategroyItem("간식",R.drawable.food_type_snack));
            items.add(new subCategroyItem("면류",R.drawable.food_type_noodle));
            items.add(new subCategroyItem("반찬류",R.drawable.food_type_side));
        }
        else{
            Button btn = rootView.findViewById(R.id.food_type);
            Button btn2 = rootView.findViewById(R.id.food_situation);
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
            btn2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pc_yello)));

            items.add(new subCategroyItem("소주상",R.drawable.food_situation_soju));
            items.add(new subCategroyItem("맥주상",R.drawable.food_situation_beer));
            items.add(new subCategroyItem("대접용",R.drawable.food_situation_hospitality));
            items.add(new subCategroyItem("데이트",R.drawable.food_situation_date));
            items.add(new subCategroyItem("보양식",R.drawable.food_situation_health));
            items.add(new subCategroyItem("도시락",R.drawable.food_situation_lunchbox));
            items.add(new subCategroyItem("아이용",R.drawable.food_situation_child));
            items.add(new subCategroyItem("이유식",R.drawable.food_situation_baby));
        }

        home_page.SubCategoryCustomAdapter scAdapter = new home_page.SubCategoryCustomAdapter(items);
        subCategory.setAdapter(scAdapter);

        // fix to grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
        subCategory.setLayoutManager(layoutManager);
    }


    /*
    추천 레시피 리스트
    */
    // 추천 레시피 리스트리사이클러 뷰를 위한 뷰 홀더
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

    // 추천 레시피 리스트를 관리할 adapter
    public class RecipeListCustomAdapter extends RecyclerView.Adapter<home_page.RecipeListCustomViewHolder> {
        ArrayList<recipeItem> items;

        public RecipeListCustomAdapter(ArrayList<recipeItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.RecipeListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new home_page.RecipeListCustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.RecipeListCustomViewHolder holder, int position) {
            recipeItem item = items.get(position);
            holder.recipe_image.setImageResource(item.getResId());
            holder.recipe_title.setText(item.getTitle());
            holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
            holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
            holder.recipe_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeTitle = (String) holder.recipe_title.getText();
                    Intent recipeIntent = new Intent(requireContext(), recipe_page.class);
                    recipeIntent.putExtra("recipeTitle",recipeTitle);
                    startActivity(recipeIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }

    // 추천 레시피 리스트 업데이트 코드
    private void updateRecommendedList(){
        RecyclerView recommendedList = rootView.findViewById(R.id.recommend_list);

        ArrayList<recipeItem> items = recipeList;

        home_page.RecipeListCustomAdapter rlAdapter = new home_page.RecipeListCustomAdapter(items);
        recommendedList.setAdapter(rlAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        recommendedList.setLayoutManager(layoutManager);
    }


    /*
    나의 냉장고 재료 카테고리
     */
    // 재료 카테고리 리사이클러 뷰를 위한 뷰 홀더
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView category_icon;
        private TextView category_name;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            category_icon = itemView.findViewById(R.id.my_refrigerator_category_icon);
            category_name = itemView.findViewById(R.id.my_refrigerator_category_name);
        }
    }

    // 재료 카테고리 관리할 adapter
    public static class CustomAdapter extends RecyclerView.Adapter<home_page.CustomViewHolder> {
        ArrayList<myRefrigeratorCategoryItem> items;

        public CustomAdapter(ArrayList<myRefrigeratorCategoryItem> a_list){
            items = a_list;
        }

        @NonNull
        @Override
        public home_page.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_refrigerator_category_item, parent, false);
            return new home_page.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull home_page.CustomViewHolder holder, int position) {
            myRefrigeratorCategoryItem item = items.get(position);
            holder.category_icon.setImageResource(item.getResID());
            holder.category_name.setText(item.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // 재료 카테고리 업데이트 코드
    private void updateMyRefrigeratorCategoryList(){
        RecyclerView myRefrigeratorCategory = rootView.findViewById(R.id.my_refrigerator_category);
//        myRefrigeratorCategory.removeAllViews();

        ArrayList<myRefrigeratorCategoryItem> items = new ArrayList<myRefrigeratorCategoryItem>();

        items.add(new myRefrigeratorCategoryItem("과일-채소",R.drawable.refrigerator_category_fruit));
        items.add(new myRefrigeratorCategoryItem("육류",R.drawable.refrigerator_category_meat));
        items.add(new myRefrigeratorCategoryItem("어패류",R.drawable.refrigerator_fishs));
        items.add(new myRefrigeratorCategoryItem("곡류",R.drawable.refrigerator_grains));
        items.add(new myRefrigeratorCategoryItem("난류",R.drawable.refrigerator_eggs));
        items.add(new myRefrigeratorCategoryItem("유제품",R.drawable.refrigerator_dairys));
        items.add(new myRefrigeratorCategoryItem("기타",R.drawable.refrigerator_etc));

        home_page.CustomAdapter rAdapter = new home_page.CustomAdapter(items);
        myRefrigeratorCategory.setAdapter(rAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        myRefrigeratorCategory.setLayoutManager(layoutManager);
    }




    /*
    파이어베이스에서 찾아서 가져오기
     */
    private void getRecipeLists() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // firebase 연동
        DatabaseReference mDatabase = database.getReference("cooclock").child("Recipe");  // DB테이블 연결
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()));
//                Log.d(TAG, "Tot Data: " + dataSnapshot.getValue().toString());
                ArrayList<String> allList  = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, snapshot.getKey());
                    allList.add(snapshot.getKey());
                }

                for (int i=0; i<2; ++i) {
                    while (true) {
                        int tmp = (int) (Math.random() * allList.size());
                        if (!showList.contains(tmp)) {
                            showList.add(tmp);
                            break;
                        }
                    }
                }

                for(int i=0; i<2; ++i){
                    String name = allList.get(showList.get(i));
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.getKey().equals(name)){
                            recipeItem item = new recipeItem();

                            for (DataSnapshot detail : snapshot.getChildren()) {
                                Log.d(TAG, i +" " + detail.getKey());
                                if (detail.getKey().equals("title"))
                                    item.setTitle(detail.getValue().toString());
                                else if (detail.getKey().equals("totaltime"))
                                    item.setTotalTime((Long) detail.getValue());
                                else if (detail.getKey().equals("likeCnt"))
                                    item.setLikeCnt((Long) detail.getValue());
                                // 사진 추가
                            }
                            item.setResId(R.drawable.sample_img);
                            Log.d(TAG, item.title+ " "+ item.likeCnt+ " "+ item.totalTime);
                            recipeList.add(item);
                        }
                    }
                }
                // 그리드 뷰 배치
                updateSubCategories();
                // 리스트 뷰 배치
                updateRecommendedList();
                // 리사이클러뷰 배치
                updateMyRefrigeratorCategoryList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}