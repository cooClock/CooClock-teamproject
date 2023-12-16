package com.example.cooclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasicRecipeListAdapter extends RecyclerView.Adapter<BasicRecipeListAdapter.BasicRecipeListViewHolder> {
    private static String TAG = "BasicRecipeListAdapter";
    static ArrayList<recipeItem> items;

    public interface OnItemClickListener{
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }

    public class BasicRecipeListViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipe_image;
        private TextView recipe_title;
        private TextView recipe_time;
        private TextView recipe_like;

        public BasicRecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_image = itemView.findViewById(R.id.recipe_item_image);
            recipe_title = itemView.findViewById(R.id.recipe_item_title);
            recipe_time = itemView.findViewById(R.id.recipe_item_time);
            recipe_like = itemView.findViewById(R.id.recipe_item_favorite);
        }
    }

    public BasicRecipeListAdapter(ArrayList<recipeItem> a_list) { items = a_list; }

    @Override
    public BasicRecipeListAdapter.BasicRecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new BasicRecipeListAdapter.BasicRecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicRecipeListAdapter.BasicRecipeListViewHolder holder, int position) {
        recipeItem item = items.get(position);
        holder.recipe_image.setImageResource(item.getResId());
        holder.recipe_like.setText(String.valueOf(item.getLikeCnt()));
        holder.recipe_time.setText(String.valueOf(item.getTotalTime()));
        holder.recipe_title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
