package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

public class MainMenuPresenter {

    private Context context;
    @SuppressWarnings("UnusedDeclaration")
    @Inject
    private IdentityModel identityModel;
    private MenuItem loginItem;
    private MenuItem logoutItem;
    private AuthenticationReceiver onAuthenticationReceiver;

    public void doLogin() {
        this.identityModel.startLogin(this.context);
    }

    public void doLogout() {
        this.identityModel.logOut();
        this.updateView();
    }

    public void onPause() {
        if (this.onAuthenticationReceiver != null) {
            this.context.unregisterReceiver(this.onAuthenticationReceiver);
        }
    }

    public void onResume() {
        if (this.context != null) {
            IntentFilter filter = new IntentFilter(AuthenticationService.BROADCAST_AUTHENTICATED);
            this.context.registerReceiver(this.onAuthenticationReceiver, filter);
        }
    }

    public void setView(Context context, Menu view) {
        this.context = context;
        this.loginItem = view.findItem(R.id.m_main_login);
        this.logoutItem = view.findItem(R.id.m_main_logout);

        this.onAuthenticationReceiver = new AuthenticationReceiver();

        this.updateView();
    }

    private void updateView() {
        int state = this.identityModel.getLoggedInState();
        this.loginItem.setVisible(state == IdentityModel.STATE_LOGGED_OUT);
        this.logoutItem.setVisible(state == IdentityModel.STATE_LOGGED_IN);
    }


    private class AuthenticationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainMenuPresenter.this.updateView();
        }
    }
}
