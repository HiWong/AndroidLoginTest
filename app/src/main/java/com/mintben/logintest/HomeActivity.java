package com.mintben.logintest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity
        extends RoboActivity implements View.OnClickListener {

    @InjectView(R.id.a_home_id_text)
    TextView idText;
    @InjectView(R.id.a_home_login_button)
    Button loginButton;
    @InjectView(R.id.a_home_progress)
    ProgressBar loginProgress;
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
        this.menuPresenter.setView(this, menu);
        this.menuPresenter.onResume();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result;

        switch (item.getItemId()) {
            case R.id.m_main_login:
                this.menuPresenter.doLogin();
                result = true;
                break;
            case R.id.m_main_logout:
                this.menuPresenter.doLogout();
                result = true;
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        this.presenter.updateView();

        return result;
    }

    public void setIdText(String text) {
        this.idText.setText(text);
    }

    public void setLoginButtonEnabled(boolean enabled) {
        this.loginButton.setEnabled(enabled);
    }

    public void setLoginButtonVisibility(boolean isVisible) {
        int v = isVisible ? View.VISIBLE : View.GONE;
        this.loginButton.setVisibility(v);
    }

    public void setLoginProgressVisibility(boolean visible) {
        int v = visible ? View.VISIBLE : View.GONE;
        this.loginProgress.setVisibility(v);
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

    @Override
    protected void onPause() {
        super.onPause();
        this.presenter.onPause();
        this.menuPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.onResume();
        this.menuPresenter.onResume();
    }
}
