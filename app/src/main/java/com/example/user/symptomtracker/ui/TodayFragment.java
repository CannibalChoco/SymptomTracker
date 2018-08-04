package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.ui.adapter.TodayAdapter;
import com.example.user.symptomtracker.utils.WidgetUtils;
import com.example.user.symptomtracker.viewmodel.MainActivityViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying and editing Today
 */
public class TodayFragment extends Fragment implements TodayAdapter.OnSeverityClickListener {

    private static final String NAME = TodayFragment.class.getSimpleName();
    public static final String KEY_DATA_CHANGED = "dataChanged";

    public TodayFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rvToday)
    RecyclerView recyclerView;
    private static ProgressBar progressBar;
    private static TextView emptyStateText;

    static TodayAdapter adapter;
    static AppDatabase db;

    private static MainActivityViewModel model;
    private static List<Symptom> symptoms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, rootView);

        initProgressbarAndEmptyState(rootView);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        initRecyclerView();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        getValidData();

        return rootView;
    }

    /**
     * Checks ViewModel if there is valid data cached. If there is, uses that data, otherwise
     * queries database for fresh data. Sends that data to adapter
     */
    private void getValidData() {
        if (model.isSymptomDataForTodayValid()) {
            symptoms = model.getUnresolvedSymptoms();
            adapter.replaceSymptomData(symptoms);

            progressBar.setVisibility(View.GONE);
        } else {
            new GetSeverityAsyncTask().execute();
        }
    }

    @Override
    public void onSeverityClicked(int parentId, final int severity) {
        model.invalidateSymptomDataForToday();
        final SeverityEntity severityEntity = new SeverityEntity(parentId, severity,
                new Date().getTime());

        Repository.getInstance(db).saveSeverity(severityEntity);
        WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
    }

    /**
     * AsyncTask needed for db read, because ToggleButtonGroup does not work if the dataset comes
     * from LiveData.
     * AsyncTask retrieves Symptom list from database, caches it in MainActivityViewModel, and reloads
     * only if the user has entered new data
     */
    private static class GetSeverityAsyncTask extends AsyncTask<Void, Void, List<Symptom>> {

        @Override
        protected List<Symptom> doInBackground(Void... voids) {
            Log.d("ASYNCQUERY", "doInBackground");
            return db.symptomDao().loadUnresolvedSymptoms();
        }

        @Override
        protected void onPostExecute(List<Symptom> symptomList) {
            super.onPostExecute(symptomList);
            if (symptomList != null && symptomList.size() > 0) {
                symptoms = symptomList;
                model.setUnresolvedSymptoms(symptomList);
                adapter.replaceSymptomData(symptomList);
                emptyStateText.setVisibility(View.GONE);
            } else {
                emptyStateText.setVisibility(View.VISIBLE);
            }

            progressBar.setVisibility(View.GONE);
        }
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
