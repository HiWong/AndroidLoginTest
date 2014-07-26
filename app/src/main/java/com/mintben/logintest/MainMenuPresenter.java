package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

public class MainMenuPresenter {

    private Context context;
    @SuppressWarnings("UnusedDeclaration")
    @Inject
    private IdentityModel identityModel;
    private MenuItem loginItem;
    private MenuItem logoutItem;
    private AuthenticationReceiver onLogInReceiver;

    public void doLogin() {
        this.identityModel.doLogin(this.context);
        this.updateView();
    }

    public void doLogout() {
        this.identityModel.doLogout(this.context);
        this.updateView();
    }

    public void onPause() {
        this.context.unregisterReceiver(this.onLogInReceiver);
    }

    public void onResume() {
        if (this.context != null) {
            IntentFilter filter = new IntentFilter(AuthenticationService.ON_LOGIN_BROADCAST);
            this.context.registerReceiver(this.onLogInReceiver, filter);
        }
    }

    public void setView(Context context, Menu view) {
        this.context = context;
        this.loginItem = view.findItem(R.id.m_main_login);
        this.logoutItem = view.findItem(R.id.m_main_logout);

        this.onLogInReceiver = new AuthenticationReceiver();

        this.updateView();
    }

    private void updateView() {
        this.loginItem.setVisible(!this.identityModel.isLoggedIn());
        this.logoutItem.setVisible(this.identityModel.isLoggedIn());
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

                MainMenuPresenter.this.updateView();
            }
        }
    }
}
