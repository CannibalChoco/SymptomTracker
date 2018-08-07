package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.SymptomTrackerApplication;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.ui.adapter.TodayAdapter;
import com.example.user.symptomtracker.utils.WidgetUtils;
import com.example.user.symptomtracker.viewmodel.MainActivityViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying and editing Today
 */
public class TodayFragment extends Fragment implements TodayAdapter.OnSeverityClickListener {

    private static final String NAME = TodayFragment.class.getSimpleName();

    public TodayFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rvToday)
    RecyclerView recyclerView;
    private static ProgressBar progressBar;
    private static TextView emptyStateText;

    TodayAdapter adapter;
    static AppDatabase db;

    private static MainActivityViewModel model;

    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, rootView);

        initProgressbarAndEmptyState(rootView);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        repository = Repository.getInstance(db);

        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        initRecyclerView();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        //getValidData();
        getDataFromViewModel();

        return rootView;
    }

    private void getDataFromViewModel() {
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        model.getUnresolvedSymptomsLiveData().observe(getActivity(), symptoms -> {

            adapter.replaceSymptomData(symptoms);
            if (symptoms.size() < 1){
                emptyStateText.setVisibility(View.VISIBLE);
            } else {
                emptyStateText.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSeverityInsert(int parentId, final int severity) {
        //model.invalidateSymptomDataForToday();
        final SeverityEntity severityEntity = new SeverityEntity(parentId, severity,
                new Date().getTime());

        repository.saveSeverity(severityEntity);


        //new GetSeverityAsyncTask().execute();


        WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
    }

    @Override
    public void onSeverityUpdate(int severityEntityId, int newSeverityValue) {
        //model.invalidateSymptomDataForToday();
        repository.updateSeverity(severityEntityId, newSeverityValue, System.currentTimeMillis());


        //new GetSeverityAsyncTask().execute();


        WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new TodayAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initProgressbarAndEmptyState(View rootView) {
        progressBar = rootView.findViewById(R.id.progressBar);
        emptyStateText = rootView.findViewById(R.id.todayEmptyStateText);
        progressBar.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
    }
}
