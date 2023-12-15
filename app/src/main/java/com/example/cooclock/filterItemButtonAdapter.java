package com.example.cooclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class filterItemButtonAdapter extends RecyclerView.Adapter<filterItemButtonAdapter.ButtonViewHolder> {

    private List<filterItemBtnModel> buttonList;

    public filterItemButtonAdapter(List<filterItemBtnModel> buttonList) {
        this.buttonList = buttonList;
    }

    @NonNull
    @Override
    public filterItemButtonAdapter.ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtering_result_btn, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        filterItemBtnModel buttonModel = buttonList.get(position);
        holder.button.setText(buttonModel.getButtonText());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.filter_item_btn);
        }
    }
}