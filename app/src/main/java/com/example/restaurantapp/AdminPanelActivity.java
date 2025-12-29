package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdminPanelActivity extends AppCompatActivity {

    private TextView tvTotalRevenue, tvTotalUsers, tvTotalOrders, tvPendingOrders;
    private DatabaseReference usersRef, ordersRef;
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    private Button btnManageMenu;
    private Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvPendingOrders = findViewById(R.id.tvMenuItems);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        fetchDashboardData();

        setupOrdersRecyclerView();
        fetchAllOrdersForList();
        btnManageMenu = findViewById(R.id.btnManageMenu);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, ManageMenuActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupOrdersRecyclerView() {
        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        rvOrders.setAdapter(orderAdapter);
    }

    private void fetchAllOrdersForList() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(orderSnapshot.getKey());
                            orderList.add(order);
                        }
                    }
                }
                Collections.sort(orderList, (o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load orders list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDashboardData() {

        usersRef.orderByChild("role").equalTo("customer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long userCount = snapshot.exists() ? snapshot.getChildrenCount() : 0;
                        tvTotalUsers.setText(String.valueOf(userCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminPanelActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalOrders = 0;
                long pendingOrders = 0;
                double totalRevenue = 0.0;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        totalOrders++;

                        String status = orderSnapshot.child("orderStatus").getValue(String.class);

                        if (status != null && status.equalsIgnoreCase("Pending")) {
                            pendingOrders++;
                        }

                        if (status != null && status.equalsIgnoreCase("Delivered")) {
                            Double price = orderSnapshot.child("totalPrice").getValue(Double.class);
                            if (price != null) {
                                totalRevenue += price;
                            }
                        }
                    }
                }

                tvTotalOrders.setText(String.valueOf(totalOrders));
                tvTotalRevenue.setText("Rs " + String.format(Locale.getDefault(), "%.2f", totalRevenue));
                tvPendingOrders.setText(String.valueOf(pendingOrders));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load order data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
