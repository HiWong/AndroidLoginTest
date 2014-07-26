package com.mintben.logintest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity
        extends RoboActivity implements View.OnClickListener {

    @InjectView(R.id.a_home_login_button)
    Button loginButton;
    @Inject
    MainMenuPresenter menuPresenter;
    @Inject
    HomePresenter presenter;
    @InjectView(R.id.a_home_welcome_text)
    TextView welcomeText;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_home_login_button:
                this.presenter.doLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menuPresenter.setView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result;

        switch (item.getItemId()) {
            case R.id.m_main_login:
                this.menuPresenter.doLogin(this);
                result = true;
                break;
            case R.id.m_main_logout:
                this.menuPresenter.doLogout(this);
                result = true;
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        this.presenter.updateView();

        return result;
    }

    public void setLoginButtonVisibilty(boolean isVisible) {
        int v = isVisible ? View.VISIBLE : View.GONE;
        this.loginButton.setVisibility(v);
    }

    public void setWelcomeText(String text) {
        this.welcomeText.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter.setView(this);
        this.loginButton.setOnClickListener(this);
    }
}
