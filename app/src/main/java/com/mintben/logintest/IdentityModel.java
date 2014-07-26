package com.mintben.logintest;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.mintben.Utilities.ThreadPreconditions;

@Singleton
public class IdentityModel {
    private LoginInformation loginInfo;

    public void doLogin(Context loginContext) {
        Toast.makeText(loginContext, "Login", Toast.LENGTH_SHORT).show();

        Intent authSvc = new Intent(loginContext, AuthenticationService.class);
        authSvc.putExtra(AuthenticationService.KEY_ACTION, AuthenticationService.ACTION_LOGIN_END);
        loginContext.startService(authSvc);
    }

    public void doLogout(Context loginContext) {
        Preconditions.checkNotNull(loginContext);

        Intent authSvc = new Intent(loginContext, AuthenticationService.class);
        authSvc.putExtra(AuthenticationService.KEY_ACTION, AuthenticationService.ACTION_LOGOUT);
        loginContext.startService(authSvc);
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
    }

    public boolean isLoggedIn() {
        return this.loginInfo != null;
    }

    public void logOut() {
        ThreadPreconditions.checkNotOnUiThread();

        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {
        }

        this.loginInfo = null;
    }
}
