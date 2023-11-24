package com.example.cooclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeStepListCustomAdapter extends RecyclerView.Adapter<RecipeStepListCustomAdapter.RecipeStepListCustomViewHolder> {
    ArrayList<recipeStepItem> items;


    // 요리 단계 리스트리사이클러 뷰를 위한 뷰 홀더
    public static class RecipeStepListCustomViewHolder extends RecyclerView.ViewHolder {
        private TextView recipe_step_number;
        private TextView recipe_step_detail;
        private TextView recipe_step_minute;
        private TextView recipe_step_second;
        private ImageView recipe_step_image;
        private ImageButton recipe_step_prev;
        private  ImageButton recipe_step_next;

        public RecipeStepListCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_step_number = itemView.findViewById(R.id.recipe_step_number);
            recipe_step_detail = itemView.findViewById(R.id.recipe_step_detail);
            recipe_step_minute = itemView.findViewById(R.id.timer_minute);
            recipe_step_second = itemView.findViewById(R.id.timer_second);
            recipe_step_image = itemView.findViewById(R.id.recipe_step_image);
            recipe_step_next = itemView.findViewById(R.id.recipe_step_next);
            recipe_step_prev = itemView.findViewById(R.id.recipe_step_prev);


            // 다음 단계로 넘어가는 버튼
            recipe_step_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){

                    }

                }
            });

            // 이전 단계로 넘어가는 버튼
            recipe_step_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });

        }
    }
    public RecipeStepListCustomAdapter(ArrayList<recipeStepItem> a_list){
        items = a_list;
    }

    public RecipeStepListCustomAdapter.RecipeStepListCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepListCustomAdapter.RecipeStepListCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepListCustomAdapter.RecipeStepListCustomViewHolder holder, int position) {
        recipeStepItem item = items.get(position);
        holder.recipe_step_number.setText(String.valueOf(item.getNumber()));
        holder.recipe_step_detail.setText(item.getDescription());
        holder.recipe_step_minute.setText(String.valueOf(item.getMinute()));
        holder.recipe_step_second.setText(String.valueOf(item.getSecond()));
        holder.recipe_step_image.setImageResource(item.getResId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
