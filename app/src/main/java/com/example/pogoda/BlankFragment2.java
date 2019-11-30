package com.example.pogoda;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment2 extends Fragment {

    private class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "1";
        @Override
        public void onReceive(Context context, Intent intent) {

            String avgTemp = intent.getStringExtra("avgTemp");
            String avgHumidity = intent.getStringExtra("avgHumidity");
            String avgWindSpeed = intent.getStringExtra("avgWindSpeed");
            String minTemp = intent.getStringExtra("minTemp");
            String maxTemp = intent.getStringExtra("maxTemp");

            TextView temp = (TextView) rootView.findViewById(R.id.temperatura);
            TextView windSpeed = (TextView) rootView.findViewById(R.id.predkosc);
            TextView humidity = (TextView) rootView.findViewById(R.id.wilgotnosc);

            String temperature=avgTemp + " (min:"+minTemp+" max:"+maxTemp+")";
            temp.setText(temperature);
            windSpeed.setText(avgWindSpeed);
            humidity.setText(avgHumidity);
        }
    }
    public BlankFragment2() {
        // Required empty public constructor
    }

    private MyBroadcastReceiver receiver;
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String cityName = getArguments().getString("CITY");
        String day = "1";  // 0 - Today  //1 - Tomorrow
        Intent msgIntent = new Intent(getActivity(), MyIntentService.class);
        msgIntent.putExtra(MyIntentService.DAY, day);
        msgIntent.putExtra(MyIntentService.CITY,cityName);
        getActivity().startService(msgIntent);

        rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        String dayText = getResources().getString(R.string.tomorrow);
        TextView title = (TextView) rootView.findViewById(R.id.title1);
        String titleText = cityName+","+dayText;
        title.setText(titleText);

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        MyBroadcastReceiver receiver;
        IntentFilter filter = new IntentFilter(MyBroadcastReceiver.ACTION_RESP);
        receiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

}
