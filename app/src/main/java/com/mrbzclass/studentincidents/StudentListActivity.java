package com.mrbzclass.studentincidents;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class StudentListActivity extends ActionBarActivity {

    private DBStudentManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseStudentHelper._ID, DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER };

    final int[] to = new int[] { R.id.id, R.id.studentid, R.id.studentname, R.id.studentper };

    @Override
    public void onStart() {
        super.onStart();

        dbManager = new DBStudentManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_emp_list);

        dbManager = new DBStudentManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView studentidTextView = (TextView) view.findViewById(R.id.studentid);
                TextView studentnameTextView = (TextView) view.findViewById(R.id.studentname);
                TextView studentperTextView = (TextView) view.findViewById(R.id.studentper);

                String id = idTextView.getText().toString();
                String studentid = studentidTextView.getText().toString();
                String studentname = studentnameTextView.getText().toString();
                String studentper = studentperTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyStudentActivity.class);
                modify_intent.putExtra("studentid", studentid);
                modify_intent.putExtra("studentname", studentname);
                modify_intent.putExtra("studentper", studentper);
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddStudentActivity.class);
            startActivity(add_mem);

        }/*
        //THIS IS THE OTHER ICON ON THE TOP.  SORT OF A SETTINGS LOOKING THING
        if (id == R.id.view_record) {

            Intent view_mem = new Intent(this, StudentCheckListActivity.class);
            startActivity(view_mem);
        }
*/
        //THIS IS THE OTHER ICON ON THE TOP.  SORT OF A SETTINGS LOOKING THING
        if (id == R.id.another_record) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("You clicked on the button to upload students from a CSV file.  This will wipe out all existing students and student data.  This is meant to be done only once at the beginning of a school year.  Download the CSV file to your phone and use the FILE MANAGER on the next screen to import student names into the app from the CSV file.  The CSV file should be a simple text file in the format: 'studentid, studentname, period' (commas are necessary, no quotes)   Each new student should be on their own line.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent view_mem = new Intent(StudentListActivity.this, ImportMainActivity.class);
                            startActivity(view_mem);
                        }
                    });
            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

}