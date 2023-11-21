package com.example.cooclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//public class IngredientButtonAdapter {
//}
//버튼 어댑터 생성
//public class IngredientButtonAdapter extends RecyclerView.Adapter<IngredientButtonAdapter.ButtonViewHolder> {
//
//    private List<ingredientBtnModel> buttonList;
//
//    public IngredientButtonAdapter(List<ingredientBtnModel> buttonList) {
//        this.buttonList = buttonList;
//    }
//
//    @NonNull
//    @Override
//    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_ingredient_btn, parent, false);
//        return new ButtonViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
//        ingredientBtnModel buttonModel = buttonList.get(position);
//        holder.button.setText(buttonModel.getButtonText());
//    }
//
//    @Override
//    public int getItemCount() {
//        return buttonList.size();
//    }
//
//    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
//        Button button;
//
//        public ButtonViewHolder(@NonNull View itemView) {
//            super(itemView);
//            button = itemView.findViewById(R.id.ingredient_btn);
//        }
//    }
//}