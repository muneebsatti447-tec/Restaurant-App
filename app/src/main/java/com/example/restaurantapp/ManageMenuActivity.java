package com.example.restaurantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageMenuAdapter adapter;
    private List<FoodItem> foodItemsList;
    private DatabaseReference menuDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        recyclerView = findViewById(R.id.manageMenuRecyclerView);
        FloatingActionButton fabAddItem = findViewById(R.id.fab_add_item);
        menuDatabaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");
        setupRecyclerView();
        loadMenuFromFirebase();
        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        foodItemsList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ManageMenuAdapter(this, foodItemsList, new ManageMenuAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(FoodItem item) {
                Intent intent = new Intent(ManageMenuActivity.this, EditItemActivity.class);
                intent.putExtra("ITEM_ID", item.getItemId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(FoodItem item, int position) {
                showDeleteConfirmationDialog(item, position);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadMenuFromFirebase() {
        menuDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodItemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    if (foodItem != null) {
                        foodItem.setItemId(snapshot.getKey());
                        foodItemsList.add(foodItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageMenuActivity.this, "Failed to load menu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(FoodItem item, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete '" + item.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteItemFromFirebase(item, position);
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_delete)
                .show();
    }

    private void deleteItemFromFirebase(FoodItem item, int position) {
        if (item.getItemId() == null || item.getItemId().isEmpty()) {
            Toast.makeText(this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }


        menuDatabaseReference.child(item.getItemId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ManageMenuActivity.this, item.getName() + " deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManageMenuActivity.this, "Failed to delete item.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
