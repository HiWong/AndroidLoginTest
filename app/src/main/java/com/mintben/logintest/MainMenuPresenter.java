package com.mintben.logintest;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

public class MainMenuPresenter {

    @SuppressWarnings("UnusedDeclaration")
    @Inject
    private IdentityModel identityModel;
    private MenuItem loginItem;
    private MenuItem logoutItem;

    public void doLogin(Context context) {
        this.identityModel.doLogin(context);
        this.updateView();
    }

    public void doLogout(Context context) {
        this.identityModel.doLogout(context);
        this.updateView();
    }

    public void setView(Menu view) {
        this.loginItem = view.findItem(R.id.m_main_login);
        this.logoutItem = view.findItem(R.id.m_main_logout);

        this.updateView();
    }

    private void updateView() {
        this.loginItem.setVisible(!this.identityModel.isLoggedIn());
        this.logoutItem.setVisible(this.identityModel.isLoggedIn());
    }
}
