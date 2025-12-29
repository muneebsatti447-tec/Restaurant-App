package com.example.restaurantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<FoodItem> menuItems;
    private Context context;

    public MenuAdapter(Context context, List<FoodItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        FoodItem currentItem = menuItems.get(position);

        holder.foodNameTextView.setText(currentItem.getName());
        holder.foodPriceTextView.setText(String.format("Rs. %.2f", currentItem.getPrice()));


        try {
            String base64Image = currentItem.getImage();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.foodImageView.setImageBitmap(decodedByte);
        } catch (Exception e) {
            holder.foodImageView.setImageResource(R.drawable.ic_launcher_background);
            e.printStackTrace();
        }

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager cartManager = CartManager.getInstance();
                cartManager.addItem(currentItem);
                Toast.makeText(context, currentItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView foodPriceTextView;
        Button addToCartButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
