package com.mintben.logintest;

import android.content.Context;

import javax.inject.Inject;

import roboguice.util.RoboAsyncTask;

public class AuthenticatedTask extends RoboAsyncTask {
    @Inject
    private IdentityModel identityModel;

    protected AuthenticatedTask(Context context) {
        super(context);
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
