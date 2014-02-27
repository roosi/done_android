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
import java.util.Locale;

public class TaskFragment extends BaseFragment {

    final DateFormat UiFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    final SimpleDateFormat Rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
                getActivity().getActionBar().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.status_completed)));
            }
        });

        Calendar c = Calendar.getInstance();

        mButtonDate.setText(UiFormat.format(c.getTime()));

        mDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mButtonDate.setText(UiFormat.format(new Date(year, monthOfYear, dayOfMonth)));
                    }
                });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Task task = getSelectedTask();

        try {
            Date dueDate = Rfc3339Format.parse(task.getDue().toStringRfc3339());

            Calendar c = Calendar.getInstance();
            c.setTime(dueDate);
            mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        mEditTextNotes.setText(task.getNotes());

    }
}
