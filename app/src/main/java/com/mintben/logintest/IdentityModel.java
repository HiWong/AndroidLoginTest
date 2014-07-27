package com.mintben.logintest;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.mintben.Utilities.ThreadPreconditions;

@Singleton
public class IdentityModel {
    public static final int STATE_LOGGED_IN = 0;
    public static final int STATE_LOGGED_OUT = 3;
    private int loggedIntState = STATE_LOGGED_OUT;
    public static final int STATE_LOGGING_IN = 1;
    public static final int STATE_LOGGING_OUT = 2;
    private LoginInformation loginInfo;

    public int getLoggedInState() {
        return this.loggedIntState;
    }

    public LoginInformation getLoginInfo() {
        return this.loginInfo;
    }

    public void setLoginInfo(LoginInformation loginInfo) {
        Preconditions.checkNotNull(loginInfo);
        ThreadPreconditions.checkNotOnUiThread();

        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {
        }

        this.loginInfo = loginInfo;
        this.loggedIntState = STATE_LOGGED_IN;
    }

    public boolean isAuthenticated() {
        return this.loggedIntState == STATE_LOGGED_IN;
    }

    public void logOut() {
        this.loginInfo = null;
        this.loggedIntState = STATE_LOGGED_OUT;
    }

    public void startLogin(Context loginContext) {
        Toast.makeText(loginContext, "Login", Toast.LENGTH_SHORT).show();

        Intent authSvc = new Intent(loginContext, AuthenticationService.class);
        loginContext.startService(authSvc);

        this.loggedIntState = STATE_LOGGING_IN;
    }
}
