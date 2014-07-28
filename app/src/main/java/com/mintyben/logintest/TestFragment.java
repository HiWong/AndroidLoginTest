package com.mintyben.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class TestFragment extends RoboFragment implements View.OnClickListener {

    @InjectView(R.id.f_test_goButton)
    private Button goButton;

    @InjectView(R.id.f_test_message_input)
    private EditText messageInput;

    @InjectView(R.id.f_test_message_output)
    private TextView messageOutput;
    @Inject
    private TestFragmentPresenter presenter;
    @InjectView(R.id.f_test_spinner)
    private ProgressBar spinner;

    public String getMessageInput() {
        return this.messageInput.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_test_goButton:
                presenter.onGo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.presenter.setView(this);
        this.goButton.setOnClickListener(this);
    }

    public void setGoButtonEnabled(boolean goButtonEnabled) {
        this.goButton.setEnabled(goButtonEnabled);
    }

    public void setMessageOutput(String message) {
        this.messageOutput.setText(message);
    }

    public void setMessageOutputVisible(boolean visible) {
        int v = visible ? View.VISIBLE : View.GONE;
        this.messageOutput.setVisibility(v);
    }

    public void setSpinnerVisible(boolean visible) {
        int v = visible ? View.VISIBLE : View.GONE;
        this.spinner.setVisibility(v);
    }
}
