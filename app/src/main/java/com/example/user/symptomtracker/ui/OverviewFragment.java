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
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.ui.adapter.OverviewAdapter;
import com.example.user.symptomtracker.viewmodel.MainActivityViewModel;

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
    @BindView(R.id.todayEmptyStateText)
    TextView emptyStateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        initProgressBarAndEmptyState();

        initRecyclerView();

        getDataFromViewModel();

        return rootView;
    }

    private void getDataFromViewModel() {
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        model.getUnresolvedSymptomsLiveData().observe(getActivity(), symptoms -> {
            adapter.replaceDataSet(symptoms);
            if (symptoms.size() < 1){
                emptyStateText.setVisibility(View.VISIBLE);
            } else {
                emptyStateText.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSymptomSelected(int id) {
        // TODO: refactor. The same code in OverviewFragment
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_ID, id);
        startActivity(intent);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new OverviewAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initProgressBarAndEmptyState() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
    }
}
