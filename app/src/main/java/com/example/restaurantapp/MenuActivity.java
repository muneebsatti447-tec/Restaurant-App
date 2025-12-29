
package com.example.restaurantapp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<FoodItem> foodItemsList;
    private DatabaseReference menuDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuDatabaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");

        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodItemsList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, foodItemsList);
        menuRecyclerView.setAdapter(menuAdapter);

        checkAndLoadMenu();

        setupUIListeners();
    }

    private void setupUIListeners() {
        FloatingActionButton cartFab = findViewById(R.id.cartFab);
        cartFab.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });

        FloatingActionButton orderHistoryFab = findViewById(R.id.orderHistoryFab);
        orderHistoryFab.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnLogout2).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void checkAndLoadMenu() {
        menuDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loadMenuFromFirebase();
                } else {
                    uploadInitialMenuToFirebase();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuActivity.this, "Database check failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadInitialMenuToFirebase() {

        ArrayList<FoodItem> itemsToUpload = new ArrayList<>();
        itemsToUpload.add(new FoodItem("Pepperoni Pizza", 1300.00, convertDrawableToBase64(R.drawable.pizza)));
        itemsToUpload.add(new FoodItem("Crown Crust Pizza", 1200.00, convertDrawableToBase64(R.drawable.crown_crust_pizza)));
        itemsToUpload.add(new FoodItem("Chicken Biryani", 850.00, convertDrawableToBase64(R.drawable.biryani)));
        itemsToUpload.add(new FoodItem("Zinger Burger", 600.00, convertDrawableToBase64(R.drawable.burger)));
        itemsToUpload.add(new FoodItem("Shawarma", 350.00, convertDrawableToBase64(R.drawable.shawarma)));
        itemsToUpload.add(new FoodItem("Paratha Roll", 1300.00, convertDrawableToBase64(R.drawable.paratha_roll)));
        itemsToUpload.add(new FoodItem("French Fries", 350.00, convertDrawableToBase64(R.drawable.fries)));
        itemsToUpload.add(new FoodItem(" Pasta", 900.00, convertDrawableToBase64(R.drawable.pasta)));
        itemsToUpload.add(new FoodItem("Club Sandwich", 500.00, convertDrawableToBase64(R.drawable.sandwich)));
        itemsToUpload.add(new FoodItem(" Salad", 750.00, convertDrawableToBase64(R.drawable.salad)));
        itemsToUpload.add(new FoodItem("Chicken Roast", 1200.00, convertDrawableToBase64(R.drawable.chicken)));
        itemsToUpload.add(new FoodItem("Desi Dall", 800.00, convertDrawableToBase64(R.drawable.desi_dall)));
        itemsToUpload.add(new FoodItem("Chicken Karahi", 1800.00, convertDrawableToBase64(R.drawable.chicken_karahi)));
        itemsToUpload.add(new FoodItem("chicken_malai_boti", 1000.00, convertDrawableToBase64(R.drawable.chicken_malai_boti)));
        itemsToUpload.add(new FoodItem("Beef karahi", 2400.00, convertDrawableToBase64(R.drawable.beef_karahi)));
        itemsToUpload.add(new FoodItem("Beef malai boti", 1200.00, convertDrawableToBase64(R.drawable.beef_malai_boti)));
        itemsToUpload.add(new FoodItem(" Ice Cream", 400.00, convertDrawableToBase64(R.drawable.icecream)));


        for (FoodItem item : itemsToUpload) {
            String itemId = menuDatabaseReference.push().getKey();
            if (itemId != null) {
                menuDatabaseReference.child(itemId).setValue(item);
            }
        }
        loadMenuFromFirebase();
    }

    private String convertDrawableToBase64(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Quality 50% rakhi hai taake string choti bane
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadMenuFromFirebase() {
        menuDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodItemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    if (foodItem != null) {
                        foodItemsList.add(foodItem);
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MenuActivity.this, "Failed to load menu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
