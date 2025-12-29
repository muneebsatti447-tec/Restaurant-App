package com.example.restaurantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageMenuAdapter extends RecyclerView.Adapter<ManageMenuAdapter.ViewHolder> {

    private final Context context;
    private final List<FoodItem> foodItems;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(FoodItem item);
        void onDeleteClick(FoodItem item, int position);
    }

    public ManageMenuAdapter(Context context, List<FoodItem> foodItems, OnItemClickListener listener) {
        this.context = context;
        this.foodItems = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_manage_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem currentItem = foodItems.get(position);

        holder.foodNameTextView.setText(currentItem.getName());
        holder.foodPriceTextView.setText(String.format("Rs. %.2f", currentItem.getPrice()));

        try {
            String base64Image = currentItem.getImage();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.foodImageView.setImageBitmap(decodedByte);
        } catch (Exception e) {

            holder.foodImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(currentItem);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(currentItem, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView foodPriceTextView;
        ImageButton editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
