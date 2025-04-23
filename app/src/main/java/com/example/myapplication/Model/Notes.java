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
    String title = "";
    @ColumnInfo(name = "note")
    String note = "";
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
            this.title = new String(aesEncryption.encrypt(key, title.getBytes(), null));
        } catch (Exception e) {
            throw new RuntimeException("Didn't set title", e);
        }
    }

    public String getTitle() {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.title.getBytes(), null));
        } catch (Exception e) {
            throw new RuntimeException("Didn't get title", e);
        }
    }

    public void setNote(String note) {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            this. note = new String(aesEncryption.encrypt(key, note.getBytes(), null));
        } catch (Exception e) {
            throw new RuntimeException("Didn't set note", e);
        }
    }

    public String getNote() {
        AesEncryption aesEncryption = new AesEncryption();
        try {
            return new String(aesEncryption.decrypt(key, this.note.getBytes(), null));
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

    public static String encrypt(String plainText, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        byte[] cipherMessage = byteBuffer.array();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(cipherMessage);
        }else{
            throw new RuntimeException();
        }
    }

    public static String decrypt(String encryptedDataInBase64, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] encryptedDataBytes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedDataBytes = Base64.getDecoder().decode(encryptedDataInBase64.getBytes());
        }else{
            throw new RuntimeException();
        }

        AlgorithmParameterSpec iv = new GCMParameterSpec(128, encryptedDataBytes, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] plainText = cipher.doFinal(encryptedDataBytes, 12, encryptedDataBytes.length - 12);

        return new String(plainText);
    }

    public static byte[] generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return key;
    }
}
