package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.user.symptomtracker.ui.adapter.ResolvedAdapter;
import com.example.user.symptomtracker.viewmodel.MainActivityViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying an all of the resolved symptoms
 */
public class ResolvedFragment extends Fragment implements ResolvedAdapter.OnSymptomClickListener{

    private static final String NAME = ResolvedFragment.class.getSimpleName();

    @BindView(R.id.resolvedRv)
    RecyclerView recyclerView;
    @BindView(R.id.resolvedEmptyText)
    TextView emptyText;
    @BindView(R.id.resolvedProgressBar)
    ProgressBar progressBar;

    private MainActivityViewModel model;
    ResolvedAdapter adapter;

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    public ResolvedFragment() {
        // Required empty public constructor
    }

    private OnSymptomSelected symptomSelectedListener;

    public interface OnSymptomSelected{
        void onResolvedSymptomSelected(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSymptomSelected){
            symptomSelectedListener = (OnSymptomSelected) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resolved, container, false);
        ButterKnife.bind(this, rootView);

        initProgressBarAndEmptyState();
        initRecyclerView();
        getDataFromViewModel();

        FirebaseAnalytics.getInstance(getActivity()).setCurrentScreen(getActivity(), NAME, null);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        symptomSelectedListener = null;
    }

    private void getDataFromViewModel(){
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        model.getResolvedSymptomsLiveData().observe(getActivity(), symptoms -> {
            adapter.replaceDataSet(symptoms);
            if (symptoms.size() < 1){
                emptyText.setVisibility(View.VISIBLE);
            } else {
                emptyText.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.GONE);
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

        adapter = new ResolvedAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void initProgressBarAndEmptyState() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
    }

    @Override
    public void onResolvedSymptomSelected(int id) {
        symptomSelectedListener.onResolvedSymptomSelected(id);
    }

}
