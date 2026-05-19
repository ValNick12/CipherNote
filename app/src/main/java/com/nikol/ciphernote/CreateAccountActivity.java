package com.nikol.ciphernote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nikol.ciphernote.Database.RoomDB;
import com.nikol.ciphernote.Model.Profiles;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editText_username;
    private EditText editText_password;
    private Profiles new_profile;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createAccActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        database = RoomDB.getInstance(this);
        Pattern pattern_pass = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        Pattern pattern_user = Pattern.compile("[a-zA-Z0-9]{8,20}");


        createAccountButton.setOnClickListener(v -> {
            String username = editText_username.getText().toString().trim();
            String password = editText_password.getText().toString().trim();
            Matcher matcher_user = pattern_user.matcher(username);
            Matcher matcher_pass = pattern_pass.matcher(password);

            if (!matcher_user.find()){
                Toast.makeText(CreateAccountActivity.this, "Invalid username!", Toast.LENGTH_LONG).show();
                return;
            }

            if (!matcher_pass.find()){
                Toast.makeText(CreateAccountActivity.this, "Invalid password!", Toast.LENGTH_LONG).show();
                return;
            }

            createAccountButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Thread(() -> {
                if (database.mainDAO().getUsername(username) != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(CreateAccountActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                        createAccountButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    });
                    return;
                }

                Log.d("createAccount", "Create account clicked");
                new_profile = new Profiles();
                new_profile.setUsername(username);
                new_profile.setPassword(password); // This is slow (Argon2)
                database.mainDAO().insert_profile(new_profile);

                runOnUiThread(() -> {
                    Log.d("createAccount", "Account is saved in the database " + new_profile.getUsername());
                    Toast.makeText(CreateAccountActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                    Intent logac = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(logac);
                    finish();
                });
            }).start();
        });
    }
}
