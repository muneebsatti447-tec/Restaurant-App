package com.example.restaurantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvOrderStatus, tvOrderPrice, tvShippingAddress, tvOrderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            // Naye TextViews ko initialize karein
            tvShippingAddress = itemView.findViewById(R.id.tvShippingAddress);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvCustomerName.setText("Customer: " + (order.getCustomerName() != null ? order.getCustomerName() : "N/A"));
        holder.tvOrderStatus.setText("Status: " + order.getOrderStatus());
        if (order.getTotalPrice() != null) {
            holder.tvOrderPrice.setText(String.format(Locale.getDefault(), "Price: Rs %.2f", order.getTotalPrice()));
        } else {
            holder.tvOrderPrice.setText("Price: N/A");
        }

        if (order.getShippingAddress() != null && !order.getShippingAddress().isEmpty()) {
            holder.tvShippingAddress.setText("Address: " + order.getShippingAddress());
            holder.tvShippingAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvShippingAddress.setVisibility(View.GONE);
        }


        List<FoodItem> items = order.getItems();
        if (items != null && !items.isEmpty()) {
            StringBuilder itemsText = new StringBuilder("Items: ");
            for (int i = 0; i < items.size(); i++) {
                FoodItem item = items.get(i);
                if (item != null && item.getName() != null) {
                    itemsText.append(item.getName()).append(" x ").append(item.getQuantity());
                    if (i < items.size() - 1) {
                        itemsText.append(", ");
                    }
                }
            }
            holder.tvOrderItems.setText(itemsText.toString());
            holder.tvOrderItems.setVisibility(View.VISIBLE);
        } else {
            holder.tvOrderItems.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }
}
