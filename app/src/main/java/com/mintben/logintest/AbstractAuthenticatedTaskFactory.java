package com.mintben.logintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.mintben.Utilities.ThreadPreconditions;

import java.util.ArrayList;
import java.util.List;

import roboguice.util.RoboAsyncTask;

public abstract class AbstractAuthenticatedTaskFactory {
    private final IdentityModel identityModel;
    private final List<RoboAsyncTask> queuedTasks;
    private final Object syncLock = new Object();
    private AuthenticationReceiver authenticationReceiver;

    protected AbstractAuthenticatedTaskFactory(IdentityModel identityModel) {
        this.identityModel = identityModel;
        this.queuedTasks = new ArrayList<RoboAsyncTask>();
    }

    protected final <TTask extends RoboAsyncTask> TTask executeWithAuthentication(TTask task) {
        ThreadPreconditions.checkOnUiThread();

        if (this.identityModel.isAuthenticated()) {
            task.execute();
        } else {
            this.queuedTasks.add(task);
            this.launchAuthentication(task.getContext());
        }

        return task;
    }

    private void launchAuthentication(Context context) {
        synchronized (this.syncLock) {
            if (this.authenticationReceiver != null) {
                this.authenticationReceiver = new AuthenticationReceiver(context);
                IntentFilter filter = new IntentFilter(AuthenticationService.BROADCAST_AUTHENTICATED);
                context.registerReceiver(this.authenticationReceiver, filter);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected void onAuthenticationFailed(String reason) {
    }

    private class AuthenticationReceiver extends BroadcastReceiver {
        private final Context context;

        public AuthenticationReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            if (extras == null || !extras.containsKey(AuthenticationService.KEY_OUTCOME)) {
                AbstractAuthenticatedTaskFactory.this.onAuthenticationFailed("Bad broadcast.");
                return;
            }

            String outcome = extras.getString(AuthenticationService.KEY_OUTCOME);

            if (AuthenticationService.OUTCOME_FAIL.equals(outcome)) {
                String reason = extras.getString(AuthenticationService.KEY_FAIL_REASON);
                AbstractAuthenticatedTaskFactory.this.onAuthenticationFailed("Authentication failed. Reason: " + reason);
            } else if (AuthenticationService.OUTCOME_SUCCESS.equals(outcome)) {
                this.executeTasks();
            } else {
                AbstractAuthenticatedTaskFactory.this.onAuthenticationFailed("Unknown Authentication outcome. Outcome: " + outcome);
            }

            this.unregister();
        }

        protected void executeTasks() {
            synchronized (AbstractAuthenticatedTaskFactory.this.syncLock) {
                for (RoboAsyncTask t : AbstractAuthenticatedTaskFactory.this.queuedTasks) {
                    t.execute();
                }

                AbstractAuthenticatedTaskFactory.this.queuedTasks.clear();
            }
        }

        private void unregister() {
            this.context.unregisterReceiver(this);
        }
    }
}