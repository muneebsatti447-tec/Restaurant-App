package com.example.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<FoodItem> foodList;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onEditClick(FoodItem item);
        void onDeleteClick(FoodItem item);
    }

    public FoodAdapter(Context context, List<FoodItem> foodList, OnItemClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_admin, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.tvItemName.setText(item.getName());
        holder.tvItemPrice.setText("Rs " + item.getPrice());

        holder.ivEdit.setOnClickListener(v -> listener.onEditClick(item));
        holder.ivDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemPrice;
        ImageView ivEdit, ivDelete;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
