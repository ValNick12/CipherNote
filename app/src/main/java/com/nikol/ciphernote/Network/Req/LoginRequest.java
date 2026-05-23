package com.nikol.ciphernote.Network.Req;

public class LoginRequest {
    public String username;
    public String passwordHash;

    public LoginRequest(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
