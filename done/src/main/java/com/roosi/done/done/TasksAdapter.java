package com.roosi.done.done;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by jtn on 27/02/14.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    final DateFormat UiFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    final SimpleDateFormat Rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    final private String StatusNeedsAction = "needsAction";
    final private String StatusCompleted = "completed";

    private int mTextViewResourceId;

    public TasksAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);

        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = super.getView(position, convertView, parent);
        }

        Task task = getItem(position);

        TextView textView = (TextView)view.findViewById(mTextViewResourceId);
        textView.setText(task.getTitle());

        TextView textViewDue = (TextView)view.findViewById(R.id.textViewDue);
        Date dueDate = null;
        try {
            dueDate = Rfc3339Format.parse(task.getDue().toStringRfc3339());
            textViewDue.setText(UiFormat.format(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FrameLayout statusTile = (FrameLayout) view.findViewById(R.id.statusTile);
        int color = R.color.status_new;

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date today = c.getTime();
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();

        if (task.getStatus().equals(StatusNeedsAction))
        {
            if (dueDate.before(today))
            {
                color = R.color.status_due;
            }
            else if (dueDate.before(tomorrow))
            {
                color = R.color.status_due_closing;
            }
            else
            {
                color = R.color.status_needs_action;
            }
        }
        else if (task.getStatus().equals(StatusCompleted))
        {
            color = R.color.status_completed;
        }

        statusTile.setBackgroundResource(color);

        return view;
    }
}
