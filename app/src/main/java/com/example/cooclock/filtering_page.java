package com.example.cooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class filtering_page extends AppCompatActivity {
    private Button cookTime1Btn;
    private Button cookTime2Btn;
    private Button cookTime3Btn;

    private boolean isButton1On = false;
    private boolean isButton2On = false;
    private boolean isButton3On = false;

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ArrayList<FriendItem> mfriendItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering_page);

        cookTime1Btn = findViewById(R.id.cookTime1);
        cookTime2Btn = findViewById(R.id.cookTime2);
        cookTime3Btn = findViewById(R.id.cookTime3);

        View.OnClickListener commonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(v);
            }
        };

        // Set the common click listener for each button
        cookTime1Btn.setOnClickListener(commonClickListener);
        cookTime2Btn.setOnClickListener(commonClickListener);
        cookTime3Btn.setOnClickListener(commonClickListener);


        mRecyclerView = (RecyclerView) findViewById(R.id.ingredient_btn_recyclerView);

        /* initiate adapter */
        mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        /* adapt data */
        mfriendItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            if(i%2==0)
                mfriendItems.add(new FriendItem(R.drawable.profile_off,i+"번째 사람",i+"번째 상태메시지"));
            else
                mfriendItems.add(new FriendItem(R.drawable.profile_on,i+"번째 사람",i+"번째 상태메시지"));

        }
        mRecyclerAdapter.setFriendList(mfriendItems);

//        List<ingredientBtnModel> buttonList = new ArrayList<>();
//        for (int i = 1; i <= 16; i++) {
//            buttonList.add(new ingredientBtnModel("Button " + i));
//            Log.d("logcat","Button"+i);
//        }
//        Log.d("logcat","this is :: "+ buttonList.toString());
//
//        RecyclerView recyclerView = findViewById(R.id.ingredient_btn_recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
//
//        IngredientButtonAdapter buttonAdapter = new IngredientButtonAdapter(buttonList);
//        recyclerView.setAdapter(buttonAdapter);
    }

    public void handleButtonClick(View view) {
        Button clickedButton = (Button) view;

        // Reset all buttons to off state
        cookTime1Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime2Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
        cookTime3Btn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));

        // Determine which button was clicked
        switch (clickedButton.getId()) {
            case R.id.cookTime1:
                isButton1On = !isButton1On;
                isButton2On = false;
                isButton3On = false;
                break;
            case R.id.cookTime2:
                isButton2On = !isButton2On;
                isButton1On = false;
                isButton3On = false;
                break;
            case R.id.cookTime3:
                isButton3On = !isButton3On;
                isButton1On = false;
                isButton2On = false;
                break;
        }

        // Set the background color based on the button state
        if (isButton1On) {
            cookTime1Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime1Btn.getText().toString());
        }
        if (isButton2On) {
            cookTime2Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime2Btn.getText().toString());
        }
        if (isButton3On) {
            cookTime3Btn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", cookTime3Btn.getText().toString());
        }
    }

    public void btnClicked(View view){
        Button clickedBtn = (Button) view;

        // Check if the button has a tag indicating its state
        Boolean isButtonOn = (Boolean) clickedBtn.getTag();

        // Toggle the button state
        if (isButtonOn == null || isButtonOn) {
            // Button is off, set it to on
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.buttonOnColor));
            Log.d("logcat", String.valueOf(isButtonOn)+clickedBtn.getText().toString());
            clickedBtn.setTag(false);
        } else {
            // Button is already on, set it to off
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.buttonOffColor));
            clickedBtn.setTag(true);
        }
    }

    /*
    나의 냉장고 재료 btn
    */
    //버튼 모델을 나타내는 클래스
//    public static class ingredientBtnModel {
//        private String buttonText;
//
//        public ButtonModel(String buttonText) {
//            this.buttonText = buttonText;
//        }
//
//        public String getButtonText() {
//            return buttonText;
//        }
//
//        public void setButtonText(String buttonText) {
//            this.buttonText = buttonText;
//        }
//    }
//    //버튼 어댑터 생성
//    public static class IngredientButtonAdapter extends RecyclerView.Adapter<IngredientButtonAdapter.ButtonViewHolder> {
//
//        private List<ingredientBtnModel> buttonList;
//
//        public IngredientButtonAdapter(List<ingredientBtnModel> buttonList) {
//            this.buttonList = buttonList;
//        }
//
//        @NonNull
//        @Override
//        public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_ingredient_btn, parent, false);
//            return new ButtonViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
//            ingredientBtnModel buttonModel = buttonList.get(position);
//            holder.button.setText(buttonModel.getButtonText());
//        }
//
//        @Override
//        public int getItemCount() {
//            return buttonList.size();
//        }
//
//        public static class ButtonViewHolder extends RecyclerView.ViewHolder {
//            Button button;
//
//            public ButtonViewHolder(@NonNull View itemView) {
//                super(itemView);
//                button = itemView.findViewById(R.id.ingredient_btn);
//             }
//        }
//    }
}