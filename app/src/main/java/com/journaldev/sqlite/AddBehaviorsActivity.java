package com.journaldev.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddBehaviorsActivity extends Activity implements OnClickListener {


    private Button addTodoBtn;
    private EditText ConsequenceNameEditText;
    private EditText ConsequenceSortText;

    private DBBehaviorsManager dbManager;

    public String itemId;
    public String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        //This messed with me for TOO LONG!  DataItemAdapter  ITEM_ID = "item_id" and thats whats needed
        itemId =  bundle.getString("item_id");
        itemName =  bundle.getString("item_name");

        setTitle("Add Behaviors Record");

        setContentView(R.layout.activity_add_behaviors_record);

        ConsequenceNameEditText = (EditText) findViewById(R.id.consequencename_edittext);
        ConsequenceSortText = (EditText) findViewById(R.id.consequencesort_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBBehaviorsManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String studentid = ConsequenceNameEditText.getText().toString();
                final String studentper = ConsequenceSortText.getText().toString();


                dbManager.insert(studentid, studentper);

                Intent main = new Intent(AddBehaviorsActivity.this, BehaviorsListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("item_id", itemId);
                main.putExtra("item_name", itemName);
                startActivity(main);
                break;
        }
    }


}