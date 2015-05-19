package io.normyle.braggingrights;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.matthew.braggingrights.R;

public class PersonhoodFragment extends Fragment {

    TextView txtviewComingSoon;

    public PersonhoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personhood, container, false);

        txtviewComingSoon = (TextView) view.findViewById(R.id.txtview_coming_soon);
        txtviewComingSoon.setTextColor(Color.GRAY);

        return view;

    }
}
