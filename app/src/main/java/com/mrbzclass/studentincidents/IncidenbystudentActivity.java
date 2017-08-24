package com.mrbzclass.studentincidents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.os.Bundle;
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


    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DBIncidentManager dbManager;
    private DBStudentManager dbManager2;
    int list_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        String group[] = new String[total_students];
        String[][] child = new String[total_students][250];

        studentcursor.moveToFirst();
        list_count = 0;
        while (total_students > list_count) {
            String id_data = studentcursor.getString(0);  //student id
            String data = studentcursor.getString(2);  //student name

            Cursor incidentcursor = dbManager.fetchIncidentsByName(id_data);
            Cursor incidentcursorcount = dbManager.fetchIncidentsCount(id_data);
            int total_incidents = incidentcursorcount.getInt(0);
            //int total_records = incidentcursor.getInt();
            if (total_incidents > 0) {
                int record_count = 0;
                //listDataHeader.add(list_count,data);
                incidentcursor.moveToFirst();
                group[list_count] = data + " " + total_incidents;
                do {
                    //incident_data_list[record_count] = incidentcursor.getString(2).toString();
                    child[list_count][record_count] = incidentcursor.getString(2).toString();
                    incidentcursor.moveToNext();
                    record_count++;
                }while (record_count < total_incidents);
                //listDataChild.put(listDataHeader.get(list_count), incident_data_list);
                //child[list_count][record_count] = "Bill";
                //incident_data_list.clear();
                studentcursor.moveToNext();
                list_count++;
            }
        }
        dbManager.close();
        dbManager2.close();

        ////////////////////////////////////////////////////////////


        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();


        for (int i = 0; i < group.length; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, group[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < child[i].length; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, child[i][j]);
            }
            childData.add(children);
        }

        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(this, groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME }, new int[] { android.R.id.text1 });
        setListAdapter(mAdapter);
    }

}