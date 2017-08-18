package com.journaldev.sqlite;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ModifyConsequenceActivity extends Activity implements OnClickListener {


    private EditText studentidText;
    private TextView studentnameText;
    private EditText studentperText;
    private Button updateBtn, deleteBtn;

    private long _id;

    public String itemId;
    public String itemName;


    private DBConsequenceManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        //This messed with me for TOO LONG!  DataItemAdapter  ITEM_ID = "item_id" and thats whats needed
        itemId =  bundle.getString("item_id");
        itemName =  bundle.getString("item_name");
        //Toast.makeText(this, "Sent " + itemId + " " + itemName,
        //        Toast.LENGTH_SHORT).show();

        setTitle("Modify Record");

        setContentView(R.layout.consequence_modify_record);

        dbManager = new DBConsequenceManager(this);
        dbManager.open();

        studentidText = (EditText) findViewById(R.id.consequence_name_edittextview);
        studentnameText = (EditText) findViewById(R.id.consequence_sort_edittextview);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

//        Intent intent = getIntent();
//        String studentid = intent.getStringExtra("item_id");

        //String name = intent.getStringExtra("item_name");

        String name = intent.getStringExtra("studentid");

        String consequence = intent.getStringExtra("studentname");


        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);

        studentidText.setText(name);
        studentnameText.setText(consequence);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String studentid = studentidText.getText().toString();
                String studentname = studentnameText.getText().toString();

                dbManager.update(_id, studentid, studentname);

                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), ConsequenceListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent.putExtra("item_id", itemId);
        home_intent.putExtra("item_name", itemName);
        startActivity(home_intent);
    }

}