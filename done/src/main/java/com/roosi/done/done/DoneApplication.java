package com.roosi.done.done;

import android.app.Application;

import com.google.api.services.tasks.model.Task;

/**
 * Created by jtn on 27/02/14.
 */
public class DoneApplication extends Application {

    private com.google.api.services.tasks.Tasks mService;
    private Task mTask;

    public void setService(com.google.api.services.tasks.Tasks service) {
        mService = service;
    }

    public com.google.api.services.tasks.Tasks getService() {
        return mService;
    }

    public void setSelectedTask(Task task) {
        mTask = task;
    }

    public Task getSelectedTask() {
        return mTask;
    }
}
