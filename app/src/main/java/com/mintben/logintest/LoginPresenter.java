package com.mintben.logintest;

import android.os.Bundle;

public class LoginPresenter {
    private boolean hasLoggedIn;
    private LoginActivity view;

    public void doLogin() {
        if (!this.checkSuperSecretPassword()) {
            return;
        }
        String userName = this.view.getUserName();

        this.hasLoggedIn = true;
        this.view.broadcastOnLoginSuccess("id/" + userName, userName);
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
            this.view.broadcastOnLoginFail("Did not log in");
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
