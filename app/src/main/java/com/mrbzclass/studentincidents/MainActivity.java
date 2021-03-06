package com.mrbzclass.studentincidents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mrbzclass.studentincidents.database.StudentDataSource;
import com.mrbzclass.studentincidents.model.DataItem;
import com.mrbzclass.studentincidents.sample.SampleDataProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    String choosen_period;
    private static final String TAG = "MainActivity";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    StudentDataSource mDataSource;
    List<DataItem> listFromDB;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Get periods from database
        DatabaseClassPeriodsHelper db = new DatabaseClassPeriodsHelper(getApplicationContext());
        List<String> periods = db.getAllPeriods();

        //List<String> stockList = db.getAllPeriods();

        if(periods!=null){
            String[] stringArray = periods.toArray(new String[0]);
            mCategories = stringArray;

        }else
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
                String category = mCategories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                //globalVarValue = category;

                SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                e.putString("period_chosen", category);
                e.commit();

                displayDataItems(category);
            }
        });
//      end of navigation drawer

        mDataSource = new StudentDataSource(this);
        mDataSource.open();
        mDataSource.seedDatabase(dataItemList);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        displayDataItems(null);
    }

//Populate the student list from the StudentDataSource.
    private void displayDataItems(String category) {
        listFromDB = mDataSource.getAllItems(category);
        mItemAdapter = new DataItemAdapter(this, listFromDB);
        mRecyclerView.setAdapter(mItemAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();

        String lastPeriod = PreferenceManager.getDefaultSharedPreferences(this).getString("period_chosen", null);
        //Toast.makeText(MainActivity.this, "You chose " + lastPeriod,
        //        Toast.LENGTH_SHORT).show();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        if (lastPeriod != null) {
            displayDataItems(lastPeriod);}
        else
        {
            displayDataItems(null);
        }

        // get last open Activity
        String lastActivity = PreferenceManager.getDefaultSharedPreferences(this).getString("last_activity", "");
        //Toast.makeText(MainActivity.this, "You chose " + lastActivity,
        //        Toast.LENGTH_SHORT).show();
        if (lastActivity == "Reset") {
            SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            e.putString("period_chosen", null);
            e.commit();
            displayDataItems(null);

            //Toast.makeText(MainActivity.this, "it reset" + lastActivity,
            //        Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //This is somewhat confusing.  This is the edit menu
            case R.id.action_students:
                Intent intent = new Intent(this, StudentListActivity.class);
                startActivity(intent);
                return true;
            //This is the menu item next to settings
            case R.id.view_records:
                //Intent list_intent = new Intent(this, IncidenbystudentActivity.class);
                Intent list_intent = new Intent(this, IncidentsbystudentExport.class);
                startActivity(list_intent);
                return true;
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_all_items:
                // display all items
                SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                e.putString("period_chosen", null);
                e.commit();
                displayDataItems(null);
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(SigninActivity.EMAIL_KEY, email);
            editor.apply();

        }

    }
    @Override
    public void onBackPressed() {
    }
}
