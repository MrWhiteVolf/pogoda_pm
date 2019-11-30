package com.example.pogoda;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.pogoda.receivers.ConnectionReceiver;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver MyReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyReceiver = new ConnectionReceiver();

        final Fragment fragment1 = new BlankFragment();
        final Fragment fragment2 = new BlankFragment2();
        String cityName = "Warsaw";

        Bundle bundle = new Bundle();
        bundle.putString("CITY",cityName);
        fragment1.setArguments(bundle);
        fragment2.setArguments(bundle);
        Button button1 = findViewById(R.id.fragment1button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment1);
                ft.commit();
            }
        });

        Button button2 = findViewById(R.id.fragment2button);
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
