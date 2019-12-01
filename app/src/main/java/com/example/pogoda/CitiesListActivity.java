package com.example.pogoda;

import android.content.Intent;
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
    String[] version = {"Aestro","Wilno","Konin","Donut","Radom","Cipki","GingerBread","HoneyComb","IceCream Sandwich",
            "Jelly Bean","Kitkat","Lolipop","Marshmallow","Nought","Oreo","Krakow","Warszawa","Lodz","Rzeszow","Jaslo","Jodlowa"};
    //Tę liste trzeba z bazki pobrać
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);


        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.searchView);

        list = new ArrayList<>();

        for (int i = 0;i<version.length;i++){
            list.add(version[i]);
        }

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
