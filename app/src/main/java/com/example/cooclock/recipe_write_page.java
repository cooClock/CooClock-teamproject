package com.example.cooclock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class recipe_write_page extends AppCompatActivity {
    Uri uri;
    ImageView imageView;
    LinearLayout get_picture;

//    SharedPreferences sharedPreferences;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증

    private FirebaseAuth mAuth;

    String nickname;

    int selectedPosition;
    String itemTitle;
    String itemCategory=null;
    int itemTime;
    int itemIngridientCount = 1;
    int itemRecipieCount = 1;
    int itemServings=0;
    JSONArray itemRating = new JSONArray();

    public ArrayList<Integer> idArray_itemIngridientET = new ArrayList<>();
    public ArrayList<Integer> idArray_itemIngridientAmount = new ArrayList<>();

    public ArrayList<Integer> idArray_itemRecipieET = new ArrayList<>();
    public ArrayList<Integer> idArray_itemRecipieTimeMinute = new ArrayList<>();
    public ArrayList<Integer> idArray_itemRecipieTimeSecond = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write_page);

        //id 변수값
        idArray_itemIngridientET.add(R.id.itemIngridient1);
        idArray_itemRecipieET.add(R.id.itemRecipie1);
        idArray_itemIngridientAmount.add(R.id.itemIngridientAmount1);
        idArray_itemRecipieTimeMinute.add(R.id.input_recipieTime_minute);
        idArray_itemRecipieTimeSecond.add(R.id.input_recipieTime_second);


        get_picture = findViewById(R.id.input_picture);
        imageView = findViewById(R.id.food_imageview);
        get_picture.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityResult.launch(intent);
        });

        TextView categorySelectText = findViewById(R.id.category_select_layout_text);
        LinearLayout categorySelect = findViewById(R.id.category_select_layout);
        categorySelect.setOnClickListener(view -> {
            AlertDialog.Builder dlg = new AlertDialog.Builder(recipe_write_page.this, R.style.MyAlertDialogStyle);
            dlg.setTitle("카테고리 선택"); //제목
            final String[] versionArray = new String[] {"한식","일식","중식","양식","분식","간식","면류","반찬류","소주상","맥주상","대접용","데이트","보양식","도시락","아이용","이유식"};

            selectedPosition = 0; // Default selected position

            dlg.setSingleChoiceItems(versionArray, 0, (dialog, which) -> {
                selectedPosition = which; // Update the selected position
                categorySelectText.setText(versionArray[which]);
            });

            dlg.setPositiveButton("확인", (dialog, which) -> {
                //토스트 메시지
//                        Toast.makeText(recipe_write_page.this,categorySelectText.getText()+"가 선택되었습니다.",Toast.LENGTH_SHORT).show();
                if (selectedPosition == 0) {
                    // No item selected, handle this case
                    if(!itemCategory.isEmpty()){
                        Toast.makeText(recipe_write_page.this, itemCategory + "이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(recipe_write_page.this, "카테고리가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Item selected, handle it
                    itemCategory = categorySelectText.getText().toString();
                    Toast.makeText(recipe_write_page.this, categorySelectText.getText() + "이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            dlg.show();
        });


        //기준인원 editText 값 정수로 제한하는 코드.
        EditText editText = findViewById(R.id.iteServings);
        // Set an InputFilter to restrict input to integers only
        InputFilter inputFilter = (source, start, end, dest, dstart, dend) -> {
            // Iterate through each character in the source
            for (int i = start; i < end; i++) {
                // Check if the character is not a digit
                if (!Character.isDigit(source.charAt(i))) {
                    // Return an empty string to reject the input
                    return "";
                }
            }
            // Allow the input because all characters are digiㅅts
            return null;
        };

        // Apply the InputFilter to the EditText
        editText.setFilters(new InputFilter[]{inputFilter});

        //user 정보 업데이트
        updateUserInfo();

    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        uri = result.getData().getData();
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                            get_picture.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    // 재료 용량 - or + button listener
//    public void sub_ingredient_weight(View v){
//        TextView textView = findViewById(R.id.input_ingredient_weight1);
//        textView.setText(String.format("%.1f",Float.parseFloat(textView.getText().toString())-0.1));
//    }
//    public void add_ingredient_weight(View v){
//        TextView textView = findViewById(R.id.input_ingredient_weight1);
//        textView.setText(String.format("%.1f",Float.parseFloat(textView.getText().toString())+0.1));
//    }
    // 재료 용량 - or + button listener
    public void sub_recipieStepTimer(View v){
        TextView secondTextView = findViewById(R.id.input_recipieTime_second);
        TextView minuteTextView = findViewById(R.id.input_recipieTime_minute);

        int second = Integer.parseInt(secondTextView.getText().toString()) - 10;
        if(second < 0){
            int minute = Integer.parseInt(minuteTextView.getText().toString())-1;
            if(minute>=0) {
                minuteTextView.setText(String.valueOf(minute));
                secondTextView.setText("50");
            }
        }else {
            if(second==0){
                secondTextView.setText("00");
            }else {
                secondTextView.setText(String.valueOf(second));
            }
        }
    }

    public void add_recipieStepTimer(View v){
        TextView secondTextView = findViewById(R.id.input_recipieTime_second);
        TextView minuteTextView = findViewById(R.id.input_recipieTime_minute);

        int second = Integer.parseInt(secondTextView.getText().toString()) + 10;
        if(second >= 60){
            int minute = Integer.parseInt(minuteTextView.getText().toString())+1;
            minuteTextView.setText(String.valueOf(minute));
            secondTextView.setText("00");
        }else {
            secondTextView.setText(String.valueOf(second));
        }
    }

    // 재료 추가 button
    public void add_ingredient(View v) {
        itemIngridientCount++;

        // Assuming you have a reference to your parent layout named layout_recipie
        LinearLayout layoutIngredient = findViewById(R.id.layout_ingridient);

        // Create a new LinearLayout
        LinearLayout newIngredientLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin10)); // Adjust the margin
        newIngredientLayout.setLayoutParams(layoutParams);
        newIngredientLayout.setOrientation(LinearLayout.VERTICAL); // Adjust orientation if needed
        newIngredientLayout.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7), // Adjust the vertical padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7)); // Adjust the vertical padding
        newIngredientLayout.setBackgroundResource(R.drawable.search_edit_text_border_gray); // Set background drawable


        // Create a new EditText
        EditText newEditText = new EditText(this);

        newEditText.setId(View.generateViewId()); //TODO : 이렇게 써도 맞는가 ㅋㅋ 처맞는가?
        idArray_itemIngridientET.add(newEditText.getId());

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0);
        newEditText.setLayoutParams(editTextParams);
        newEditText.setHint(getString(R.string.write_the_ingridient_name));
        newEditText.setTextColor(getResources().getColor(R.color.black));
        newEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        newEditText.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
        newEditText.setIncludeFontPadding(false);
        newEditText.setBackgroundColor(getResources().getColor(android.R.color.white));

        // Add the EditText to the new LinearLayout
        newIngredientLayout.addView(newEditText);

        // Add the new LinearLayout to the parent layout
        layoutIngredient.addView(newIngredientLayout);

        // Create a new LinearLayout
        LinearLayout newIngredientLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin25)); // Adjust the margin
        newIngredientLayout1.setLayoutParams(layoutParams1);
        newIngredientLayout1.setOrientation(LinearLayout.VERTICAL); // Adjust orientation if needed
        newIngredientLayout1.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7), // Adjust the vertical padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7)); // Adjust the vertical padding
        newIngredientLayout1.setBackgroundResource(R.drawable.search_edit_text_border_gray); // Set background drawable


        // Create a new EditText
        EditText newEditText1 = new EditText(this);

        newEditText1.setId(View.generateViewId()); //TODO : 이렇게 써도 맞는가 ㅋㅋ 처맞는가?
        idArray_itemIngridientAmount.add(newEditText1.getId());

        LinearLayout.LayoutParams editTextParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams1.setMargins(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0);
        newEditText1.setLayoutParams(editTextParams1);
        newEditText1.setHint(getString(R.string.write_the_ingridient_amount));
        newEditText1.setTextColor(getResources().getColor(R.color.black));
        newEditText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        newEditText1.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
        newEditText1.setIncludeFontPadding(false);
        newEditText1.setBackgroundColor(getResources().getColor(android.R.color.white));

        // Add the EditText to the new LinearLayout
        newIngredientLayout1.addView(newEditText1);

        // Add the new LinearLayout to the parent layout
        layoutIngredient.addView(newIngredientLayout1);
    }

//    레시피 추가 button
    public void add_recipe(View v) {
        itemRecipieCount++;

        // Assuming you have a reference to your parent layout named layout_recipie
        LinearLayout layoutRecipie = findViewById(R.id.layout_recipie);

        // Create a new TextView
        TextView newTextView = new TextView(this);
        newTextView.setText(String.format("%s단계",itemRecipieCount));
        newTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        newTextView.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_light));
        newTextView.setIncludeFontPadding(false);
        newTextView.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin10), // Adjust the padding
                0,
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7));

        // Add the TextView to the parent layout
        layoutRecipie.addView(newTextView);

        // Create a new LinearLayout
        LinearLayout newRecipieLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin15)); // Adjust the margin
        newRecipieLayout.setLayoutParams(layoutParams);
        newRecipieLayout.setOrientation(LinearLayout.VERTICAL); // Adjust orientation if needed
        newRecipieLayout.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7), // Adjust the vertical padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin15), // Adjust the horizontal padding
                getResources().getDimensionPixelSize(R.dimen.dimeMargin7)); // Adjust the vertical padding
        newRecipieLayout.setBackgroundResource(R.drawable.search_edit_text_border_gray); // Set background drawable

        // Create a new EditText
        EditText newEditText = new EditText(this);

        newEditText.setId(View.generateViewId()); //TODO : 이렇게 써도 되는게 맞는가 ????
        idArray_itemRecipieET.add(newEditText.getId());

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Adjust the horizontal margin
                0);
        newEditText.setLayoutParams(editTextParams);
        newEditText.setHint(getString(R.string.write_the_recipe));
        newEditText.setTextColor(getResources().getColor(R.color.black));
        newEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        newEditText.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
        newEditText.setIncludeFontPadding(false);
        newEditText.setBackgroundColor(getResources().getColor(android.R.color.white));

        // Add the EditText to the new LinearLayout
        newRecipieLayout.addView(newEditText);

        // Add the new LinearLayout to the parent layout
        layoutRecipie.addView(newRecipieLayout);

        // Create a new LinearLayout
        LinearLayout newLayout = new LinearLayout(this);
        LinearLayout.LayoutParams recipieTimerlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        newLayout.setLayoutParams(recipieTimerlayoutParams);
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setGravity(Gravity.CENTER);
        newLayout.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin15));


        // 분
        TextView minuteTextView = new TextView(this);
        minuteTextView.setId(View.generateViewId());
        idArray_itemRecipieTimeMinute.add(minuteTextView.getId());
        minuteTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        minuteTextView.setGravity(Gravity.CENTER);
        minuteTextView.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin20), // Left padding
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5),
                0
        );
        minuteTextView.setTextSize(25);
        minuteTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        minuteTextView.setText("0");

// 분 단위 텍스트
        TextView minuteUnitTextView = new TextView(this);
        minuteUnitTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        minuteUnitTextView.setGravity(Gravity.CENTER);
        minuteUnitTextView.setTextSize(17);
        minuteUnitTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        minuteUnitTextView.setTextColor(Color.BLACK);
        minuteUnitTextView.setText("분");
        minuteUnitTextView.setIncludeFontPadding(false);
        minuteUnitTextView.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin5), 0);

// 초
        TextView secondTextView = new TextView(this);

        secondTextView.setId(View.generateViewId());
        idArray_itemRecipieTimeSecond.add(secondTextView.getId());

        secondTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        secondTextView.setGravity(Gravity.CENTER);
        secondTextView.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5), // Left padding
                0,
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5),
                0
        );
        secondTextView.setTextSize(25);
        secondTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        secondTextView.setText("10");

        // 초 단위 텍스트
        TextView secondUnitTextView = new TextView(this);
        secondUnitTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        secondUnitTextView.setGravity(Gravity.CENTER);
        secondUnitTextView.setTextSize(17);
        secondUnitTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        secondUnitTextView.setTextColor(Color.BLACK);
        secondUnitTextView.setText("초");
        secondUnitTextView.setIncludeFontPadding(false);
        secondUnitTextView.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin20), 0);

        // - 버튼
        ImageView subButton = new ImageView(this);
        subButton.setLayoutParams(new ViewGroup.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30), // Width
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30)  // Height
        ));
        subButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        subButton.setImageResource(R.drawable.minus_btn);
        subButton.setBackgroundColor(Color.TRANSPARENT);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int second = Integer.parseInt(secondTextView.getText().toString()) - 10;
                if(second < 0){
                    int minute = Integer.parseInt(minuteTextView.getText().toString())-1;
                    if(minute>=0) {
                        minuteTextView.setText(String.valueOf(minute));
                        secondTextView.setText("50");
                    }
                }else {
                    if(second==0){
                        secondTextView.setText("00");
                    }else {
                        secondTextView.setText(String.valueOf(second));
                    }
                }
            }
        });

        // + 버튼
        ImageView addButton = new ImageView(this);
        addButton.setLayoutParams(new ViewGroup.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30), // Width
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30)  // Height
        ));
        addButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addButton.setImageResource(R.drawable.plus_btn);
        addButton.setBackgroundColor(Color.TRANSPARENT);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int second = Integer.parseInt(secondTextView.getText().toString()) + 10;
                if(second >= 60){
                    int minute = Integer.parseInt(minuteTextView.getText().toString())+1;
                    minuteTextView.setText(String.valueOf(minute));
                    secondTextView.setText("00");
                }else {
                    secondTextView.setText(String.valueOf(second));
                }
            }
        });

        // Add views to the new layout
        newLayout.addView(subButton);
        newLayout.addView(minuteTextView);
        newLayout.addView(minuteUnitTextView);
        newLayout.addView(secondTextView);
        newLayout.addView(secondUnitTextView);
        newLayout.addView(addButton);

        // Add the new LinearLayout to the parent layout
        layoutRecipie.addView(newLayout);
    }


    public void updateUserInfo() {
        // 유저이름 반영하기
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock").child("UserAccount");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            mDatabaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        nickname = dataSnapshot.child("username").getValue(String.class);
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

    public void write_recipe_complete(View v) throws JSONException {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock");
        EditText TitleEditText = (EditText) findViewById(R.id.itemTitle);
        Slider TimeSlider = findViewById(R.id.slider);
        EditText servingEditText = (EditText) findViewById(R.id.iteServings);
        itemTitle = TitleEditText.getText().toString();
        itemTime = (int) TimeSlider.getValue();
        itemServings = Integer.parseInt(servingEditText.getText().toString());
        itemRating.put(0);
        itemRating.put(0);
        itemRating.put(0);
        itemRating.put(0);
        itemRating.put(0);

            // Check if the value is not empty
            if (!itemTitle.isEmpty()) {
//                Log.d("logcat", "itemtitle:"+ itemTitle.toString());
                if(itemCategory != null){
                    if(itemServings>0) {
//                    Log.d("logcat", itemCategory.toString());
                        //TODO : ingridient list & recipie List 다 채워져있다고 생각함. 예외처리 추후 적용
                        JSONObject ingredientList = new JSONObject();
                        for (int i = 0; i < idArray_itemIngridientET.size(); i++) {
                            // For each ingredient, create a JSONArray and put it in the ingredientList JSONObject
                            int id11 = idArray_itemIngridientET.get(i);
                            EditText editText = findViewById(id11);

                            int id12 = idArray_itemIngridientAmount.get(i);
                            EditText editTextAmount = findViewById(id12);

                            JSONArray ingredientArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(editTextAmount.getText().toString()); // Change this to the actual quantity
                            ingredientList.put(String.valueOf(i), ingredientArray);
                        }

                        JSONObject recipieList = new JSONObject();
                        for (int i = 0; i < idArray_itemRecipieET.size(); i++) {
                            // For each recipie, create a JSONArray and put it in the recipieList JSONObject
                            int id2 = idArray_itemRecipieET.get(i);
                            EditText editText = findViewById(id2);

                            int id21 = idArray_itemRecipieTimeMinute.get(i);
                            TextView textViewMinute = findViewById(id21);
                            int id22 = idArray_itemRecipieTimeSecond.get(i);
                            TextView textViewSecond = findViewById(id22);

                            JSONArray recipieArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(String.format("%s.%s", textViewMinute.getText().toString(), textViewSecond.getText().toString())); // Change this to the actual quantity
                            recipieList.put(String.valueOf(i), recipieArray);
                        }

                        // Create the main JSON object
                        JSONObject itemObject = new JSONObject()
                                .put("title", itemTitle)
                                .put("category", itemCategory)
                                .put("likeCnt", 0)
                                .put("writer", nickname.toString())
                                .put("totaltime", itemTime)
                                .put("servings",itemServings)
                                .put("ratings",itemRating)
                                .put("recipeIngredient", ingredientList)
                                .put("recipeStep", recipieList);

                        JSONObject recipieItemObject = new JSONObject()
                                .put(itemTitle.toString(), itemObject);
                        Log.d("logcat", recipieItemObject.toString());

                        //firebase로 데이터 전송
                        mDatabaseRef.child("Recipe").push().setValue(recipieItemObject);

                        Log.d("logcat", recipieItemObject.toString());
                        Log.d("logcat", "write_recipe_complete");
                        Toast.makeText(getApplicationContext(), "레시피가 작성되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Log.d("logcat", "itemServings is empty");
                        Toast.makeText(getApplicationContext(), "기준 인원을 작성해주세요.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("logcat", "itemCategory is empty");
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("logcat", "itemTitle is empty");
                Toast.makeText(getApplicationContext(), "레시피 이름을 작성하세요.",Toast.LENGTH_SHORT).show();
            }
    }
}