package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying an overview of all the symptoms
 */
public class OverviewFragment extends Fragment implements OverviewAdapter.OnSymptomClickListener{

    private static final String NAME = OverviewFragment.class.getSimpleName();

    private static final String KEY_GRID_RV_STATE = "gridviewState";
    private static final String KEY_LINEAR_RV_STATE = "linearviewState";

    public OverviewFragment() {
        // Required empty public constructor
    }

    private OnSymptomSelected symptomSelectedListener;

    public interface OnSymptomSelected{
        void onOverviewSymptomSelected(int id);
    }

    private OverviewAdapter adapter;
    private MainActivityViewModel model;

    @BindView(R.id.overviewRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.todayEmptyStateText)
    TextView emptyStateText;

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        initProgressBarAndEmptyState();

        initRecyclerView();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        getDataFromViewModel();

        if (savedInstanceState != null){
            Configuration configuration = getResources().getConfiguration();
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                gridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(
                        KEY_GRID_RV_STATE));
            } else {
                linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(
                        KEY_LINEAR_RV_STATE));
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            outState.putParcelable(KEY_GRID_RV_STATE, gridLayoutManager.onSaveInstanceState());
        } else {
            outState.putParcelable(KEY_LINEAR_RV_STATE, linearLayoutManager.onSaveInstanceState());
        }
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
        symptomSelectedListener.onOverviewSymptomSelected(id);
    }

    public void setSymptomSelectedListener(OnSymptomSelected listener){
        this.symptomSelectedListener = listener;
    }

    private void initRecyclerView() {
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new OverviewAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void initProgressBarAndEmptyState() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
    }
}
