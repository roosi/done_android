package com.roosi.done.done;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.services.tasks.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jtn on 27/02/14.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    final DateFormat UiFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    final SimpleDateFormat Rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private int mTextViewResourceId;

    public TasksAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);

        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Task task = getItem(position);

        TextView textView = (TextView)view.findViewById(mTextViewResourceId);
        textView.setText(task.getTitle());

        TextView textViewDue = (TextView)view.findViewById(R.id.textViewDue);
        try {
            Date dueDate = Rfc3339Format.parse(task.getDue().toStringRfc3339());
            textViewDue.setText(UiFormat.format(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
