package com.journaldev.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class StudentCheckListActivity extends AppCompatActivity {


    private DBStudentManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseStudentHelper._ID,
            DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER };

    final int[] to = new int[] { R.id.id, R.id.studentid, R.id.studentname, R.id.studentper };
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_emp_list);

        dbManager = new DBStudentManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.student_checklist_activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

//        // OnCLickListiner For List Items
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
//                TextView idTextView = (TextView) view.findViewById(R.id.id);
//                TextView studentidTextView = (TextView) view.findViewById(R.id.studentid);
//                TextView studentnameTextView = (TextView) view.findViewById(R.id.studentname);
//                TextView studentperTextView = (TextView) view.findViewById(R.id.studentper);
//
//                String id = idTextView.getText().toString();
//                String studentid = studentidTextView.getText().toString();
//                String studentname = studentnameTextView.getText().toString();
//                String studentper = studentperTextView.getText().toString();
//
//                //Intent modify_intent = new Intent(getApplicationContext(), ModifyStudentActivity.class);
//                Intent modify_intent = new Intent(getApplicationContext(), DetailActivity.class);
////                modify_intent.putExtra("studentid", studentid);
////                modify_intent.putExtra("studentname", studentname);
////                modify_intent.putExtra("studentper", studentper);
////                modify_intent.putExtra("id", id);
//
//                startActivity(modify_intent);
//            }
//        });


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

        }
        //THIS IS MY ADDITION TO SEE IF I CAN RUN ANOTHER ACTIVITY
        if (id == R.id.view_record) {


        }

        return super.onOptionsItemSelected(item);
    }
 }