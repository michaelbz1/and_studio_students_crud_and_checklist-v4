package com.mrbzclass.studentincidents;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IncidenbystudentActivity2 extends AppCompatActivity {

    private DBIncidentManager dbManager;
    private DBStudentManager dbManager2;

    private ListView listView;
    private TextView textView;
    public String itemId;
    public String itemName;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int list_count = 0;
//////
    private SimpleCursorAdapter adapter2;
    final String[] from2 = new String[] { DatabaseStudentHelper._ID, DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER };
    final int[] to2 = new int[] { R.id.id, R.id.studentid, R.id.studentname, R.id.studentper };
    //////

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { databaseIncidentHelper._ID,
            databaseIncidentHelper.BEHAVIORID, databaseIncidentHelper.BEHAVIORNAME, databaseIncidentHelper.BEHAVIORDATE,
            databaseIncidentHelper.BEHAVIORCONSEQUENCE, databaseIncidentHelper.BEHAVIORPARENTCONTACT, databaseIncidentHelper.BEHAVIORCOMMENTS };

    final int[] to = new int[] { R.id.id, R.id.studentid, R.id.studentname, R.id.studentper, R.id.studentcons, R.id.studentparentcontact, R.id.studentcomment };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get rid of annoying keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        //Set up ExpandableListAdapter
        //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        dbManager = new DBIncidentManager(this);
        dbManager.open();
        //Cursor cursor = dbManager.fetch();
        List<String> incident_data_list = new ArrayList();
        //ArrayList<String>[] incident_data_contents = new ArrayList[200];

        dbManager2 = new DBStudentManager(this);
        dbManager2.open();
        Cursor studentcursor = dbManager2.fetch();
        list_count = 0;
        while (studentcursor.moveToNext()) {
            String id_data = studentcursor.getString(0);  //student id
            String data = studentcursor.getString(2);  //student name
            Cursor incidentcursor = dbManager.fetchIncidentsByName(id_data);
            Cursor incidentcursorcount = dbManager.fetchIncidentsCount(id_data);
            int total_records = incidentcursorcount.getInt(0);
            //int total_records = incidentcursor.getInt();
            incidentcursor.moveToFirst();
            if (total_records > 0) {
                int record_count = 0;
                listDataHeader.add(list_count,data);
                do {
                    incident_data_list.add(record_count, incidentcursor.getString(2).toString());
                    incidentcursor.moveToNext();
                    record_count++;
                }while (record_count < total_records);
                listDataChild.put(listDataHeader.get(list_count), incident_data_list);
                incident_data_list.clear();
                list_count++;
            }
        }


        // Adding child data
        listDataHeader.add("Top 250");
        //listDataHeader.add("Now Showing");
        //listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        //listDataChild.put(listDataHeader.get(1), nowShowing);
        //listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
