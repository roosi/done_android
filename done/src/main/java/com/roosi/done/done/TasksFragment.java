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

public class TasksFragment extends BaseFragment {

    public static TasksFragment newInstance(TaskList taskList) {
        TasksFragment fragment = new TasksFragment(taskList);
        return fragment;
    }

    private TasksAdapter mTasksAdapter;
    private AbsListView taskListView;
    private TaskList mTaskList;

    public TasksFragment(TaskList taskList) {
        mTaskList = taskList;
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
                setSelectedTask(mTasksAdapter.getItem(i));
                startActivity(new Intent(getActivity(), TaskActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        super.onLoadingStarted();

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
                    onError(e);
                }
                return tasks;
            }

            @Override
            protected void onPostExecute(Tasks tasks) {
                super.onPostExecute(tasks);
                TasksFragment.super.onLoadingStopped();
                mTasksAdapter.clear();
                if(tasks != null) {
                    mTasksAdapter.addAll(tasks.getItems());
                }
            }
        }.execute();
    }
}

