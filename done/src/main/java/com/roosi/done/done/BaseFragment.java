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

    private OnLoadingListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoadingListener)activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLoadingListener");
        }
    }

    protected void onLoadingStarted() {
        mListener.onLoadingStarted();
    }

    protected void onLoadingStopped() {
        mListener.onLoadingStopped();
    }
}
