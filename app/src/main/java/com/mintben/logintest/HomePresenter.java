package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

public class HomePresenter {
    @Inject
    IdentityModel identityModel;
    private AuthenticationReceiver onSignInReceiver;
    private HomeActivity view;

    public void doLogin() {
        this.identityModel.startLogin(this.view);
    }

    public void onPause() {
        this.view.unregisterReceiver(this.onSignInReceiver);
    }

    public void onResume() {
        IntentFilter filter = new IntentFilter(AuthenticationService.ON_LOGIN_BROADCAST);
        this.view.registerReceiver(this.onSignInReceiver, filter);
    }

    public void setView(HomeActivity view) {
        this.view = view;
        this.onSignInReceiver = new AuthenticationReceiver();
        this.updateView();
    }

    public void updateView() {
        int state = this.identityModel.getLoggedInState();

        if (state == IdentityModel.STATE_LOGGING_IN) {
            this.view.setWelcomeText("Logging in...");
            this.view.setLoginProgressVisibility(true);
            this.view.setLoginButtonVisibility(true);
            this.view.setLoginButtonEnabled(false);
        } else if (state == IdentityModel.STATE_LOGGED_IN) {
            LoginInformation loginInfo = this.identityModel.getLoginInfo();

            this.view.setWelcomeText("Welcome " + loginInfo.getDisplayName());
            this.view.setIdText("Id: " + loginInfo.getId());
            this.view.setLoginButtonVisibility(false);
            this.view.setLoginProgressVisibility(false);
        } else if (state == IdentityModel.STATE_LOGGING_OUT) {
            this.view.setWelcomeText("Logging out...");
            this.view.setLoginButtonVisibility(false);
            this.view.setLoginProgressVisibility(true);
        } else if (state == IdentityModel.STATE_LOGGED_OUT) {
            this.view.setWelcomeText("You are not logged in.");
            this.view.setIdText("");
            this.view.setLoginButtonVisibility(true);
            this.view.setLoginButtonEnabled(true);
            this.view.setLoginProgressVisibility(false);
        }
    }

    private class AuthenticationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            if (extras == null) {
                return;
            }

            String action = extras.getString(AuthenticationService.KEY_ACTION);

            if (AuthenticationService.ACTION_LOGIN_START.equals(action)) {
                Toast.makeText(context, "Start login", Toast.LENGTH_SHORT).show();
            } else if (AuthenticationService.ACTION_LOGIN_END.equals(action)) {
                Toast.makeText(context, "Finished login", Toast.LENGTH_SHORT).show();
            } else if (AuthenticationService.ACTION_LOGOUT_START.equals(action)) {
                Toast.makeText(context, "Start logout", Toast.LENGTH_SHORT).show();
            } else if (AuthenticationService.ACTION_LOGOUT_END.equals(action)) {
                Toast.makeText(context, "Finished logout", Toast.LENGTH_SHORT).show();
            }

            HomePresenter.this.updateView();
        }
    }
}
