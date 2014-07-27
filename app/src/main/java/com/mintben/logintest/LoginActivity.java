package com.mintben.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class LoginActivity
        extends RoboActivity
        implements View.OnClickListener {

    public static final String ACTION_LOGIN_END = "LOGIN_END";
    public static final String ACTION_LOGIN_START = "LOGIN_START";
    public static final String ACTION_LOGOUT_END = "LOGOUT_END";
    public static final String ACTION_LOGOUT_START = "LOGOUT_START";
    public static final String BROADCAST_LOGIN_STATE_CHANGED = "com.mintyben.logintest.LoginActivity.BROADCAST_LOGIN_STATE_CHANGED";
    public static final String BROADCAST_ON_LOGIN = "com.mintyben.logintest.LoginActivity.LOGIN_ACTIVITY";
    public static final String KEY_ACTION = "com.mintyben.logintest.LoginActivity.KEY_ACTION";
    public static final String KEY_IDENTITY_DISPLAY_NAME = "com.mintyben.logintest.LoginActivity.KEY_IDENTITY_DISPLAY_NAME";
    public static final String KEY_IDENTITY_ID = "com.mintyben.logintest.LoginActivity.KEY_IDENTITY_ID";
    public static final String KEY_LOGIN_HINT = "com.mintyben.logintest.LoginActivity.KEY_IDENTITY_HINT";
    public static final String KEY_LOGIN_SUCCESS = "com.mintyben.logintest.LoginActivity.KEY_LOGIN_FAIL";
    public static final String KEY_OUTCOME = "com.mintyben.logintest.LoginActivity.KEY_OUTCOME";
    public static final String OUTCOME_FAIL = "OUTCOME_FAIL";
    public static final String OUTCOME_SUCCESS = "OUTCOME_SUCCESS";
    @InjectView(R.id.a_login_login_button)
    private Button loginButton;
    @InjectView(R.id.a_login_password_text)
    private EditText password;
    @Inject
    private LoginPresenter presenter;
    @InjectView(R.id.a_login_userName_text)
    private EditText userName;

    public void broadcastLoginChangedFail(String action) {
        Intent broadcast = new Intent(BROADCAST_LOGIN_STATE_CHANGED);
        broadcast.putExtra(KEY_ACTION, action);
        broadcast.putExtra(KEY_OUTCOME, OUTCOME_FAIL);

        this.sendBroadcast(broadcast);
    }

    public void broadcastLoginChangedSuccess(String action) {
        Intent broadcast = new Intent(BROADCAST_LOGIN_STATE_CHANGED);
        broadcast.putExtra(KEY_ACTION, action);
        broadcast.putExtra(KEY_OUTCOME, OUTCOME_SUCCESS);

        this.sendBroadcast(broadcast);
    }

    public void close() {
        this.finish();
    }

    public String getPassword() {
        return this.password.getText().toString();
    }

    public String getUserName() {
        return this.userName.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_login_login_button:
                this.presenter.doLogin();
            default:
                break;
        }
    }

    public void setUserNameText(String userName) {
        this.userName.setText(userName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        this.loginButton.setOnClickListener(this);

        this.presenter.setView(this);
        this.presenter.initialize(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
    }
}
