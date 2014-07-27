package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.common.base.Preconditions;

import javax.inject.Inject;

import roboguice.service.RoboIntentService;

public class AuthenticationService extends RoboIntentService {
    public static final String ACTION_LOGIN_END = "LOGIN_END";
    public static final String ACTION_LOGIN_START = "LOGIN_START";
    public static final String ACTION_LOGOUT_END = "LOGOUT_END";
    public static final String ACTION_LOGOUT_START = "LOGOUT_START";
    public static final String BROADCAST_AUTHENTICATED = "com.mintben.logintest.AuthenticationService.BROADCAST_AUTHENTICATED";
    public static final String BROADCAST_LOGIN_CHANGED = "com.mintben.logintest.AuthenticationService.BROADCAST_LOGIN_CHANGED";
    public static final String KEY_ACTION = "com.mintben.logintest.AuthenticationService.KEY_ACTION";
    public static final String KEY_FAIL_REASON = "com.mintben.logintest.AuthenticationService.KEY_FAIL_REASON";
    public static final String KEY_LOGIN_HINT = "com.mintben.logintest.AuthenticationService.KEY_LOGIN_HINT";
    public static final String KEY_OUTCOME = "com.mintben.logintest.AuthenticationService.KEY_OUTCOME";
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

    private void broadcastFail(String action) {
        Intent broadcast = new Intent(BROADCAST_LOGIN_CHANGED);
        broadcast.putExtra(KEY_ACTION, action);
        broadcast.putExtra(KEY_OUTCOME, OUTCOME_FAIL);

        this.sendBroadcast(broadcast);
    }

    private void broadcastSuccess(String action) {
        Intent broadcast = new Intent(BROADCAST_LOGIN_CHANGED);
        broadcast.putExtra(KEY_ACTION, action);
        broadcast.putExtra(KEY_OUTCOME, OUTCOME_SUCCESS);

        this.sendBroadcast(broadcast);
    }

    private void doLogout() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        this.identityModel.logOut();

        this.broadcastSuccess(ACTION_LOGOUT_END);
    }

    private void finishLogin(LoginInformation info) {
        this.identityModel.setLoginInfo(info);

        this.broadcastSuccess(ACTION_LOGIN_END);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Preconditions.checkNotNull(extras);

        String task = extras.getString(KEY_ACTION);

        if (ACTION_LOGIN_END.equals(task)) {
            this.broadcastSuccess(ACTION_LOGIN_START);
            this.startLoginActivity(intent.getExtras());
        } else if (ACTION_LOGOUT_END.equals(task)) {
            this.broadcastSuccess(ACTION_LOGOUT_START);
            this.doLogout();
        }
    }

    private void startLoginActivity(Bundle extras) {
        Intent loginActivity = new Intent(this, LoginActivity.class);

        if (extras != null && extras.containsKey(KEY_LOGIN_HINT)) {
            loginActivity.putExtra(LoginActivity.KEY_LOGIN_HINT, extras.getString(KEY_LOGIN_HINT));
        }

        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplication().startActivity(loginActivity);
    }

    private class LoginActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getExtras() == null) {
                return;
            }

            Bundle e = intent.getExtras();

            boolean success = e.getBoolean(LoginActivity.KEY_LOGIN_SUCCESS);

            if (success) {
                String id = e.getString(LoginActivity.KEY_IDENTITY_ID);
                String displayName = e.getString(LoginActivity.KEY_IDENTITY_DISPLAYNAME);
                LoginInformation info = new LoginInformation(id, displayName);
                AuthenticationService.this.finishLogin(info);
            } else {
                AuthenticationService.this.broadcastFail(ACTION_LOGIN_START);
            }
        }
    }
}
