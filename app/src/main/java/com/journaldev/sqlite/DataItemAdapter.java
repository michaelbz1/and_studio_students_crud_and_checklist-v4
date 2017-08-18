package com.journaldev.sqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.journaldev.sqlite.model.DataItem;

import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_KEY = "item_key";
    private List<DataItem> mItems;
    private Context mContext;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public DataItemAdapter(Context context, List<DataItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                Log.i("preferences", "onSharedPreferenceChanged: " + key);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(prefsListener);

        boolean grid = settings.getBoolean(
                mContext.getString(R.string.pref_display_grid), false);
        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);
        holder.tvName.setText(item.getItemName());
//        Toast.makeText(mContext, "(setOnClickListener)You selected " + item,
//                Toast.LENGTH_SHORT).show();
//        try {
//            holder.tvName.setText(item.getItemName());
//            String imageFile = item.getImage();
//            InputStream inputStream = mContext.getAssets().open(imageFile);
//            Drawable d = Drawable.createFromStream(inputStream, null);
//            holder.imageView.setImageDrawable(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = item.getItemId();
//                Toast.makeText(mContext, "(setOnClickListener)You selected " + itemId,
//                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, IncidentListActivity.class);
                intent.putExtra(ITEM_KEY, item);
                intent.putExtra(ITEM_ID, item.getItemId());
                intent.putExtra(ITEM_NAME, item.getItemName());
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "You long clicked " + item.getItemName(),
                Toast.makeText(mContext, "(DataItemAdapter) You long clicked " + item.getItemId(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }
}