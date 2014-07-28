package com.mintyben.logintest;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

@Singleton
public class IdentityModel {
    public static final int STATE_AUTHENTICATED = 1;
    public static final int STATE_NOT_AUTHENTICATED = 0;

    private IdentityInfo identityInfo;

    public int getAuthenticatedState() {
        return this.identityInfo == null ?
                STATE_NOT_AUTHENTICATED :
                STATE_AUTHENTICATED;
    }

    public IdentityInfo getIdentityInfo() {
        return this.identityInfo;
    }

    public void setIdentityInfo(IdentityInfo identityInfo) {
        Preconditions.checkNotNull(identityInfo);

        this.identityInfo = identityInfo;
    }

    public void logOut() {
        this.identityInfo = null;
    }

    public void startLogin(Context loginContext) {
        Toast.makeText(loginContext, "Login", Toast.LENGTH_SHORT).show();

        Intent authSvc = new Intent(loginContext, AuthenticationService.class);
        loginContext.startService(authSvc);
    }
}
