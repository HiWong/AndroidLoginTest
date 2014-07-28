package com.mintyben.logintest;

public class IdentityInfo {
    private final String displayName;
    private final String id;

    public IdentityInfo(String id, String displayName) {
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
