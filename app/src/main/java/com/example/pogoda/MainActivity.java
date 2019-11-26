package com.example.pogoda;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Fragment fragment1 = new BlankFragment();
        final Fragment fragment2 = new BlankFragment2();

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
}
