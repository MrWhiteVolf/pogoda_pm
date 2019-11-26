package com.example.pogoda;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private View rootView;
    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView tv = (TextView) inf.findViewById(R.id.temperatura);
        tv.setText("GORĄCO BARDZO");    //albo zapytanie do api
        return inf;
    }



}
