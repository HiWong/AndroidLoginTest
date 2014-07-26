package com.mintben.logintest;

public class LoginInformation {
    private final String displayName;
    private final String id;

    public LoginInformation(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }
}
