package com.nikol.ciphernote.cryptography;

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

    public void setMasterKey(byte[] masterKey) {
        this.masterKey = masterKey;
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
