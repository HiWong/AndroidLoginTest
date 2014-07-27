package com.mintben.logintest;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import com.google.common.base.Strings;

import javax.inject.Inject;

import roboguice.service.RoboService;

public class AuthenticationService extends RoboService {
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Notification n = new Notification.Builder(this).build();
        this.startForeground(0, n);

        Bundle extras = intent.getExtras();

        String idHint = null;
        if (extras != null) {
            idHint = extras.getString(KEY_IDENTITY_HINT);
        }

        this.startLoginActivity(idHint);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    private void broadcastAuthentication(String outcome) {
        Intent broadcast = new Intent(BROADCAST_AUTHENTICATED);
        broadcast.putExtra(KEY_OUTCOME, outcome);
        this.sendBroadcast(broadcast);
    }

    private void finishLogin(IdentityInfo info) {
        this.identityModel.setIdentityInfo(info);
        this.broadcastAuthentication(OUTCOME_SUCCESS);
    }

    private void startLoginActivity(String identityHint) {
        if (this.loginReceiver != null) {
            return;
        }

        LoginReceiver receiver = new LoginReceiver();
        Intent loginActivity = new Intent(this, LoginActivity.class);
        IntentFilter loginFilter = new IntentFilter(LoginActivity.BROADCAST_ON_LOGIN);

        this.registerReceiver(receiver, loginFilter);
        this.loginReceiver = receiver;

        if (Strings.isNullOrEmpty(identityHint)) {
            loginActivity.putExtra(LoginActivity.KEY_LOGIN_HINT, identityHint);
        }

        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplication().startActivity(loginActivity);
    }

    private void unregisterAndStop() {
        this.unregisterReceiver();
        this.stopForeground(true);
        this.stopSelf();
    }

    private void unregisterReceiver() {
        if (this.loginReceiver != null) {
            this.unregisterReceiver(this.loginReceiver);
            this.loginReceiver = null;
        }
    }

    private class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getExtras() == null) {
                AuthenticationService.this.broadcastAuthentication(OUTCOME_FAIL);
                return;
            }

            Bundle e = intent.getExtras();

            boolean success = LoginActivity.OUTCOME_SUCCESS.equals(e.getString(LoginActivity.KEY_OUTCOME));

            if (success) {
                String id = e.getString(LoginActivity.KEY_IDENTITY_ID);
                String displayName = e.getString(LoginActivity.KEY_IDENTITY_DISPLAY_NAME);
                IdentityInfo info = new IdentityInfo(id, displayName);

                AuthenticationService.this.finishLogin(info);
            } else {
                AuthenticationService.this.broadcastAuthentication(OUTCOME_FAIL);
            }

            AuthenticationService.this.unregisterAndStop();
        }
    }
}
