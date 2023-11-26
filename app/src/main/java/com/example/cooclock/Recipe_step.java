package com.example.cooclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class Recipe_step extends Fragment {
    private static String TAG = "Recipe_Step";

    private TextView recipe_step_number;
    private TextView recipe_step_detail;
    private TextView recipe_step_minute;
    private TextView recipe_step_second;
    private ImageView recipe_step_image;
    private ImageButton recipe_step_prev;
    private ImageButton recipe_step_next;
    private TextView recipe_tot_number;
    private ImageButton recipe_timer_start;
    private ImageButton recipe_timer_pause;
    private ImageButton recipe_timer_reset;
    private LinearProgressIndicator recipe_step_progess;
    public int currentStepIndex = 0;
    private ArrayList<recipeStepItem> items;
    private boolean firstStart;
    private boolean timerRunning;
    private long time;
    private long tmpTime;
    private long init_time;
    private int i=0;

    private CountDownTimer countDownTimer;


    public Recipe_step() {
        items = new ArrayList<recipeStepItem>();
    }

    public Recipe_step(ArrayList<recipeStepItem> items) {
        this.items = items;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RecipeStepRoot = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        recipe_step_number = RecipeStepRoot.findViewById(R.id.recipe_step_number);
        recipe_step_detail = RecipeStepRoot.findViewById(R.id.recipe_step_detail);
        recipe_step_minute = RecipeStepRoot.findViewById(R.id.timer_minute);
        recipe_step_second = RecipeStepRoot.findViewById(R.id.timer_second);
        recipe_step_image = RecipeStepRoot.findViewById(R.id.recipe_step_image);
        recipe_step_prev = RecipeStepRoot.findViewById(R.id.recipe_step_prev);
        recipe_step_next = RecipeStepRoot.findViewById(R.id.recipe_step_next);
        recipe_tot_number = RecipeStepRoot.findViewById(R.id.recipe_tot_number);
        recipe_timer_start = RecipeStepRoot.findViewById(R.id.timer_start_btn);
        recipe_timer_pause = RecipeStepRoot.findViewById(R.id.timer_pause_btn);
        recipe_timer_reset = RecipeStepRoot.findViewById(R.id.timer_reset_btn);
        recipe_step_progess = RecipeStepRoot.findViewById(R.id.recipe_progress_bar);
        updateStep();

        recipe_step_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStepIndex++;
                Log.d(TAG, "" + currentStepIndex);
                if (currentStepIndex >= items.size())
                    currentStepIndex--;
                updateStep();
            }
        });

        recipe_step_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStepIndex--;
                if (currentStepIndex < 0)
                    currentStepIndex++;
                updateStep();
            }
        });

        recipe_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstStart = true;
                startTimer();
            }
        });

        recipe_timer_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        recipe_timer_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstStart = true;
                resetTimer();
            }
        });
        return RecipeStepRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(countDownTimer != null) countDownTimer.cancel();
    }

    public void updateStep() {
        if (!items.isEmpty()) {
            recipeStepItem item = items.get(currentStepIndex);
            Log.d(TAG, "update " + currentStepIndex);
            recipe_step_number.setText(String.valueOf(item.getNumber()));
            recipe_step_detail.setText(item.getDescription());

            if (item.getMinute() < 10) recipe_step_minute.setText("0"+item.getMinute());
            else recipe_step_minute.setText(String.valueOf(item.getMinute()));
            if (item.getSecond() < 10) recipe_step_second.setText("0"+item.getSecond());
            else recipe_step_second.setText(String.valueOf(item.getSecond()));

            recipe_step_image.setImageResource(item.getResId());
            recipe_tot_number.setText(String.valueOf(items.size()));
            if (countDownTimer != null) {
                countDownTimer.cancel();
                firstStart = true;
                timerRunning = false;
                recipe_timer_start.setClickable(true);
                recipe_timer_pause.setClickable(false);
                recipe_step_progess.setProgress(0);
            }
        }
    }

    /*
    타이머 구현 부분
     */
    private void pauseTimer() {
        countDownTimer.cancel();
        recipe_timer_start.setClickable(true);
        recipe_timer_pause.setClickable(false);
        timerRunning = true;
    }
    private void startTimer(){
        int minute = items.get(currentStepIndex).getMinute();
        int second = items.get(currentStepIndex).getSecond();

        if(firstStart && !timerRunning){
            init_time =  (minute* 60L +second)*1000;
            time = init_time;
        }
        else if(timerRunning) {
            time = tmpTime;
        }
        else {
            time = tmpTime;
        }
        Log.d(TAG, "timer " + String.valueOf(time));

        recipe_step_progess.setProgress(i);

        countDownTimer = new CountDownTimer(time,1000 ) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, String.valueOf(tmpTime));
                i++;
                recipe_step_progess.setProgress((int) ((int)i*100/(time/1000L)));
                tmpTime = millisUntilFinished;
                updateTimer(tmpTime);
            }
            @Override
            public void onFinish() {
                i++;
                recipe_step_progess.setProgress(100);

            }
        }.start();
        firstStart = false;
        timerRunning = true;
        recipe_timer_start.setClickable(false);
        recipe_timer_pause.setClickable(true);
    }

    private void resetTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        int minute = items.get(currentStepIndex).getMinute();
        int second = items.get(currentStepIndex).getSecond();
        updateTimer((minute* 60L +second)*1000);
        firstStart = true;
        recipe_timer_start.setClickable(true);
        recipe_timer_pause.setClickable(false);
        recipe_step_progess.setProgress(0);
    }
    private void updateTimer(long tmpTime){
        int minutes = (int) tmpTime % 3600000 / 60000;
        int seconds = (int) tmpTime % 3600000 % 60000 / 1000;
        if(minutes<10) recipe_step_minute.setText("0"+ minutes);
        else recipe_step_minute.setText(String.valueOf(minutes));
        if(seconds<10) recipe_step_second.setText("0"+ seconds);
        else recipe_step_second.setText(String.valueOf(seconds));
    }
}
