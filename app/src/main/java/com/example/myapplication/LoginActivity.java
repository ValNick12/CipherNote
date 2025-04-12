package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.MainDAO;
import com.example.myapplication.Database.RoomDB;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    String username;
    String password;
    Button loginButton;
    Button createAccButton;
    RoomDB database;

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

        username = ((EditText)findViewById(R.id.editText_username)).getText().toString().trim();
        password = ((EditText)findViewById(R.id.editText_password)).getText().toString().trim();
        loginButton = findViewById(R.id.loginButton);
        createAccButton = findViewById(R.id.createAccButton);

        database = RoomDB.getInstance(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals(database.mainDAO().getUsername(username))
                && password.equals(database.mainDAO().getPasswordHash(password))){
                    Log.d("login", "Credentials accepted!");
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong username or password!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caa = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(caa);
            }
        });

    }
}