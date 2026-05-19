package com.nikol.ciphernote.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.nikol.ciphernote.cryptography.PasswordHasher;

import java.io.Serializable;

@Entity(tableName = "profiles")
public class Profiles implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    String username = "";

    @ColumnInfo(name = "password_hash")
    @NonNull
    public String password_hash = "";

    @NonNull
    public String getUsername() { return this.username; }

    public void setUsername(@NonNull String username) { this.username = username; }

    public void setPassword(String password) {
        this.password_hash = PasswordHasher.hash(password);
    }
}
