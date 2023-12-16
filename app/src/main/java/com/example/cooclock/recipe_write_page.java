package com.example.cooclock;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class recipe_write_page extends AppCompatActivity {
    Uri uri;
    ImageView imageView;
    LinearLayout get_picture;

    int selectedPosition;
    String itemTitle;
    String itemCategory;
    int itemTime;
    int itemIngridientCount = 1;
    int itemRecipieCount = 1;

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
        idArray_itemIngridientAmount.add(R.id.input_ingredient_weight1);
        idArray_itemRecipieTimeMinute.add(R.id.input_recipieTime_minute);
        idArray_itemRecipieTimeSecond.add(R.id.input_recipieTime_second);

        get_picture = findViewById(R.id.input_picture);
        imageView = findViewById(R.id.food_imageview);
        get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });

        TextView categorySelectText = (TextView) findViewById(R.id.category_select_layout_text);
        LinearLayout categorySelect = (LinearLayout) findViewById(R.id.category_select_layout);
        categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(recipe_write_page.this, R.style.MyAlertDialogStyle);
                dlg.setTitle("카테고리 선택"); //제목
                final String[] versionArray = new String[] {"한식","일식","중식","양식","분식","간식","면류","반찬류","소주상","맥주상","대접용","데이트","보양식","도시락","아이용","이유식"};

                selectedPosition = 0; // Default selected position

                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPosition = which; // Update the selected position
                        categorySelectText.setText(versionArray[which]);
                    }
                });

                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
//                        Toast.makeText(recipe_write_page.this,categorySelectText.getText()+"가 선택되었습니다.",Toast.LENGTH_SHORT).show();
                        if (selectedPosition == 0) {
                            // No item selected, handle this case
                            if(!itemCategory.isEmpty()){
                                Toast.makeText(recipe_write_page.this, itemCategory.toString() + "이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(recipe_write_page.this, "카테고리가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // Item selected, handle it
                            itemCategory = categorySelectText.getText().toString();
                            Toast.makeText(recipe_write_page.this, categorySelectText.getText() + "이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlg.show();
            }
        });
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
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    // Helper method to create an ImageView for buttons
    private ImageView createButton(int drawableId, View.OnClickListener listener) {
        ImageView button = new ImageView(this);
        button.setLayoutParams(new ViewGroup.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30), // Width
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30)  // Height
        ));
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        button.setImageResource(drawableId);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setOnClickListener(listener);
        return button;
    }
    // Helper method to create a TextView
    private TextView createTextView(String text, int textSize) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20, 0, 5, 0);
        textView.setTextSize(textSize);
//        textView.setTypeface(Typeface.create("notosanskr_medium", Typeface.NORMAL));
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_light));
        textView.setIncludeFontPadding(false);
        return textView;
    }

    // 재료 용량 - or + button listener
    public void sub_ingredient_weight(View v){
        TextView textView = (TextView) findViewById(R.id.input_ingredient_weight1);
        textView.setText(String.format("%.1f",Float.parseFloat(textView.getText().toString())-0.1));
    };
    public void add_ingredient_weight(View v){
        TextView textView = (TextView) findViewById(R.id.input_ingredient_weight1);
        textView.setText(String.format("%.1f",Float.parseFloat(textView.getText().toString())+0.1));
    };
    // 재료 용량 - or + button listener
    public void sub_recipieStepTimer(View v){
        TextView secondTextView = (TextView) findViewById(R.id.input_recipieTime_second);
        TextView minuteTextView = (TextView) findViewById(R.id.input_recipieTime_minute);

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
    };
    public void add_recipieStepTimer(View v){
        TextView secondTextView = (TextView) findViewById(R.id.input_recipieTime_second);
        TextView minuteTextView = (TextView) findViewById(R.id.input_recipieTime_minute);

        int second = Integer.parseInt(secondTextView.getText().toString()) + 10;
        if(second >= 60){
            int minute = Integer.parseInt(minuteTextView.getText().toString())+1;
            minuteTextView.setText(String.valueOf(minute));
            secondTextView.setText("00");
        }else {
            secondTextView.setText(String.valueOf(second));
        }
    };

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
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin5)); // Adjust the margin
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


        // 재료 양 입력받을 -> LinearLayout 생성
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

//        // - 버튼
//        ImageView subButton = createButton(R.drawable.minus_btn, this::sub_ingredient_weight);
//
//        // 수량 텍스트뷰
//        TextView quantityTextView = createTextView("0.1", 25);
//
//        // kg 텍스트뷰
//        TextView kgTextView = createTextView("kg", 17);
//
//        // + 버튼
//        ImageView addButton = createButton(R.drawable.plus_btn, this::add_ingredient_weight);

        // - 버튼

        // 수량 텍스트뷰
        TextView quantityTextView = new TextView(this);

        quantityTextView.setId(View.generateViewId());
        idArray_itemIngridientAmount.add(quantityTextView.getId());

        quantityTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        quantityTextView.setGravity(Gravity.CENTER);
        quantityTextView.setPadding(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin20),
                getResources().getDimensionPixelSize(R.dimen.dimeMargin10),
                getResources().getDimensionPixelSize(R.dimen.dimeMargin5),
                getResources().getDimensionPixelSize(R.dimen.dimeMargin10));
        quantityTextView.setTextSize(25);
        quantityTextView.setIncludeFontPadding(false);
        quantityTextView.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_light));
        quantityTextView.setText("0.1");


        // kg 텍스트뷰
        TextView kgTextView = new TextView(this);
        kgTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        kgTextView.setGravity(Gravity.CENTER);
        kgTextView.setTextSize(17);
        kgTextView.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_light));
        kgTextView.setTextColor(Color.BLACK);
        kgTextView.setText("kg");
        kgTextView.setIncludeFontPadding(false);
        kgTextView.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.dimeMargin20), 0);

        // - 버튼
        ImageView subButton = new ImageView(this);
        subButton.setLayoutParams(new ViewGroup.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30), // Width
                getResources().getDimensionPixelSize(R.dimen.dimeMargin30)  // Height
        ));
        subButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        subButton.setImageResource(R.drawable.minus_btn);
        subButton.setBackgroundColor(Color.TRANSPARENT);
        subButton.setOnClickListener(this::sub_ingredient_weight);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // - 버튼 클릭시
                quantityTextView.setText(String.format("%.1f",Float.parseFloat(quantityTextView.getText().toString())-0.1));
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
        addButton.setOnClickListener(this::add_ingredient_weight);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // - 버튼 클릭시
                quantityTextView.setText(String.format("%.1f",Float.parseFloat(quantityTextView.getText().toString())+0.1));
            }
        });

        // 레이아웃에 추가
        linearLayout.addView(subButton);
        linearLayout.addView(quantityTextView);
        linearLayout.addView(kgTextView);
        linearLayout.addView(addButton);

        // Add the new LinearLayout to the parent layout
        layoutIngredient.addView(linearLayout);
    };

//    레시피 추가 button
    public void add_recipe(View v) {
        itemRecipieCount++;

        // Assuming you have a reference to your parent layout named layout_recipie
        LinearLayout layoutRecipie = findViewById(R.id.layout_recipie);

        // Create a new TextView
        TextView newTextView = new TextView(this);
        LinearLayout.LayoutParams newTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
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
    };


    public void write_recipe_complete(View v) {
        Log.d("logcat", "write_recipe_complete");
        EditText TitleEditText = (EditText) findViewById(R.id.itemTitle);
        Slider TimeSlider = (Slider) findViewById(R.id.slider);
        itemTitle = TitleEditText.getText().toString();
        itemTime = (int) TimeSlider.getValue();

            // Check if the value is not empty
            if (!itemTitle.isEmpty()) {
                if(!itemCategory.isEmpty()){
                    //TODO : ingridient list & recipie List 다 채워져있다고 생각함. 예외처리 추후 적용

                    try{
                        JSONObject ingredientList = new JSONObject();
                        for (int i = 0; i < idArray_itemIngridientET.size(); i++) {
                            // For each ingredient, create a JSONArray and put it in the ingredientList JSONObject
                            int id11 = idArray_itemIngridientET.get(i);
                            EditText editText = (EditText) findViewById(id11);

                            int id12 = idArray_itemIngridientAmount.get(i);
                            TextView textView = (TextView) findViewById(id12);

                            JSONArray ingredientArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(textView.getText().toString()); // Change this to the actual quantity
                            ingredientList.put(String.valueOf(i), ingredientArray);
                        }

                        JSONObject recipieList = new JSONObject();
                        for (int i = 0; i < idArray_itemRecipieET.size(); i++) {
                            // For each recipie, create a JSONArray and put it in the recipieList JSONObject
                            int id2 = idArray_itemRecipieET.get(i);
                            EditText editText = (EditText) findViewById(id2);

                            int id21 = idArray_itemRecipieTimeMinute.get(i);
                            TextView textViewMinute = (TextView) findViewById(id21);
                            int id22 = idArray_itemRecipieTimeSecond.get(i);
                            TextView textViewSecond = (TextView) findViewById(id22);

                            JSONArray recipieArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(String.format("%s.%s",textViewMinute.getText().toString(),textViewSecond.getText().toString())); // Change this to the actual quantity
                            recipieList.put(String.valueOf(i), recipieArray);
                        }

//                        Log.d("logcat", "ingridient"+ingredientList.toString());
//                        Log.d("logcat", "recipie"+recipieList.toString());
                        // Create the main JSON object
                        JSONObject itemObject = new JSONObject()
                                .put("title", itemTitle)
                                .put("category", itemCategory)
                                .put("time", itemTime)
                                .put("ingredientList", ingredientList)
                                .put("recipieList",recipieList);
                        Log.d("logcat",itemObject.toString());
                        finish();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("logcat", "itemCategory is empty");
                }
            } else {
                Log.d("logcat", "itemTitle is empty");
            }
            Toast.makeText(getApplicationContext(), "레시피를 올바르게 작성해주세요.",Toast.LENGTH_SHORT).show();
    };

//    // Helper method to create an indexed JSON object
//    private static JSONObject createIndexedJSONObject(JSONArray jsonArray) throws JSONException {
//        JSONObject indexedObject = new JSONObject();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                indexedObject.put(String.valueOf(i + 1), jsonArray.get(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return indexedObject;
//    }

}