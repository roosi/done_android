package com.roosi.done.done;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        String title = getIntent().getStringExtra("title");

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        actionBar.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.status_due_closing)));
        actionBar.setTitle(title);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_task)
        {
            finish();
            return true;
        }
        else if (id == R.id.action_delete_task)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private DatePicker mDatePicker;
        private Button mButtonDate;
        private View mButtonDone;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_task, container, false);
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

            final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            mButtonDate.setText(df.format(c.getTime()));

            mDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                    new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    mButtonDate.setText(df.format(new Date(year, monthOfYear, dayOfMonth)));
                }
            });

            return rootView;
        }
    }
}
