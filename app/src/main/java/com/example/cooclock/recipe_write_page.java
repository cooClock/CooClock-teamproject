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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
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

import java.io.FileNotFoundException;
import java.io.IOException;

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
    int id_itemIngridientEditText;
    int id_itemRecipieEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write_page);

        id_itemIngridientEditText = R.id.itemIngridient1;
        id_itemRecipieEditText = R.id.itemRecipie1;

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

    // 재료 추가 button
    public void add_ingredient(View v) {
        Log.d("logcat", "add_ingredient");
        itemIngridientCount++;

        // Assuming you have a reference to your parent layout named layout_recipie
        LinearLayout layoutRecipie = findViewById(R.id.layout_ingridient);


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
        newEditText.setId(R.id.itemIngridient1+itemIngridientCount); //TODO : 이렇게 써도 맞는가 ㅋㅋ 처맞는가?
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
        newRecipieLayout.addView(newEditText);

        // Add the new LinearLayout to the parent layout
        layoutRecipie.addView(newRecipieLayout);
    };

//    레시피 추가 button
    public void add_recipe(View v) {
        itemRecipieCount++;
        Log.d("logcat", "add_recipe");

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
        newTextView.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
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
        newEditText.setId(R.id.itemRecipie1+itemRecipieCount); //TODO : 이렇게 써도 되는게 맞는가 ????
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

    };
    public void write_recipe_complete(View v) {
        Log.d("logcat", "write_recipe_complete");

        EditText TitleEditText = (EditText) findViewById(R.id.itemTitle);
        Slider TimeSlider = (Slider) findViewById(R.id.slider);
        try {
            itemTitle = TitleEditText.getText().toString();
            itemTime = (int) TimeSlider.getValue();
            // Check if the value is not empty
            if (!itemTitle.isEmpty()) {
//                Log.d("logcat", "itemTitle: " + itemTitle);
                if(!itemCategory.isEmpty()){
                    //TODO : ingridient list & recipieList 다 채워져있다고 생각함. 예외처리 추후 적용
                    Log.d("logcat","id:"+R.id.itemIngridient1);
                    Log.d("logcat","id:"+R.id.itemRecipie1);

                    Log.d("logcat","id:"+id_itemIngridientEditText);
                    Log.d("logcat","id:"+id_itemRecipieEditText);
                    try{
                        JSONObject ingredientList = new JSONObject();
                        for (int i = 1; i <= itemIngridientCount; i++) {
                            // For each ingredient, create a JSONArray and put it in the ingredientList JSONObject
                            int id1 = id_itemIngridientEditText+i;
                            EditText editText = (EditText) findViewById(id1);
                            JSONArray ingredientArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(i * 0.1); // Change this to the actual quantity
                            ingredientList.put(String.valueOf(i), ingredientArray);
                        }

                        JSONObject recipieList = new JSONObject();
                        for (int i = 1; i <= itemRecipieCount; i++) {
                            // For each recipie, create a JSONArray and put it in the recipieList JSONObject
                            int id2 = id_itemRecipieEditText+i;
                            EditText editText = (EditText) findViewById(id2);
                            JSONArray recipieArray = new JSONArray()
                                    .put(editText.getText().toString())
                                    .put(i * 0.1); // Change this to the actual quantity
                            recipieList.put(String.valueOf(i), recipieArray);
                        }

                        // Create the main JSON object
                        JSONObject itemObject = new JSONObject()
                                .put("title", itemTitle)
                                .put("category", itemCategory)
                                .put("time", itemTime)
                                .put("ingredientList", ingredientList)
                                .put("recipieList",recipieList);
                        Log.d("logcat",itemObject.toString());
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("logcat", "itemCategory is empty");
                }
            } else {
                Log.d("logcat", "itemTitle is empty");
            }
        } catch (Exception e) {
            // Handle any exception that might occur
            Log.e("logcat", "Error getting itemTitle: " + e.getMessage());
        }

//        finish();
    };

    // Helper method to create an indexed JSON object
    private static JSONObject createIndexedJSONObject(JSONArray jsonArray) throws JSONException {
        JSONObject indexedObject = new JSONObject();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                indexedObject.put(String.valueOf(i + 1), jsonArray.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return indexedObject;
    }

}