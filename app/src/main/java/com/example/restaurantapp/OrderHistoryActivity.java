package com.example.restaurantapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderHistoryRecyclerView = findViewById(R.id.orderHistory);
        noOrdersTextView = findViewById(R.id.noOrdersTextView);
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please login to view your orders", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, orderList);
        orderHistoryRecyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(orderSnapshot.getKey());
                            orderList.add(order);
                        }
                    }
                    Collections.reverse(orderList);
                    adapter.notifyDataSetChanged();
                    noOrdersTextView.setVisibility(View.GONE);
                    orderHistoryRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noOrdersTextView.setVisibility(View.VISIBLE);
                    orderHistoryRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
