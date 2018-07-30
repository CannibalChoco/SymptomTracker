package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.ui.adapter.OverviewAdapter;
import com.example.user.symptomtracker.viewmodel.MainActivityViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying an overview of all the symptoms
 */
public class OverviewFragment extends Fragment implements OverviewAdapter.OnSymptomClickListener{

    private static final String NAME = OverviewFragment.class.getSimpleName();

    public OverviewFragment() {
        // Required empty public constructor
    }

    private OverviewAdapter adapter;
    private MainActivityViewModel model;

    @BindView(R.id.overviewRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        progressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new OverviewAdapter(new ArrayList<Symptom>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        getDataFromViewModel();

        return rootView;
    }

    private void getDataFromViewModel() {
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        model.getUnresolvedSymptomsLiveData().observe(getActivity(), symptoms ->
                adapter.replaceDataSet(symptoms));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSymptomSelected(int id) {
        // TODO: refactor. The same code in OverviewFragment
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_ID, id);
        startActivity(intent);
    }
}
