package com.roosi.done.done;

/**
 * Created by jtn on 24/02/14.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TasksFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayAdapter<String> mTasksAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TasksFragment newInstance(GoogleAccountCredential credential, int sectionNumber) {
        TasksFragment fragment = new TasksFragment(credential);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private View progressBar;
    private AbsListView taskListView;
    private List<String> tasks;

    final HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();

    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    com.google.api.services.tasks.Tasks mService;

    public TasksFragment(GoogleAccountCredential credential) {
        mService =
                new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                        .setApplicationName("done").build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        taskListView = (AbsListView) rootView.findViewById(R.id.taskListView);
        tasks = new ArrayList<String>();

        mTasksAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_task,
                R.id.textViewTitle);

        taskListView.setAdapter(mTasksAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("title", tasks.get(i));
                startActivity(intent);
            }
        });

        progressBar = rootView.findViewById(R.id.progressBar);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        tasks.add("Task 1");
                        tasks.add("Task 2");
                        tasks.add("Task 3");
                        tasks.add("Task 4");
                        tasks.add("Task 5");
                        tasks.add("Task 6");
                        tasks.add("Task 7");
                        tasks.add("Task 8");
                        tasks.add("Task 9");
                        tasks.add("Task 10");
                        tasks.add("Task 11");
                        tasks.add("Task 12");
                        tasks.add("Task 13");
                        mTasksAdapter.addAll(tasks);
                    }
                });
            }
        }, 2000);

        return rootView;
    }
}

