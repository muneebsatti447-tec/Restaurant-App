package com.example.restaurantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnItemDeleteListener {
        void onDeleteClick(int position);
    }

    private List<FoodItem> cartItems;
    private OnItemDeleteListener deleteListener;

    public CartAdapter(List<FoodItem> cartItems, OnItemDeleteListener deleteListener) {
        this.cartItems = cartItems;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodItem currentItem = cartItems.get(position);
        holder.itemName.setText(currentItem.getName());
        holder.itemPrice.setText(String.format("Rs. %.2f", currentItem.getPrice()));
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        Button deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.cartItemNameTextView);
            itemPrice = itemView.findViewById(R.id.cartItemPriceTextView);
            deleteButton = itemView.findViewById(R.id.deleteCartButton);
        }
    }
}
