package com.mintben.logintest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import roboguice.inject.InjectView;

public class LoginActivity
        extends Activity
        implements View.OnClickListener {

    public static final String KEY_IDENTITY_DISPLAYNAME = "KEY_IDENTITY_DISPLAYNAME";
    public static final String KEY_IDENTITY_ID = "KEY_IDENTITY_ID";
    public static final String KEY_LOGIN_HINT = "KEY_LOGIN_HINT";
    public static final String KEY_LOGIN_SUCCESS = "KEY_LOGIN_FAIL";
    public static final String ON_LOGIN_BROADCAST = "com.mintyben.logintest.LOGIN_ACTIVITY";

    @InjectView(R.id.a_login_login_button)
    private Button loginButton;
    @InjectView(R.id.a_login_password_text)
    private EditText password;
    @Inject
    private LoginPresenter presenter;
    @InjectView(R.id.a_login_userName_text)
    private EditText userName;

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
}
