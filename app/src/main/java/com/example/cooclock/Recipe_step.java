package com.example.cooclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    public int currentStepIndex = 0;
    private ArrayList<recipeStepItem> items;

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
        Log.d(TAG, "ㅁㅁ" + currentStepIndex);

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
        return RecipeStepRoot;
    }

    public void updateStep() {
        if (!items.isEmpty()) {
            recipeStepItem item = items.get(currentStepIndex);
            Log.d(TAG, "update " + currentStepIndex);
            recipe_step_number.setText(String.valueOf(item.getNumber()));
            recipe_step_detail.setText(item.getDescription());
            recipe_step_minute.setText(String.valueOf(item.getMinute()));
            recipe_step_second.setText(String.valueOf(item.getSecond()));
            recipe_step_image.setImageResource(item.getResId());
        }
    }
}
