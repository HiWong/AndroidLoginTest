package com.mintben.logintest;

import android.content.Context;
import android.widget.Toast;

import com.google.inject.Singleton;

@Singleton
public class IdentityModel {
    private String displayName;
    private boolean loggedIn;
    private String loginId;

    public void doLogin(Context loginContext) {
        this.loggedIn = true;
        this.displayName = "Someone";
        this.loginId = "login/1234";
        Toast.makeText(loginContext, "Login", Toast.LENGTH_SHORT).show();
    }

    public void doLogout(Context loginContext) {
        this.loggedIn = false;
        this.displayName = null;
        this.loginId = null;
        Toast.makeText(loginContext, "Logout", Toast.LENGTH_SHORT).show();
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLoginId() {

        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.loggedIn = isLoggedIn;
    }
}
