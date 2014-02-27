package com.roosi.done.done;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TaskFragment(title, null))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task, menu);

        //menu.findItem(R.id.action_save_task).setEnabled(false);

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
            Toast.makeText(this, R.string.toast_task_saved, Toast.LENGTH_SHORT).show();
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
}
