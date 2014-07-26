package com.mintben.logintest;

import javax.inject.Inject;

public class HomePresenter {
    @Inject
    IdentityModel identityModel;
    private HomeActivity view;

    public void doLogin() {
        this.identityModel.doLogin(this.view);
        this.updateView();
    }

    public void setView(HomeActivity view) {
        this.view = view;
        this.updateView();
    }

    public void updateView() {
        if (this.identityModel.isLoggedIn()) {
            this.view.setWelcomeText("Welcome " + this.identityModel.getDisplayName());
            this.view.setLoginButtonVisibilty(false);
        } else {
            this.view.setWelcomeText("You are not logged in.");
            this.view.setLoginButtonVisibilty(true);
        }
    }
}
