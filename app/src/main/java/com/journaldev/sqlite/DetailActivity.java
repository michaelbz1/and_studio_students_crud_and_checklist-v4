package com.journaldev.sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.journaldev.sqlite.database.StudentDataSource;
import com.journaldev.sqlite.model.DataItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;
    private String[] mCategories;
    private ListView mList;

    StudentDataSource mDataSource;
    List<DataItem> listFromDB;
    DataItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DataItem item = getIntent().getExtras().getParcelable(DataItemAdapter.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        tvName = (TextView) findViewById(R.id.tvItemName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        tvName.setText(item.getItemName());
//        tvDescription.setText(item.getDescription());

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mList = (ListView) findViewById(R.id.recycle_categories);

        mDataSource = new StudentDataSource(this);
        mDataSource.open();
        listFromDB = mDataSource.getAllItems(null);
        mItemAdapter = new DataItemAdapter(this, listFromDB);
        mList.setAdapter(new ArrayAdapter<>(this,
                R.layout.detail_list_item, listFromDB));

        //mDataSource.seedDatabase(dataItemList);

        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

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
    }

 /*
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                displayDataItems(category);
 */

 //       tvPrice.setText(nf.format(item.getPrice()));

//        InputStream inputStream = null;
//        try {
//            String imageFile = item.getImage();
//            inputStream = getAssets().open(imageFile);
//            Drawable d = Drawable.createFromStream(inputStream, null);
//            itemImage.setImageDrawable(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
   //}
}