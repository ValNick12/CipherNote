package com.nikol.ciphernote.cryptography;

import java.nio.charset.StandardCharsets;

public class SessionManager {
    private static SessionManager instance;
    private byte[] masterKey;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void deriveMasterKey(String password, String username) {
        byte[] salt = username.getBytes(StandardCharsets.UTF_8);
        this.masterKey = PasswordHasher.deriveKey(password, salt);
    }

    public byte[] getMasterKey() {
        return masterKey;
    }

    public void clear() {
        if (masterKey != null) {
            java.util.Arrays.fill(masterKey, (byte) 0);
            masterKey = null;
        }
    }
}
