package com.journaldev.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class BehaviorsListActivity extends AppCompatActivity {

    private DBBehaviorsManager dbManager;

    private ListView listView;
    private TextView textView;
    public String itemId;
    public String itemName;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseBehaviorsHelper._ID,
            DatabaseBehaviorsHelper.CONSEQUENCENAME, DatabaseBehaviorsHelper.CONSEQUENCESORT };

    final int[] to = new int[] { R.id.id, R.id.consequence_name_textview, R.id.consequence_sort_textview };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get rid of annoying keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_behaviors_list);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        //This messed with me for TOO LONG!  DataItemAdapter  ITEM_ID = "item_id" and thats whats needed
        if (bundle != null) {
            itemId = bundle.getString("item_id");
            itemName = bundle.getString("item_name");
        }

        dbManager = new DBBehaviorsManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        //Cursor cursor = dbManager.fetchCountriesByName(itemId);


//        Toast.makeText(this, "(IncidentListActivity)You selected " + itemId + " " + itemName,
//                Toast.LENGTH_SHORT).show();

        textView = (TextView) findViewById(R.id.textView);
        String studentname = itemName;
        textView.setText(studentname);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        //adapter = new SimpleCursorAdapter(this, R.layout.behavior_view_record, cursor, from, to, 0);
        adapter = new SimpleCursorAdapter(this, R.layout.behaviors_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView studentidTextView = (TextView) view.findViewById(R.id.consequence_name_textview);
                TextView studentnameTextView = (TextView) view.findViewById(R.id.consequence_sort_textview);

                String id = idTextView.getText().toString();
                String studentid = studentidTextView.getText().toString();
                String studentname = studentnameTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyBehaviorsActivity.class);
                modify_intent.putExtra("studentid", studentid);
                modify_intent.putExtra("studentname", studentname);
                modify_intent.putExtra("id", id);


                //modify_intent.putExtra("item_id", itemId);
                //modify_intent.putExtra("item_name", itemName);
                startActivity(modify_intent);
            }
        });
/*
        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbManager.fetchCountriesByName(constraint.toString());
            }
        });
*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddBehaviorsActivity.class);
//            Toast.makeText(this, "You selected " + itemId,
//                    Toast.LENGTH_SHORT).show();
            add_mem.putExtra("item_id", itemId);
            add_mem.putExtra("item_name", itemName);
            //this is the one for add.  See above for modify
            startActivity(add_mem);

        }
        //THIS IS THE OTHER ICON ON THE TOP.  SORT OF A SETTINGS LOOKING THING
        if (id == R.id.view_record) {

            //Intent view_mem = new Intent(this, BehaviorCheckListActivity.class);
            //startActivity(view_mem);
        }

        //THIS IS THE OTHER ICON ON THE TOP.  SORT OF A SETTINGS LOOKING THING
        if (id == R.id.another_record) {

            //Intent view_mem = new Intent(this, ImportMainActivity.class);
            //startActivity(view_mem);
        }

        return super.onOptionsItemSelected(item);
    }

}