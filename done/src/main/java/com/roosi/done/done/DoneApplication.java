package com.roosi.done.done;

import android.app.Application;

/**
 * Created by jtn on 27/02/14.
 */
public class DoneApplication extends Application {

    private com.google.api.services.tasks.Tasks mService;

    public void setService(com.google.api.services.tasks.Tasks service) {
        mService = service;
    }

    public com.google.api.services.tasks.Tasks getService() {
        return mService;
    }
}
