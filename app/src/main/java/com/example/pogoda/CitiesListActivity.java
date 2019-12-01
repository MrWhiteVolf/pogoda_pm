package com.example.pogoda;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class CitiesListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter adapter;
    FeedReaderDbHelper myDatabase;
//    String[] version = {"Aestro","Wilno","Konin","Donut","Radom","Cipki","GingerBread","HoneyComb","IceCream Sandwich",
//            "Jelly Bean","Kitkat","Lolipop","Marshmallow","Nought","Oreo","Krakow","Warszawa","Lodz","Rzeszow","Jaslo","Jodlowa"};
//    //Tę liste trzeba z bazki pobrać
    SearchView searchView;

    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "CITIES";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS CITIES_T(Name VARCHAR)");
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL("DROP TABLE CITIES_T");
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);
        myDatabase = new FeedReaderDbHelper(this);
        SQLiteDatabase w = myDatabase.getWritableDatabase();
        SQLiteDatabase readable = myDatabase.getReadableDatabase();

        String[] projection = {
                "Name"
        };

        String sortOrder =
                "Name" + " ASC";

        Cursor result = readable.query(
                "CITIES_T",   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        while(result.moveToNext()) {
            String itemId = result.getString(
                    result.getColumnIndexOrThrow("Name"));
            list.add(itemId);
        }
        result.close();


        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.searchView);

//        for (int i = 0;i<version.length;i++){
//            list.add(version[i]);
//        }

        adapter = new ArrayAdapter(CitiesListActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                Intent myIntent = new Intent(CitiesListActivity.this, MainActivity.class);
                bundle.putString("SELECTED_CITY",adapterView.getItemAtPosition(i).toString());
                myIntent.putExtras(bundle);
                CitiesListActivity.this.startActivity(myIntent);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Bundle bundle = new Bundle();
                Intent myIntent = new Intent(CitiesListActivity.this, MainActivity.class);
                bundle.putString("SELECTED_CITY",s);
                ContentValues values = new ContentValues();
                values.put("Name", s);
                myDatabase.getWritableDatabase().insert("CITIES_T", null, values);
                myIntent.putExtras(bundle);
                CitiesListActivity.this.startActivity(myIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                return true;
            }
        });
    }
}
