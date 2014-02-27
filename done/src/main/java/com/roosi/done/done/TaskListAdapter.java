package com.roosi.done.done;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.services.tasks.model.TaskList;

/**
 * Created by jtn on 26/02/14.
 */
public class TaskListAdapter extends ArrayAdapter<TaskList> {

    private final int mResource;
    private final int mTextViewResourceId;

    public TaskListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);

        mResource = resource;
        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView)view.findViewById(mTextViewResourceId);
        textView.setText(getItem(position).getTitle());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
