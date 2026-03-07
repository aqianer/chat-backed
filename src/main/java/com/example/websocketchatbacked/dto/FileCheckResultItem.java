package com.example.websocketchatbacked.dto;

public class FileCheckResultItem {
    private String hash;
    private Boolean exists;
    private String token;

    public FileCheckResultItem() {
    }

    public FileCheckResultItem(String hash, Boolean exists, String token) {
        this.hash = hash;
        this.exists = exists;
        this.token = token;
    }

    public static FileCheckResultItem exists(String hash) {
        return new FileCheckResultItem(hash, true, null);
    }

    public static FileCheckResultItem notExists(String hash, String token) {
        return new FileCheckResultItem(hash, false, token);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
