package com.example.restaurantapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private TextView noOrdersTextView;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    private Button updateOrderStatusButton;
    private String recentPendingOrderId = null;
    private String currentUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        orderHistoryRecyclerView = findViewById(R.id.orderHistory);
        noOrdersTextView = findViewById(R.id.noOrdersTextView);
        updateOrderStatusButton = findViewById(R.id.updateOrderStatusButton);
        updateOrderStatusButton.setVisibility(View.GONE);

        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please login to view your orders", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUserId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(currentUserId);

        fetchOrderHistory();


        updateOrderStatusButton.setOnClickListener(v -> {
            if (recentPendingOrderId != null && !recentPendingOrderId.isEmpty()) {
                updateOrderStatus(recentPendingOrderId);
            } else {
                Toast.makeText(this, "No pending order to mark as delivered.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchOrderHistory() {
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, orderList);
        orderHistoryRecyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                recentPendingOrderId = null;

                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            String orderId = orderSnapshot.getKey();
                            order.setOrderId(orderId);
                            orderList.add(order);


                            if ("Pending".equals(order.getOrderStatus())) {
                                recentPendingOrderId = orderId;
                            }
                        }
                    }


                    if (recentPendingOrderId != null) {
                        updateOrderStatusButton.setVisibility(View.VISIBLE);
                    } else {
                        updateOrderStatusButton.setVisibility(View.GONE);
                    }

                    Collections.reverse(orderList);
                    adapter.notifyDataSetChanged();
                    noOrdersTextView.setVisibility(View.GONE);
                    orderHistoryRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noOrdersTextView.setVisibility(View.VISIBLE);
                    orderHistoryRecyclerView.setVisibility(View.GONE);
                    updateOrderStatusButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateOrderStatus(String orderId) {
        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(currentUserId)
                .child(orderId);


        orderRef.child("orderStatus").setValue("Delivered").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(OrderHistoryActivity.this, "Order marked as Delivered!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OrderHistoryActivity.this, "Failed to update status. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
