package com.mrbzclass.studentincidents;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncidentsbystudentExport extends ExpandableListActivity {

    private static final String NAME = "NAME";

    private ExpandableListAdapter mAdapter;

    String category = null;
    //private String group[] = {"Development" , "Data Process Team"};
    //private String[][] child = { { "John", "Bill" }, { "Alice", "David" } };
    private String group[];
    private String[][] child;
    //private String group[];
    //private String[][] child;
    ListView mDrawerList;
    String[] mCategories;
    DrawerLayout mDrawerLayout;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DBIncidentManager dbManager;
    private DBStudentManager dbManager2;

    int list_count = 0;
    int total_incidents = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

//      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ////////////////////////////////////////////////////////////

        //Get periods from database
        DatabaseClassPeriodsHelper db = new DatabaseClassPeriodsHelper(getApplicationContext());
        List<String> periods = db.getAllPeriods();

        //List<String> stockList = db.getAllPeriods();

        if (periods != null) {
            String[] stringArray = periods.toArray(new String[0]);
            mCategories = stringArray;

        } else
            //this was the old code to get it from the resources
            mCategories = getResources().getStringArray(R.array.categories);

        //SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        //e.putString("period_chosen", null);
        //e.commit();

        //for(String s : stockArr)
        //    System.out.println(s);

        //mCategories = periods;
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = mCategories[position];
                Toast.makeText(IncidentsbystudentExport.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                //globalVarValue = category;

                DisplayRecords();

                //mAdapter.notifyDataSetChanged();
                //fetchCountriesByName(category);
            }
        });
//      end of navigation drawer
        DisplayRecords();
    }

    private void DisplayRecords(){

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        dbManager = new DBIncidentManager(this);
        dbManager.open();
        //Cursor cursor = dbManager.fetch();
        String[] incident_data_list = {};
        //ArrayList<String>[] incident_data_contents = new ArrayList[200];

        dbManager2 = new DBStudentManager(this);
        dbManager2.open();
        Cursor studentcursor = dbManager2.fetchCountriesByName(category);
        Cursor studentcursorcount = dbManager2.fetchCountriesByNameCount(category);
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
            total_incidents = incidentcursorcount.getInt(0);
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
        String[][] childcomment = new String[total_students][incidents_max];
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
            total_incidents = incidentcursorcount.getInt(0);
            record_count = 0;
            incidentcursor.moveToFirst();
            if (total_incidents < 10)
                group[list_count] =  "Incidents:" + " " + total_incidents + "  " + student_name[list_count];
            else
                group[list_count] =  "Incidents:" + total_incidents + "  " + student_name[list_count];


            do {
                //incident_data_list[record_count] = incidentcursor.getString(2).toString();
                childname[list_count][record_count] = incidentcursor.getString(2).toString(); //name of behavior
                childdate[list_count][record_count] = incidentcursor.getString(3).toString(); //date
                childcons[list_count][record_count] = incidentcursor.getString(4).toString(); //consequence
                childparent[list_count][record_count] = incidentcursor.getString(5).toString(); //parent contact
                childcomment[list_count][record_count] = incidentcursor.getString(6).toString(); //comment
                child[list_count][record_count] = childdate[list_count][record_count] + " - " + childname[list_count][record_count] + " - " + childcons[list_count][record_count] + " - " + childparent[list_count][record_count] + " - " + childcomment[list_count][record_count];

                incidentcursor.moveToNext();
                record_count++;
            }while (record_count < total_incidents);
            child_size[list_count]=total_incidents;
            studentcursor.moveToNext();
            list_count++;
        }

        dbManager.close();
        dbManager2.close();



        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.txt");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            //SQLiteDatabase db = dbhelper.getReadableDatabase();
            //Cursor curCSV = db.rawQuery("SELECT * FROM INCIDENTS",null);
            //csvWrite.writeNext(curCSV.getColumnNames());
            //while(curCSV.moveToNext())

            list_count = 0;
            record_count = 0;
            total_incidents=child_size[list_count];
            while (list_count < total_students) {
                do {
                    String arrStr[] = {
                            student_name[list_count],  //student name
                            childname[list_count][record_count], //name of behavior
                            childdate[list_count][record_count], //date
                            childcons[list_count][record_count], //consequence
                            childparent[list_count][record_count], //parent contact
                            childcomment[list_count][record_count] //comment
                    };
                    csvWrite.writeNext(arrStr);
                    total_incidents=child_size[list_count];
                    record_count++;
                } while (record_count < total_incidents);
                list_count++;
                record_count = 0;
            }
                csvWrite.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }




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
        if (id == R.id.action_all_items) {
            // display all items
            //displayDataItems(null);
            category = null;
            DisplayRecords();
            return true;

        }
        if (id == R.id.action_choose_category) {
            //open the drawer
            mDrawerLayout.openDrawer(mDrawerList);
            return true;

        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    private void DBtoCSV(){
        final String TAG = "IncidentDbAdapter";
        databaseIncidentHelper dbHelper;

        File dbFile=getDatabasePath("IncidentsDB.DB");
        databaseIncidentHelper dbhelper = new databaseIncidentHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.db");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM INCIDENTS",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            //{
                //Which column you want to exprort
                //String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6)};
                //csvWrite.writeNext(arrStr);
            //}
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }


    }

}