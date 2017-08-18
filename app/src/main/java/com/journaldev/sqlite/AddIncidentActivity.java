package com.journaldev.sqlite;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class AddIncidentActivity extends Activity implements OnClickListener {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private Button addTodoBtn;
    private EditText behavioridEditText;
    private EditText behaviorNameEditText;
    private EditText behaviorDateEditText;
    private EditText behaviorConsEditText;
    private EditText behaviorParentContactEditText;
    private EditText behaviorCommentEditText;

    Spinner myconsSpinner;
    Spinner myBehaviorsSpinner;

    private DBIncidentManager dbManager;

    public String itemId;
    public String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        //This messed with me for TOO LONG!  DataItemAdapter  ITEM_ID = "item_id" and thats what is needed
        itemId =  bundle.getString("item_id");
        itemName =  bundle.getString("item_name");

        //setTitle("Add Incident Record for " + itemId);
        setTitle("Add Incident Record");

        setContentView(R.layout.activity_add_incident_record);

        behavioridEditText = (EditText) findViewById(R.id.behaviorid_edittext);
        behavioridEditText.setText(itemId.toString());

        myconsSpinner=(Spinner) findViewById(R.id.spinner_consequence);
        loadSpinnerData();

        myBehaviorsSpinner=(Spinner) findViewById(R.id.behaviorname_edittext);
        loadBehaviorsSpinnerData();

        String behaviorNameEditText = myBehaviorsSpinner.getSelectedItem().toString();
        //behaviorNameEditText = (EditText) findViewById(R.id.behaviorname_edittext);

        //behaviorDateEditText = (EditText) findViewById(R.id.behaviordate_edittext);
        behaviorDateEditText = (EditText) findViewById(R.id.behaviordate_edittext);

        behaviorCommentEditText = (EditText) findViewById(R.id.et_comments);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBIncidentManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String studentid = behavioridEditText.getText().toString();
                Spinner myBehaviorsSpinner=(Spinner) findViewById(R.id.behaviorname_edittext);
                //loadBehaviorsSpinnerData();

                String studentname = myBehaviorsSpinner.getSelectedItem().toString();
                //final String studentname = behaviorNameEditText.getText().toString();
                final String studentper = behaviorDateEditText.getText().toString();

                Spinner myconsSpinner=(Spinner) findViewById(R.id.spinner_consequence);
                String studentcons = myconsSpinner.getSelectedItem().toString();

                //loadSpinnerData();

                Spinner myParentContactSpinner=(Spinner) findViewById(R.id.spinner_parent_contact);
                String studentParentContact = myParentContactSpinner.getSelectedItem().toString();
                //final String studentname = behaviorNameEditText.getText().toString();
                final String studentComment = behaviorCommentEditText.getText().toString();

                dbManager.insert(studentid, studentname, studentper, studentcons, studentParentContact, studentComment);

                Intent main = new Intent(AddIncidentActivity.this, IncidentListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("item_id", itemId);
                main.putExtra("item_name", itemName);
                startActivity(main);
                break;
        }
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        behaviorDateEditText.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        DatabaseConsequenceHelper db = new DatabaseConsequenceHelper(getApplicationContext());
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        myconsSpinner.setAdapter(dataAdapter);
    }

    private void loadBehaviorsSpinnerData() {
        DatabaseBehaviorsHelper db = new DatabaseBehaviorsHelper(getApplicationContext());
        List<String> behaviors = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, behaviors);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        myBehaviorsSpinner.setAdapter(dataAdapter);
    }
    //@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }

    //@Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Auto-generated method stub

    }
}