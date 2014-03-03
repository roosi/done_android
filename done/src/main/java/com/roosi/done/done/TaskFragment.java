package com.roosi.done.done;

/**
 * Created by jtn on 24/02/14.
 */

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TaskFragment extends BaseFragment {

    final DateFormat UiFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    final SimpleDateFormat Rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    final private String StatusNeedsAction = "needsAction";
    final private String StatusCompleted = "completed";

    private String mTitle;
    private DatePicker mDatePicker;
    private Button mButtonDate;
    private View mButtonDone;
    private EditText mEditTextTitle;
    private EditText mEditTextNotes;

    public TaskFragment(String title) {
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.status_due_closing)));
        actionBar.setTitle(mTitle);

        mEditTextTitle = (EditText)rootView.findViewById(R.id.editTextTitle);
        mEditTextTitle.setText(mTitle);
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                getActivity().getActionBar().setTitle(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEditTextNotes = (EditText)rootView.findViewById(R.id.editTextNotes);

        mDatePicker = (DatePicker)rootView.findViewById(R.id.datePicker);

        mButtonDate = (Button)rootView.findViewById(R.id.buttonDate);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePicker.setVisibility(mDatePicker.getVisibility() == View.GONE ?
                        View.VISIBLE : View.GONE);
            }
        });

        mButtonDone = rootView.findViewById(R.id.buttonDone);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = getSelectedTask();
                task.setStatus(StatusCompleted);
                setStatusColor(null);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Task task = getSelectedTask();

        Calendar c = Calendar.getInstance();
        Date dueDate = null;
        try {
            dueDate = Rfc3339Format.parse(task.getDue().toStringRfc3339());
            c.setTime(dueDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        mDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);

                        mButtonDate.setText(UiFormat.format(date.getTime()));

                        DateTime newDue = new DateTime(Rfc3339Format.format(date.getTime()));

                        Task task = getSelectedTask();
                        task.setDue(newDue);
                        setStatusColor(date.getTime());
                    }
                });

        mEditTextNotes.setText(task.getNotes());

        setStatusColor(dueDate);
    }

    private void setStatusColor(Date dueDate) {
        int color = R.color.status_new;

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date today = c.getTime();
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();

        Task task = getSelectedTask();

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

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(color)));
    }

}
