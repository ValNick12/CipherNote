package com.nikol.ciphernote.Network.Req;

public class RegisterRequest {
    public String username;
    public String passwordHash;

    public RegisterRequest(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
