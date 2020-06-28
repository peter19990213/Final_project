package com.example.final_project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.data.FinalContract;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements

        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;
    RecyclerView myRecyclerview;
    private MyCursorAdapter myAdapter;

    public static int deleted;

    TextView longitude_label;
    EditText longitude_input;
    TextView latitude_label;
    EditText latitude_input;
    TextView name_label;
    EditText name_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        longitude_label = (TextView) findViewById(R.id.longitude_label);
        longitude_input = (EditText) findViewById(R.id.longitude_input);
        latitude_label = (TextView) findViewById(R.id.latitude_label);
        latitude_input = (EditText) findViewById(R.id.latitude_input);
        name_label = (TextView) findViewById(R.id.name_label);
        name_input = (EditText) findViewById(R.id.name_input);

        myRecyclerview = (RecyclerView) findViewById(R.id.My_recyclerView);
        myRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyCursorAdapter(this);
        myRecyclerview.setAdapter(myAdapter);
        onclick();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();

                String stringId = Integer.toString(id);
                Uri uri = FinalContract.FinalEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);
                deleted++;
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                Toast.makeText(getBaseContext(), "目前刪除個數" + Integer.toString(deleted) + "刪除之節點為" + stringId, Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(myRecyclerview);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor myLocationData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (myLocationData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(myLocationData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(FinalContract.FinalEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FinalContract.FinalEntry._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                myLocationData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        myAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }

    public void addLocation(View view){
        float longitude = Float.parseFloat(longitude_input.getText().toString());
        float latitude = Float.parseFloat(latitude_input.getText().toString());
        String name = name_input.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FinalContract.FinalEntry.COLUMN_longitude, longitude);
        contentValues.put(FinalContract.FinalEntry.COLUMN_latitude, latitude);
        contentValues.put(FinalContract.FinalEntry.COLUMN_name, name);
        Uri uri = getContentResolver().insert(FinalContract.FinalEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        onResume();

    }

    private void onclick(){
        myRecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), myRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Uri uri1 = FinalContract.FinalEntry.CONTENT_URI;
                Cursor cursor = getContentResolver().query(FinalContract.FinalEntry.CONTENT_URI, null, null, null, null);
                cursor.moveToPosition(position);
                int id = cursor.getInt(0);
                String strid = Integer.toString(id);
                float location_longitude = cursor.getFloat(1);
                float location_latitude = cursor.getFloat(2);
                //Toast.makeText(getBaseContext(),"目前節點為" + id, Toast.LENGTH_LONG).show();
                Uri uri2 = FinalContract.FinalEntry.CONTENT_URI2;
                uri2 = uri2.buildUpon().appendPath(strid).build();
                cursor = getContentResolver().query(uri2, null, null, null, null);
                int nearest = 0;
                double temp;
                double dis = 100000000;
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    if (id == cursor.getInt(0)) {
                        continue;
                    } else {
                        float another_location_longitude = cursor.getFloat(1);
                        float another_location_latitude = cursor.getFloat(2);
                        temp = Math.pow(Math.pow(location_longitude - another_location_longitude, 2) + Math.pow(location_latitude - another_location_latitude, 2), 0.5);
                        if (temp < dis) {
                            dis = temp;
                            nearest = cursor.getInt(0);
                        }
                    }

                }
                if (cursor.getCount()==1)
                    Toast.makeText(getBaseContext(), "null" , Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), "距離最近之節點為" + nearest, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Cursor cursor = getContentResolver().query(FinalContract.FinalEntry.CONTENT_URI, null, null, null, null);
                cursor.moveToPosition(position);
                float location_longitude = cursor.getFloat(1);
                float location_latitude = cursor.getFloat(2);
                //Toast.makeText(getBaseContext(), location_longitude+" "+location_latitude , Toast.LENGTH_LONG).show();

                Uri w = Uri.parse("geo:0.0?q="+location_latitude+", "+location_longitude+"(Google+Sydney)");
                Intent map = new Intent(Intent.ACTION_VIEW,w);
                if(map.resolveActivity(getPackageManager())!=null){
                    startActivity(map);
                }



            }
        }));
    }
}
