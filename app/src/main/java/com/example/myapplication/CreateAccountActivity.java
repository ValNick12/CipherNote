package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.MainDAO;
import com.example.myapplication.Database.RoomDB;
import com.example.myapplication.Model.Profiles;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editText_username;
    EditText editText_password;
    Profiles new_profile;
    Button createAccountButton;
    RoomDB database;

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
        createAccountButton = findViewById(R.id.createAccountButton);
        database = RoomDB.getInstance(this);
        Pattern pattern_pass = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        Pattern pattern_user = Pattern.compile("[a-zA-Z]{8,20}");


        createAccountButton.setOnClickListener(v -> {
            String username = editText_username.getText().toString();
            String password = editText_password.getText().toString();
            Matcher matcher_user = pattern_user.matcher(username);
            Matcher matcher_pass = pattern_pass.matcher(password);
            if (matcher_user.find()){
                if (matcher_pass.find()){
                    Log.d("createAccount", "Create account clicked");
                    new_profile = new Profiles(username, password);
                    database.mainDAO().insert_profile(new_profile);
                    Log.d("createAccount", "Account is saved in the database " + new_profile.getUsername());
                    Toast.makeText(CreateAccountActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                    Intent logac = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(logac);
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Invalid password!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(CreateAccountActivity.this, "Invalid username!", Toast.LENGTH_LONG).show();
            }
        });
    }
}