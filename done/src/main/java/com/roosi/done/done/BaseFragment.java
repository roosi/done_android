package com.roosi.done.done;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by jtn on 27/02/14.
 */
public class BaseFragment extends Fragment {

    public interface OnLoadingListener {
        public void onLoadingStarted();
        public void onLoadingStopped();
    }

    public interface OnErrorLister {
        public void onError(Exception e);
    }

    private OnLoadingListener mListener;
    private OnErrorLister mErrorListener;
    protected com.google.api.services.tasks.Tasks mService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoadingListener)activity;
            mErrorListener = (OnErrorLister)activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLoadingListener");
        }
        mService = getService();
    }

    protected void onLoadingStarted() {
        mListener.onLoadingStarted();
    }

    protected void onLoadingStopped() {
        mListener.onLoadingStopped();
    }

    protected void onError(final Exception e)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mErrorListener.onError(e);
            }
        });
    }

    protected DoneApplication getApplication() {
        return (DoneApplication)getActivity().getApplication();
    }

    protected com.google.api.services.tasks.Tasks getService() {
        return getApplication().getService();
    }
}
