package com.example.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final List<Order> orderList;
    private final Context context;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order currentOrder = orderList.get(position);
        holder.orderIdTextView.setText("Order ID: " + currentOrder.getOrderId());
        holder.orderTotalTextView.setText(String.format(Locale.getDefault(), "Total: Rs. %.2f", currentOrder.getTotalPrice()));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy 'at' hh:mm a", Locale.getDefault());
            String date = sdf.format(new Date(currentOrder.getTimestamp()));
            holder.orderDateTextView.setText("Date: " + date);
        } catch (Exception e) {
            holder.orderDateTextView.setText("Date: N/A");
        }

        List<FoodItem> items = currentOrder.getItems();
        if (items != null && !items.isEmpty()) {
            StringBuilder itemsText = new StringBuilder("Items:\n");
            for (FoodItem item : items) {
                itemsText.append(String.format(Locale.getDefault(), "• %s (x%d)\n", item.getName(), item.getQuantity()));
            }
            holder.orderItemsTextView.setText(itemsText.toString().trim());
        } else {
            holder.orderItemsTextView.setText("Items: Not available");
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, orderDateTextView, orderTotalTextView, orderItemsTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
            orderItemsTextView = itemView.findViewById(R.id.orderItemsTextView);
        }
    }
}
