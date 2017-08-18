package com.journaldev.sqlite;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class AddStudentActivity extends Activity implements OnClickListener {

    private Button addTodoBtn;
    private EditText studentidEditText;
    private EditText studentnameEditText;
    private EditText studentperEditText;

    private DBStudentManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_add_record);

        studentidEditText = (EditText) findViewById(R.id.studentid_edittext);
        studentnameEditText = (EditText) findViewById(R.id.studentname_edittext);
        studentperEditText = (EditText) findViewById(R.id.studentper_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBStudentManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                final String itemId = UUID.randomUUID().toString();
                final String studentid = studentidEditText.getText().toString();
                final String studentname = studentnameEditText.getText().toString();
                final String studentper = studentperEditText.getText().toString();

                dbManager.insert(itemId, studentid, studentname, studentper);

                Intent main = new Intent(AddStudentActivity.this, StudentListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}