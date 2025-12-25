
package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextNumberPassword);
        loginButton = findViewById(R.id.loginbutton);
        registerTextView = findViewById(R.id.textView2);


        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();

            } else {
                loginUser(email, password);

            }
        });


        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserRole(user.getUid());
                        }

                    } else {

                        Toast.makeText(MainActivity.this, "Authentication failed. Check your details.", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void checkUserRole(String uid) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("isAdmin").exists() && Boolean.TRUE.equals(snapshot.child("isAdmin").getValue(Boolean.class))) {
                    Toast.makeText(MainActivity.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AdminPanelActivity.class);

                    startActivity(intent);
                } else {

                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error checking user role: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
