package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

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
    }
    public void login(View v){
        // Disable button
        v.setEnabled(false);
        Button loginButton = (Button) v;
        loginButton.setText("Logging in...");

        //get credentials
        String username = ((EditText)findViewById(R.id.editText_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();
        Log.d("login", "Credentials accepted!");

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}