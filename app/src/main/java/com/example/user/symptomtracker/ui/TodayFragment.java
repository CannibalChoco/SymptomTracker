package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private static final String KEY_RV_STATE = "rvState";

    public TodayFragment() {
        // Required empty public constructor
    }
    private OnSymptomSelected symptomSelectedListener;

    public interface OnSymptomSelected{
        void onTodaySymptomSelected(int id);
    }

    @BindView(R.id.rvToday)
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyStateText;

    TodayAdapter adapter;
    static AppDatabase db;

    private static MainActivityViewModel model;

    private LinearLayoutManager layoutManager;

    private Repository repository;

    private boolean canRestoreButtonState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, rootView);

        canRestoreButtonState = false;

        initProgressbarAndEmptyState(rootView);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        repository = Repository.getInstance(db);

        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        initRecyclerView();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        getDataFromViewModel();

        if (savedInstanceState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(
                    KEY_RV_STATE));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_RV_STATE, layoutManager.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(canRestoreButtonState){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        canRestoreButtonState = true;
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
        final SeverityEntity severityEntity = new SeverityEntity(parentId, severity,
                new Date().getTime());

        repository.saveSeverity(severityEntity);

        WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
    }

    @Override
    public void onSeverityUpdate(int severityEntityId, int newSeverityValue) {
        //model.invalidateSymptomDataForToday();
        repository.updateSeverity(severityEntityId, newSeverityValue, System.currentTimeMillis());


        //new GetSeverityAsyncTask().execute();


        WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
    }

    @Override
    public void onSymptomSelected(int id) {
        symptomSelectedListener.onTodaySymptomSelected(id);
    }

    public void setSymptomSelectedListener(OnSymptomSelected listener){
        this.symptomSelectedListener = listener;
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
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
