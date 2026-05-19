package com.nikol.ciphernote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nikol.ciphernote.Database.RoomDB;
import com.nikol.ciphernote.Model.Profiles;
import com.nikol.ciphernote.cryptography.PasswordHasher;
import com.nikol.ciphernote.cryptography.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_username;
    private EditText editText_password;
    private RoomDB database;
    private Profiles profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        database = RoomDB.getInstance(this);

        loginButton.setOnClickListener(v -> {
            String username = editText_username.getText().toString().trim();
            String password = editText_password.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Thread(() -> {
                String dbUser = database.mainDAO().getUsername(username);
                if (dbUser == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "There is no user with this username!!!", Toast.LENGTH_SHORT).show();
                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    });
                    return;
                }

                String storedHash = database.mainDAO().getPasswordHash(username);
                if (PasswordHasher.verify(password, storedHash)) {
                    profile = database.mainDAO().getProfile(username);
                    
                    // Derive and store the master key in SessionManager
                    byte[] masterKey = PasswordHasher.deriveMasterKey(password, storedHash);
                    SessionManager.getInstance().setMasterKey(masterKey);

                    runOnUiThread(() -> {
                        Log.d("login", "Credentials accepted!");
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        main.putExtra("user", profile);
                        startActivity(main);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Wrong password!!!", Toast.LENGTH_SHORT).show();
                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }).start();
        });

        createAccountButton.setOnClickListener(v -> {
            Intent caa = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(caa);
        });
    }
}
