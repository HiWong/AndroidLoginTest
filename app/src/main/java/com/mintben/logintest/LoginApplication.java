package com.mintben.logintest;

import android.app.Application;
import android.os.StrictMode;

public class LoginApplication extends Application {
    private static final boolean DEVELOPER_MODE = true;

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .detectCustomSlowCalls()
                    .penaltyLog()
                    .penaltyDialog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate();
    }
}
