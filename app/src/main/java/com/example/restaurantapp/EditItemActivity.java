package com.example.restaurantapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity {

    private ImageView ivImagePreview;
    private TextInputEditText etItemName, etItemPrice;
    private Button btnSaveItem;
    private TextView tvTitle;

    private DatabaseReference menuDatabaseReference;
    private String itemId;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivImagePreview.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ivImagePreview = findViewById(R.id.ivImagePreview);
        etItemName = findViewById(R.id.etItemName);
        etItemPrice = findViewById(R.id.etItemPrice);
        btnSaveItem = findViewById(R.id.btnSaveItem);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Edit Menu Item");
        btnSaveItem.setText("Update Item");

        itemId = getIntent().getStringExtra("ITEM_ID");
        if (itemId == null || itemId.isEmpty()) {
            Toast.makeText(this, "Error: Item ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        menuDatabaseReference = FirebaseDatabase.getInstance().getReference("MenuItems").child(itemId);
        loadItemData();

        ivImagePreview.setOnClickListener(v -> openGallery());
        btnSaveItem.setOnClickListener(v -> updateMenuItem());
    }

    private void loadItemData() {
        menuDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FoodItem currentItem = snapshot.getValue(FoodItem.class);
                if (currentItem != null) {
                    etItemName.setText(currentItem.getName());
                    etItemPrice.setText(String.valueOf(currentItem.getPrice()));

                    try {
                        byte[] decodedString = Base64.decode(currentItem.getImage(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivImagePreview.setImageBitmap(decodedByte);
                    } catch (Exception e) {
                        ivImagePreview.setImageResource(R.drawable.image_placeholder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditItemActivity.this, "Failed to load item data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void updateMenuItem() {
        String name = etItemName.getText().toString().trim();
        String priceStr = etItemPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("price", Double.parseDouble(priceStr));

        if (imageUri != null) {
            String imageBase64 = convertImageToBase64(ivImagePreview);
            if (imageBase64 != null) {
                updates.put("image", imageBase64);
            }
        }

        menuDatabaseReference.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditItemActivity.this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditItemActivity.this, "Failed to update item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertImageToBase64(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
