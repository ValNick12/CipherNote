package com.example.myapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "profiles")
public class Profiles implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "username")
    String username;
    @ColumnInfo(name = "password_hash")
    String password_hash;

    public Profiles(String username, String password_hash) {
        this.username = username;
        this.password_hash = password_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
}
