package com.mintyben.logintest;

import android.content.Context;

import roboguice.util.RoboAsyncTask;

public class TestTask extends RoboAsyncTask<String> {

    private final Handler handler;
    private final String message;

    public TestTask(Context context, String message, Handler handler) {
        super(context);
        this.message = message;
        this.handler = handler;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);

        return this.message;
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        this.handler.onFail(e);
    }

    @Override
    protected void onSuccess(String result) throws Exception {
        this.handler.onSuccess(result);
    }

    public static interface Handler {
        void onFail(Exception e);

        void onSuccess(String message);
    }
}
