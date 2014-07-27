package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.common.base.Strings;

import javax.inject.Inject;

public class HomePresenter {
    @Inject
    IdentityModel identityModel;
    private boolean initialized = false;
    private OnAuthenticatedReceiver onAuthenticatedReceiver;
    private LoginStateChangedReceiver onSignStateChangedReceiver;
    private HomeActivity view;

    public void doLogin() {
        this.identityModel.startLogin(this.view);
    }

    public void onPause() {
        this.view.unregisterReceiver(this.onSignStateChangedReceiver);
        this.view.unregisterReceiver(this.onAuthenticatedReceiver);
    }

    public void onResume() {
        IntentFilter loginFilter = new IntentFilter(LoginActivity.BROADCAST_LOGIN_STATE_CHANGED);
        this.view.registerReceiver(this.onSignStateChangedReceiver, loginFilter);

        IntentFilter authFilter = new IntentFilter(AuthenticationService.BROADCAST_AUTHENTICATED);
        this.view.registerReceiver(this.onAuthenticatedReceiver, authFilter);

        this.updateViewLoginInformation();
    }

    public void setView(HomeActivity view) {
        this.view = view;
        this.initialized = true;

        this.onSignStateChangedReceiver = new LoginStateChangedReceiver();
        this.onAuthenticatedReceiver = new OnAuthenticatedReceiver();
        this.updateViewLoginInformation();
    }

    public void updateViewFromLoginState(String loginState, boolean success) {
        if (!this.initialized || Strings.isNullOrEmpty(loginState)) {
            return;
        }

        if (loginState.equals(LoginActivity.ACTION_LOGIN_START)) {
            this.view.setWelcomeText("Logging in...");
            this.view.setLoginProgressVisibility(true);
            this.view.setLoginButtonEnabled(false);
        } else if (loginState.equals(LoginActivity.ACTION_LOGIN_END)) {
            this.view.setLoginProgressVisibility(false);

            if (!success) {
                this.view.setWelcomeText("Login failed.");
            }

        } else if (loginState.equals(LoginActivity.ACTION_LOGOUT_START)) {
            this.view.setWelcomeText("Logging out...");
            this.view.setLoginProgressVisibility(true);
        } else if (loginState.equals(LoginActivity.ACTION_LOGIN_END)) {
            this.view.setLoginProgressVisibility(false);
        }
    }

    public void updateViewLoginInformation() {
        if (!this.initialized) return;

        int state = this.identityModel.getAuthenticatedState();

        if (state == IdentityModel.STATE_NOT_AUTHENTICATED) {
            this.view.setWelcomeText("You are not authenticated.");

            this.view.setLoginButtonVisibile(true);
            this.view.setLoginButtonEnabled(true);

            this.view.setLoginInfoLayoutVisibile(false);
        } else if (state == IdentityModel.STATE_AUTHENTICATED) {
            IdentityInfo idInfo = this.identityModel.getIdentityInfo();

            this.view.setLoginInfoLayoutVisibile(true);
            this.view.setWelcomeText("Welcome " + idInfo.getDisplayName());
            this.view.setIdText(idInfo.getId());
            this.view.setDisplayNameText(idInfo.getDisplayName());

            this.view.setLoginButtonVisibile(false);
        }
    }

    private class OnAuthenticatedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }
            HomePresenter.this.updateViewLoginInformation();
        }
    }

    private class LoginStateChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            if (extras == null) {
                return;
            }

            String action = extras.getString(LoginActivity.KEY_ACTION);
            boolean success = LoginActivity.OUTCOME_SUCCESS.equals(extras.getString(LoginActivity.KEY_OUTCOME));

            HomePresenter.this.updateViewFromLoginState(action, success);
        }
    }
}
