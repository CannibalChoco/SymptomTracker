package com.example.user.symptomtracker.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.symptomtracker.R;

import butterknife.ButterKnife;

/**
 * A fragment for displaying an all of the resolved symptoms
 */
public class ResolvedFragment extends Fragment {


    public ResolvedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resolved, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

}
