package com.nikol.ciphernote.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.nikol.ciphernote.cryptography.AesEncryption;

import java.io.Serializable;
import java.security.SecureRandom;

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
        if (title == null) {
            this.title = null;
            return;
        }
        AesEncryption aesEncryption = new AesEncryption();
        try {
            this.title = aesEncryption.encrypt(key, title.getBytes(), null);
        } catch (Exception e) {
            throw new RuntimeException("Didn't set title", e);
        }
    }

    public String getTitle() {
        if (this.title == null) return "";
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.title, null));
        } catch (Exception e) {
            return new String(this.title);
        }
    }

    public void setNote(String note) {
        if (note == null) {
            this.note = null;
            return;
        }
        AesEncryption aesEncryption = new AesEncryption();
        try {
            this.note = aesEncryption.encrypt(key, note.getBytes(), null);
        } catch (Exception e) {
            throw new RuntimeException("Didn't set note", e);
        }
    }

    public String getNote() {
        if (this.note == null) return "";
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.note, null));
        } catch (Exception e) {
            return new String(this.note);
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
