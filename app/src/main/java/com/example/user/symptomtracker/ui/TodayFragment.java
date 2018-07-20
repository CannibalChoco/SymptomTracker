package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.ui.adapter.TodayAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying and editing Today
 */
public class TodayFragment extends Fragment implements TodayAdapter.OnSeverityClickListener{

    public TodayFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rvToday)
    RecyclerView recyclerView;
    TodayAdapter adapter;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new TodayAdapter(new ArrayList<SymptomEntity>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        retrieveSymptoms();

        return rootView;
    }

    private void retrieveSymptoms() {
        LiveData<List<SymptomEntity>> symptoms = db.symptomDao().loadUnResolvedSymptoms();
        symptoms.observe(this, new Observer<List<SymptomEntity>>() {
            @Override
            public void onChanged(@Nullable List<SymptomEntity> symptomList) {
                adapter.replaceDataSet(symptomList);
            }
        });
    }

    @Override
    public void onSeverityClicked(int parentId, final int severity) {
        final SeverityEntity severityEntity = new SeverityEntity(parentId, severity, new Date().getTime());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.severityDao().insertSeverity(severityEntity);
            }
        });
    }
}
