package com.mintben.logintest;

import android.content.Intent;
import android.os.Bundle;

public class LoginPresenter {
    private LoginActivity view;

    public void doLogin() {
        String userName = this.view.getUserName();

        Intent loginBroadCast = new Intent(LoginActivity.ON_LOGIN_BROADCAST);
        loginBroadCast.putExtra(LoginActivity.KEY_IDENTITY_ID, "id/" + userName);
        loginBroadCast.putExtra(LoginActivity.KEY_IDENTITY_DISPLAYNAME, userName);

        this.view.sendBroadcast(loginBroadCast);
    }

    public void initialize(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        this.view.setUserNameText(savedInstanceState.getString(LoginActivity.KEY_LOGIN_HINT));
    }

    public void setView(LoginActivity view) {
        this.view = view;
    }
}
