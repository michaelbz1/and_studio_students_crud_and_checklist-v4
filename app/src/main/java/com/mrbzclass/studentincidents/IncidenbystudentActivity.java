package com.mrbzclass.studentincidents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;

public class IncidenbystudentActivity extends ExpandableListActivity {

    private static final String NAME = "NAME";

    private ExpandableListAdapter mAdapter;

    //private String group[] = {"Development" , "Data Process Team"};
    //private String[][] child = { { "John", "Bill" }, { "Alice", "David" } };
    private String group[];
    private String[][] child;
    //private String group[];
    //private String[][] child;

    DrawerLayout mDrawerLayout;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DBIncidentManager dbManager;
    private DBStudentManager dbManager2;

    int list_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

//      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ////////////////////////////////////////////////////////////

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        dbManager = new DBIncidentManager(this);
        dbManager.open();
        //Cursor cursor = dbManager.fetch();
        String[] incident_data_list = {};
        //ArrayList<String>[] incident_data_contents = new ArrayList[200];

        dbManager2 = new DBStudentManager(this);
        dbManager2.open();
        Cursor studentcursor = dbManager2.fetch();
        Cursor studentcursorcount = dbManager2.fetchCount();
        int total_students = studentcursorcount.getInt(0);
        int total_students_with_incidents = 0;

        String[] child_id = new String [total_students];
        String[] student_name = new String [total_students];

        int incidents_max = 0;

        studentcursor.moveToFirst();
        list_count = 0;
        while (list_count < total_students) {
            String id_data = studentcursor.getString(0);  //student id
            student_name[total_students_with_incidents] = studentcursor.getString(2);  //student name
            Cursor incidentcursorcount = dbManager.fetchIncidentsCount(id_data);
            int total_incidents = incidentcursorcount.getInt(0);
            if (total_incidents > 0) {
                if (total_incidents > incidents_max)
                    incidents_max = total_incidents;
                child_id[total_students_with_incidents] = id_data;
                total_students_with_incidents++;
            }
            studentcursor.moveToNext();
            list_count++;
        }
        total_students = total_students_with_incidents;
        //incidents_max is the highest number of incidents in the list of incidents

        String group[] = new String[total_students];
        String[][] child = new String[total_students][incidents_max];
        String[][] childname = new String[total_students][incidents_max];
        String[][] childdate = new String[total_students][incidents_max];
        String[][] childcons = new String[total_students][incidents_max];
        String[][] childparent = new String[total_students][incidents_max];
        int child_size[] = new int[total_students];
        int record_count = 0;

        list_count = 0;
        while (list_count < total_students) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            boolean pref_sort = settings.getBoolean(getString(R.string.pref_sort_by_date), false);

            Cursor incidentcursor;
            if (pref_sort) {
                incidentcursor = dbManager.fetchIncidentsByDate(child_id[list_count]);
            }else
                 incidentcursor = dbManager.fetchIncidentsByName(child_id[list_count]);

            Cursor incidentcursorcount = dbManager.fetchIncidentsCount(child_id[list_count]);
            int total_incidents = incidentcursorcount.getInt(0);
            record_count = 0;
            incidentcursor.moveToFirst();
            if (total_incidents < 10)
                group[list_count] =  " " + total_incidents + "  " + student_name[list_count];
            else
                group[list_count] =  total_incidents + "  " + student_name[list_count];

            do {
                //incident_data_list[record_count] = incidentcursor.getString(2).toString();
                childname[list_count][record_count] = incidentcursor.getString(2).toString(); //name of behavior
                childdate[list_count][record_count] = incidentcursor.getString(3).toString(); //date
                childcons[list_count][record_count] = incidentcursor.getString(4).toString(); //consequence
                childparent[list_count][record_count] = incidentcursor.getString(5).toString(); //parent contact
                child[list_count][record_count] = childdate[list_count][record_count] + " - " + childname[list_count][record_count] + " - " + childcons[list_count][record_count] + " - " + childparent[list_count][record_count];
                incidentcursor.moveToNext();
                record_count++;
            }while (record_count < total_incidents);
            child_size[list_count]=total_incidents;
            studentcursor.moveToNext();
            list_count++;
        }

        dbManager.close();
        dbManager2.close();

        ////////////////////////////////////////////////////////////


        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();


        for (int i = 0; i < total_students_with_incidents; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, group[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < child_size[i]; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, child[i][j]);
            }
            childData.add(children);
        }

        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(this,
                groupData,android.R.layout.simple_expandable_list_item_1,new String[] { NAME }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2, new String[] { NAME }, new int[] { android.R.id.text1 });
                //groupData,android.R.layout.ex,new String[] { NAME }, new int[] { android.R.id.text1 },
                //childData, android.R.layout.expandable_list_content, new String[] { NAME }, new int[] { android.R.id.text1 });
        setListAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.behaviorsbystudent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddStudentActivity.class);
            startActivity(add_mem);

        }
        if (id == R.id.action_settings) {
            // Show the settings screen
            Intent settingsIntent = new Intent(this, PrefsActivity1.class);
            startActivity(settingsIntent);
        }
        if (id == R.id.another_record) {


        }

        return super.onOptionsItemSelected(item);
    }



}