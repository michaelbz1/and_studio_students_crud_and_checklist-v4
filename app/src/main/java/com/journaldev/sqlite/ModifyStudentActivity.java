package com.journaldev.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModifyStudentActivity extends Activity implements OnClickListener {

    private EditText studentidText;
    private EditText studentnameText;
    private EditText studentperText;
    private Button updateBtn, deleteBtn;

    private String _id;

    private DBStudentManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.activity_modify_record);

        dbManager = new DBStudentManager(this);
        dbManager.open();

        studentidText = (EditText) findViewById(R.id.studentid_edittext);
        studentnameText = (EditText) findViewById(R.id.studentname_edittext);
        studentperText = (EditText) findViewById(R.id.studentper_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String studentid = intent.getStringExtra("studentid");
        String name = intent.getStringExtra("studentname");
        String period = intent.getStringExtra("studentper");

        //_id = Long.parseLong(id);
        _id = id;  //changed this because it was giving an error 'invalid long: "4545345345343-345-345345-"

        studentidText.setText(studentid);
        studentnameText.setText(name);
        studentperText.setText(period);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String studentid = studentidText.getText().toString();
                String  studentname = studentnameText.getText().toString();
                String studentper = studentperText.getText().toString();

                dbManager.update(_id, studentid, studentname, studentper);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), StudentListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
