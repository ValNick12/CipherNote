package com.example.myapplication.Model;

import android.os.Build;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.cryptography.AesEncryption;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Entity(tableName = "notes")
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id = 0;
    @ColumnInfo(name = "title")
    public byte[] title = null;
    @ColumnInfo(name = "note")
    public byte[] note = null;
    @ColumnInfo(name = "user")
    String user = "";
    @ColumnInfo(name = "key")
    public byte[] key = null;

    public Notes(){
        this.key = generateKey();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            this.title = aesEncryption.encrypt(key, title.getBytes(), null);
        } catch (Exception e) {
            throw new RuntimeException("Didn't set title", e);
        }
    }

    public String getTitle() {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.title, null));
        } catch (Exception e) {
            throw new RuntimeException("Didn't get title", e);
        }
    }

    public void setNote(String note) {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            this.note = aesEncryption.encrypt(key, note.getBytes(), null);
        } catch (Exception e) {
            throw new RuntimeException("Didn't set note", e);
        }
    }

    public String getNote() {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.note, null));
        } catch (Exception e) {
            throw new RuntimeException("Didn't get note", e);
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static byte[] generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return key;
    }
}
