package com.roosi.done.done;

/**
 * Created by jtn on 24/02/14.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TasksFragment extends Fragment {

    public interface OnLoadingListener {
        public void onLoadingStarted();
        public void onLoadingStopped();
    }

    public static TasksFragment newInstance(TaskList taskList, com.google.api.services.tasks.Tasks service) {
        TasksFragment fragment = new TasksFragment(taskList, service);
        return fragment;
    }

    private TasksAdapter mTasksAdapter;
    private AbsListView taskListView;
    private TaskList mTaskList;
    private OnLoadingListener mListener;

    private com.google.api.services.tasks.Tasks mService;

    public TasksFragment(TaskList taskList, com.google.api.services.tasks.Tasks service) {
        mTaskList = taskList;
        mService = service;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        taskListView = (AbsListView) rootView.findViewById(R.id.taskListView);

        mTasksAdapter = new TasksAdapter(getActivity(),
                R.layout.list_item_task,
                R.id.textViewTitle);

        taskListView.setAdapter(mTasksAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("title", mTasksAdapter.getItem(i).getTitle());
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.onLoadingStarted();;

        new AsyncTask<Void, Void, Tasks>()
        {
            @Override
            protected Tasks doInBackground(Void... voids) {
                Tasks tasks = null;
                try {
                    tasks = mService.tasks().list(mTaskList.getId()).execute();
                } catch (UserRecoverableAuthIOException e) {
                    //startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                } catch (IOException e) {
                    final String message = e.getLocalizedMessage();


                }
                return tasks;
            }

            @Override
            protected void onPostExecute(Tasks tasks) {
                super.onPostExecute(tasks);
                mListener.onLoadingStopped();
                mTasksAdapter.clear();
                if(tasks != null) {
                    mTasksAdapter.addAll(tasks.getItems());
                }
            }
        }.execute();
    }
}

