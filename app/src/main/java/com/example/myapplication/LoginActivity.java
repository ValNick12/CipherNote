package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.RoomDB;
import com.example.myapplication.Model.Profiles;

public class LoginActivity extends AppCompatActivity {

    EditText editText_username;
    EditText editText_password;
    Button loginButton;
    Button createAccountButton;
    RoomDB database;
    Profiles profile;

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
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        database = RoomDB.getInstance(this);

        loginButton.setOnClickListener(v -> {
            String username = editText_username.getText().toString().trim();
            String password = editText_password.getText().toString().trim();
            if (username.equals(database.mainDAO().getUsername(username))){
                if(Profiles.hashPassword(password, database.mainDAO().getSalt(username))
                        .equals(database.mainDAO().getPasswordHash(username))){
                    profile = database.mainDAO().getProfile(username);
                    Log.d("login", "Credentials accepted!");
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    main.putExtra("user", profile);
                    startActivity(main);
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong password!!!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "There is no user with this username!!!", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener(v -> {
            Intent caa = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(caa);
        });

    }
}