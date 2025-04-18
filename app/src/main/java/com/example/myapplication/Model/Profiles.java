package com.example.myapplication.Model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HexFormat;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Entity(tableName = "profiles")
public class Profiles implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    String username = "";
    @ColumnInfo(name = "password_hash")
    public String password_hash = "";
    @ColumnInfo(name = "password_salt")
    public byte[] salt = new byte[16];


    @NonNull
    public String getUsername() {
        return this.username;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public byte[] getSalt(){
        return this.salt;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        setSalt();
        this.password_hash = hashPassword(password, salt);
    }
    private void setSalt(){
        SecureRandom random = new SecureRandom();
        random.nextBytes(this.salt);
    }

    public static String hashPassword(String password, byte[] salt){
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 12000, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                return HexFormat.of().formatHex(hash);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "admin";
    }
}
