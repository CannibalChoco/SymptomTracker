package com.example.user.symptomtracker.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment for displaying an overview of all the symptoms
 */
public class OverviewFragment extends Fragment implements OverviewAdapter.OnSymptomClickListener{

    public OverviewFragment() {
        // Required empty public constructor
    }

    private AppDatabase db;
    private OverviewAdapter adapter;

    @BindView(R.id.overviewRecyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.title_overview);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        retrieveSymptoms();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new OverviewAdapter(getContext(), new ArrayList<SymptomEntity>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void retrieveSymptoms() {
        LiveData<List<SymptomEntity>> symptoms = db.symptomDao().loadAllSymptoms();
        symptoms.observe(this, new Observer<List<SymptomEntity>>() {
            @Override
            public void onChanged(@Nullable List<SymptomEntity> symptomList) {
                adapter.replaceDataSet(symptomList);
            }
        });
    }

    @Override
    public void onSymptomSelected(int id) {
        // TODO: refactor. The same code in OverviewFragment
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_ID, id);
        startActivity(intent);
    }
}
