package com.example.myapplication.Model;

import static java.security.MessageDigest.getInstance;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Entity(tableName = "profiles")
public class Profiles implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    String username = "";
    @ColumnInfo(name = "password_hash")
    String password_hash = "";

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPassword_hash(String password) {
        this.password_hash = hashPassword(password);
    }

    public static String hashPassword(String pass){
        MessageDigest digest = null;
        try {
            digest = getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashbytes = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return HexFormat.of().formatHex(hashbytes);
        }else{
            return null;
        }
    }
}
