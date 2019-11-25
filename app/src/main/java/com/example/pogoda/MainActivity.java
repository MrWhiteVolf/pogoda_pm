package com.example.pogoda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView temperatura = findViewById(R.id.temperatura);
        TextView wilgotnosc = findViewById(R.id.wilgotnosc);
        TextView predkosc = findViewById(R.id.predkosc);
        temperatura.setText("20st" /*do dorobienia z shared albo sqlite*/);
        temperatura.setText("50%" /*do dorobienia z shared albo sqlite*/);
        temperatura.setText("5m/s" /*do dorobienia z shared albo sqlite*/);
    }
}
