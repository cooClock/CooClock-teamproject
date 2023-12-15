package com.example.cooclock;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class recipe_write_page extends AppCompatActivity {
    Uri uri;
    ImageView imageView;
    LinearLayout get_picture;

    int selectedPosition;
    String itemRecipieName;
    String itemCategory = NULL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_write_page);


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
                            if(itemCategory.equals(NULL)){
                                Toast.makeText(recipe_write_page.this, "카테고리가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(recipe_write_page.this, itemCategory.toString() + "이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show();
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

    public void add_ingredient(View v) {
        Log.d("logcat", "add_ingredient");
    };
    public void add_recipe(View v) {
        Log.d("logcat", "add_recipe");
    };
    public void write_recipe_complete(View v) {
        Log.d("logcat", "write_recipe_complete");
    };

}