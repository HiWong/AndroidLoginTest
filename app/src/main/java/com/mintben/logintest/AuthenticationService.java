package com.mintben.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.common.base.Preconditions;

import javax.inject.Inject;

import roboguice.service.RoboIntentService;

public class AuthenticationService extends RoboIntentService {
    public static final String ACTION_LOGIN_END = "LOGIN";
    public static final String ACTION_LOGIN_START = "LOGIN_START";
    public static final String ACTION_LOGOUT = "LOGOUT";
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_OUTCOME = "KEY_OUTCOME";
    public static final String ON_LOGIN_BROADCAST = "com.mintyben.service.LOGIN";
    public static final String OUTCOME_FAIL = "KEY_OUTCOME";
    public static final String OUTCOME_SUCCESS = "KEY_OUTCOME";
    @SuppressWarnings("UnusedDeclaration")
    @Inject
    private IdentityModel identityModel;

    public AuthenticationService() {
        super("AuthenticationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    private void broadcastSuccess(String action) {
        Intent broadcast = new Intent(ON_LOGIN_BROADCAST);
        broadcast.putExtra(KEY_ACTION, action);
        this.sendBroadcast(broadcast);
    }

    private void doLogin() {
        LoginInformation info = new LoginInformation("login/1234", "Someone");
        this.identityModel.setLoginInfo(info);

        this.broadcastSuccess(ACTION_LOGIN_END);
    }

    private void doLogout() {
        this.identityModel.logOut();

        this.broadcastSuccess(ACTION_LOGOUT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        Bundle extras = intent.getExtras();
        Preconditions.checkNotNull(extras);

        String task = extras.getString(KEY_ACTION);

        if (ACTION_LOGIN_END.equals(task)) {
            this.doLogin();
        } else if (ACTION_LOGOUT.equals(task)) {
            this.doLogout();
        }
    }
}
