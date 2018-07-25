package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
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
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.ui.adapter.TodayAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying and editing Today
 */
public class TodayFragment extends Fragment implements TodayAdapter.OnSeverityClickListener{

    private static final String NAME = TodayFragment.class.getSimpleName();

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
        adapter = new TodayAdapter(new ArrayList<Symptom>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        retrieveSymptoms();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        return rootView;
    }

    private void retrieveSymptoms() {

//        symptoms.observe(this, new Observer<List<Symptom>>() {
//            @Override
//            public void onChanged(@Nullable List<Symptom> symptomList) {
//                adapter.replaceSymptomData(symptomList);
//            }
//        });
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Symptom> symptoms = db.symptomDao().loadAllUnresolvedSymptomData();

                // DONE (7) Wrap the setTask call in a call to runOnUiThread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceSymptomData(symptoms);
                    }
                });

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
