package com.mintyben.logintest;

import javax.inject.Inject;

public class TestFragmentPresenter {
    private final TestAuthenticatedFactory taskFactory;
    private TestTask task;
    private TestFragment view;

    @Inject
    public TestFragmentPresenter(TestAuthenticatedFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public void onGo() {
        if (this.task != null) {
            this.view.setMessageOutput("Task in progress...");
            this.view.setSpinnerVisible(true);
            return;
        }

        String message = this.view.getMessageInput();
        this.view.setSpinnerVisible(true);
        this.view.setMessageOutputVisible(false);
        this.view.setSpinnerVisible(true);
        this.view.setGoButtonEnabled(false);

        this.task = this.taskFactory.createTestTask(this.view.getActivity(), message, new TestTaskHandler());
    }

    public void setView(TestFragment view) {
        this.view = view;
    }

    private class TestTaskHandler implements TestTask.Handler {
        @Override
        public void onFail(Exception e) {
            TestFragmentPresenter.this.view.setMessageOutput("Error: " + e.getLocalizedMessage());
        }

        @Override
        public void onSuccess(String message) {
            TestFragmentPresenter.this.view.setMessageOutput(message);
            TestFragmentPresenter.this.view.setMessageOutputVisible(true);
            TestFragmentPresenter.this.view.setSpinnerVisible(false);
            TestFragmentPresenter.this.view.setGoButtonEnabled(true);
            TestFragmentPresenter.this.task = null;
        }
    }
}
