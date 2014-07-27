package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.common.base.Strings;

import javax.inject.Inject;

import roboguice.service.RoboIntentService;

public class AuthenticationService extends RoboIntentService {
    public static final String BROADCAST_AUTHENTICATED = "com.mintben.logintest.AuthenticationService.BROADCAST_AUTHENTICATED";

    public static final String KEY_FAIL_REASON = "com.mintben.logintest.AuthenticationService.KEY_FAIL_REASON";
    public static final String KEY_IDENTITY_HINT = "com.mintben.logintest.AuthenticationService.KEY_IDENTITY_HINT";
    public static final String KEY_OUTCOME = "com.mintben.logintest.AuthenticationService.KEY_OUTCOME";

    public static final String OUTCOME_FAIL = "OUTCOME_FAIL";
    public static final String OUTCOME_SUCCESS = "OUTCOME_SUCCESS";

    @SuppressWarnings("UnusedDeclaration")
    @Inject
    private IdentityModel identityModel;

    private LoginReceiver loginReceiver;

    public AuthenticationService() {
        super("AuthenticationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void broadcastAuthenticationFail() {
        Intent broadcast = new Intent(BROADCAST_AUTHENTICATED);
        broadcast.putExtra(KEY_OUTCOME, OUTCOME_FAIL);
        this.sendBroadcast(broadcast);
    }

    private void finishLogin(LoginInformation info) {
        this.identityModel.setLoginInfo(info);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        String idHint = null;
        if (extras != null) {
            idHint = extras.getString(KEY_IDENTITY_HINT);
        }
        this.startLoginActivity(idHint);
    }

    private void startLoginActivity(String identityHint) {
        if (this.loginReceiver != null) {
            return;
        }

        Intent loginActivity = new Intent(this, LoginActivity.class);

        if (Strings.isNullOrEmpty(identityHint)) {
            loginActivity.putExtra(LoginActivity.KEY_LOGIN_HINT, identityHint);
        }

        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplication().startActivity(loginActivity);
    }

    private class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getExtras() == null) {
                return;
            }

            Bundle e = intent.getExtras();

            boolean success = e.getBoolean(LoginActivity.KEY_LOGIN_SUCCESS);

            if (success) {
                String id = e.getString(LoginActivity.KEY_IDENTITY_ID);
                String displayName = e.getString(LoginActivity.KEY_IDENTITY_DISPLAY_NAME);
                LoginInformation info = new LoginInformation(id, displayName);
                AuthenticationService.this.finishLogin(info);
            } else {
                AuthenticationService.this.broadcastAuthenticationFail();
            }

            AuthenticationService.this.unregisterReceiver(this);
            AuthenticationService.this.loginReceiver = null;
        }
    }
}
