package com.mintben.logintest;

import android.content.Intent;
import android.os.Bundle;

public class LoginPresenter {
    private boolean hasLoggedIn;
    private LoginActivity view;

    public void doLogin() {
        if (!this.checkSuperSecretPassword()) {
            return;
        }
        String userName = this.view.getUserName();

        Intent loginBroadCast = new Intent(LoginActivity.BROADCAST_ON_LOGIN);
        loginBroadCast.putExtra(LoginActivity.KEY_IDENTITY_ID, "id/" + userName);
        loginBroadCast.putExtra(LoginActivity.KEY_IDENTITY_DISPLAY_NAME, userName);

        this.hasLoggedIn = true;
        this.view.sendBroadcast(loginBroadCast);
        this.view.close();
    }

    public void initialize(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        this.view.setUserNameText(savedInstanceState.getString(LoginActivity.KEY_LOGIN_HINT));
    }

    public void onDestroy() {
        if (this.hasLoggedIn) {
            this.view.broadcastLoginChangedSuccess(LoginActivity.ACTION_LOGIN_END);
        } else {
            this.view.broadcastLoginChangedFail(LoginActivity.ACTION_LOGIN_END);
        }
    }

    public void setView(LoginActivity view) {
        this.view = view;
        this.hasLoggedIn = false;
        this.view.broadcastLoginChangedSuccess(LoginActivity.ACTION_LOGIN_START);
    }

    private boolean checkSuperSecretPassword() {
        return "pants".equals(this.view.getPassword());
    }
}
