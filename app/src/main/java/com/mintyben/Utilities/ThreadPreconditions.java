package com.mintyben.Utilities;

import android.os.Looper;

@SuppressWarnings("UnusedDeclaration")
public class ThreadPreconditions {
    private ThreadPreconditions() {
    }

    public static void checkNotOnUiThread() {
        if (isOnMainThreadValue()) {
            throw new RuntimeException("Running operation on UI thread. This should be run on background thread");
        }
    }

    public static void checkOnUiThread() {
        if (!isOnMainThreadValue()) {
            throw new RuntimeException("Not running operation UI thread.");
        }
    }

    private static boolean isOnMainThreadValue() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
