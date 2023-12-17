package com.example.cooclock;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListViewHolder> {
    private static String TAG = "IngredientAdapter";
    ArrayList<ingredientItem> items;

    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }


    // 요리 단계 리스트리사이클러 뷰를 위한 뷰 홀더
    public class IngredientListViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ingredient_color;
        private TextView ingredient_name;
        private TextView ingredient_weight;
        private ImageView ingredient_sub;
        private  ImageView ingredient_add;

        public IngredientListViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_color = itemView.findViewById(R.id.my_refrigerator_ingredient_color);
            ingredient_name = itemView.findViewById(R.id.my_refrigerator_ingredient_name);
            ingredient_weight = itemView.findViewById(R.id.my_refrigerator_ingredient_weight);
            ingredient_sub = itemView.findViewById(R.id.my_refrigerator_ingredient_sub);
            ingredient_add = itemView.findViewById(R.id.my_refrigerator_ingredient_add);

            // 감소 버튼
            ingredient_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    Log.d(TAG,""+position);
                    if (position != RecyclerView.NO_POSITION && position < items.size()) {
                        ingredientItem item = items.get(position);
                        item.setWeight(String.format("%.1f",item.getWeightF()-0.1));
                        notifyDataSetChanged();
                    }
                }
            });

            // 증가 버튼
            ingredient_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    Log.d(TAG,""+position);
                    if (position != RecyclerView.NO_POSITION && position < items.size()) {
                        ingredientItem item = items.get(position);
                        item.setWeight(String.format("%.1f",item.getWeightF()+0.1));
                        notifyDataSetChanged();
                    }
                }
            });

        }
    }
    public IngredientListAdapter(ArrayList<ingredientItem> a_list){
        items = a_list;
    }

    public IngredientListAdapter.IngredientListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_refrigerator_item, parent, false);
        return new IngredientListAdapter.IngredientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.IngredientListViewHolder holder, int position) {
        ingredientItem item = items.get(position);
        holder.ingredient_color.setImageResource(getColor(item.getKind()));
        holder.ingredient_name.setText(item.getName());
        holder.ingredient_weight.setText(String.valueOf(item.getWeight()));
    }

    public int getColor(String kind){
        if (kind == null)
            return R.color.black;
        switch (kind) {
            case "과일류":
            case "채소류":
                return R.color.fruite;
            case "육류":
                return R.color.meat;
            case "어패류":
                return R.color.fish;
            case "곡류":
                return R.color.grain;
            case "난류":
                return R.color.eggs;
            case "유제품류":
                return R.color.milk;
            case "기타":
                return R.color.etc;
            default:
                return R.color.black;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
