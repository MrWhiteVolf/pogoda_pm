package com.example.pogoda;

import android.content.*;
import android.net.ConnectivityManager;
import android.content.IntentFilter;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.*;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.pogoda.receivers.ConnectionReceiver;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver MyReceiver = null;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyReceiver = new ConnectionReceiver();

        final Fragment fragment1 = new BlankFragment();
        final Fragment fragment2 = new BlankFragment2();
        final Fragment lastSelected;

        pref = getSharedPreferences("WeatherAppPref", Context.MODE_WORLD_READABLE); // 0 - for private mode
        String cityName;
        try {
            cityName = getIntent().getExtras().getString("SELECTED_CITY");
            if (cityName == null) throw new NullPointerException();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("LAST_SELECTED_CITY", cityName);
            editor.commit();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        finally {
            cityName = pref.getString("LAST_SELECTED_CITY","Warszawa");
            Bundle bundle = new Bundle();
            bundle.putString("CITY",cityName);
            fragment1.setArguments(bundle);
            fragment2.setArguments(bundle);
        }

        if(MyIntentService.dday == 0){
            lastSelected = fragment1;
            System.out.println("fajno");
        }else{
            lastSelected = fragment2;
            System.out.println("nie fajno");
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,lastSelected);
        ft.commit();
        Button selectCity2 = findViewById(R.id.select_city2);
        selectCity2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        Button selectCity = findViewById(R.id.select_city);
        selectCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, CitiesListActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        final Button button1 = findViewById(R.id.fragment1button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment1);
                ft.commit();
            }
        });

        final Button button2 = findViewById(R.id.fragment2button);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment2);
                ft.commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastIntent();
    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }
}
