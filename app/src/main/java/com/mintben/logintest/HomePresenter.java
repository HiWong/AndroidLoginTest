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
    private boolean isLoggingIn;
    private AuthenticationReceiver onSignInReceiver;
    private HomeActivity view;

    public void doLogin() {
        this.isLoggingIn = true;
        this.identityModel.doLogin(this.view);
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
        if (this.isLoggingIn) {
            this.view.setWelcomeText("Logging in...");
            this.view.setProgressBarVisibility(true);
            this.view.setLoginButtonVisibility(true);
            this.view.setLoginButtonEnabled(false);

        } else if (this.identityModel.isLoggedIn()) {
            LoginInformation loginInfo = this.identityModel.getLoginInfo();

            this.view.setWelcomeText("Welcome " + loginInfo.getDisplayName());
            this.view.setIdText("Id: " + loginInfo.getId());
            this.view.setLoginButtonVisibility(false);
            this.view.setProgressBarVisibility(false);
        } else {
            this.view.setWelcomeText("You are not logged in.");
            this.view.setIdText("");
            this.view.setLoginButtonVisibility(true);
            this.view.setLoginButtonEnabled(true);
            this.view.setProgressBarVisibility(false);
        }
    }

    private class AuthenticationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                String action = extras.getString(AuthenticationService.KEY_ACTION);

                if (AuthenticationService.ACTION_LOGIN_END.equals(action)) {
                    Toast.makeText(context, "Relieved login", Toast.LENGTH_SHORT).show();
                } else if (AuthenticationService.ACTION_LOGOUT.equals(action)) {
                    Toast.makeText(context, "Relieved logout", Toast.LENGTH_SHORT).show();
                }

                HomePresenter.this.isLoggingIn = false;
                HomePresenter.this.updateView();
            }
        }
    }
}
