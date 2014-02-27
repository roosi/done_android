package com.roosi.done.done;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.services.tasks.model.Task;

/**
 * Created by jtn on 27/02/14.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    private int mTextViewResourceId;

    public TasksAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);

        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView)view.findViewById(mTextViewResourceId);
        textView.setText(getItem(position).getTitle());
        return view;
    }
}
