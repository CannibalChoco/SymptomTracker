package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying an overview of all the symptoms
 */
public class OverviewFragment extends Fragment {

    public OverviewFragment() {
        // Required empty public constructor
    }

    private AppDatabase db;

    @BindView(R.id.textView)
    TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.title_overview);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        retrieveTasks();

        return rootView;
    }

    private void retrieveTasks() {
        LiveData<List<SymptomEntity>> tasks = db.symptomDao().loadAllSymptoms();
        tasks.observe(this, new Observer<List<SymptomEntity>>() {
            @Override
            public void onChanged(@Nullable List<SymptomEntity> symptomList) {
                message.setText(symptomList.toString());
            }
        });
    }

}
