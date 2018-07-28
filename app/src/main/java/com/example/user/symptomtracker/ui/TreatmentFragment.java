package com.example.user.symptomtracker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.ui.DialogFragments.AddCurrentTreatmentDialog;
import com.example.user.symptomtracker.ui.DialogFragments.AddPastTreatmentDialog;
import com.example.user.symptomtracker.ui.adapter.CurrentTreatmentAdapter;
import com.example.user.symptomtracker.ui.adapter.PastTreatmentAdapter;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModel;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.symptomtracker.ui.DetailActivity.FRAGMENT_ADD_CURRENT_TREATMENT;
import static com.example.user.symptomtracker.ui.DetailActivity.FRAGMENT_ADD_PAST_TREATMENT;

public class TreatmentFragment extends Fragment implements
        AddCurrentTreatmentDialog.OnSaveCurrentTreatment,
        AddPastTreatmentDialog.OnSavePastTreatment{

    public static final int ID_FRAGMENT_CURRENT = 0;
    public static final int ID_FRAGMENT_PAST = 1;
    public static final String KEY_FRAGMENT_ID = "fragmentId";
    public static final String KEY_SYMPTOM_ID = "symptomId";


    @BindView(R.id.treatmentRv)
    RecyclerView treatmentRv;

    private int id;
    private int symptomId;
    private CurrentTreatmentAdapter currentTreatmentAdapter;
    private PastTreatmentAdapter pastTreatmentAdapter;
    private AppDatabase db;
    private DetailActivityViewModel model;
    private Repository repository;
    public TreatmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.treatments, container, false);

        ButterKnife.bind(this, rootView);

        db = AppDatabase.getInstance(getContext());
        repository = Repository.getInstance(db);
        Bundle arguments = getArguments();
        if (arguments != null){
            if(arguments.containsKey(KEY_FRAGMENT_ID)){
                id = arguments.getInt(KEY_FRAGMENT_ID);
            }
            if (arguments.containsKey(KEY_SYMPTOM_ID)){
                symptomId = arguments.getInt(KEY_SYMPTOM_ID);
            }
        }

        DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(db, symptomId);
        model = ViewModelProviders.of(getActivity(), factory).get(DetailActivityViewModel.class);

        LinearLayoutManager treatmentLayoutManager = new LinearLayoutManager(getContext());
        treatmentRv.setLayoutManager(treatmentLayoutManager);
        treatmentRv.setHasFixedSize(true);

        if (id == ID_FRAGMENT_CURRENT){
            currentTreatmentAdapter = new
                    CurrentTreatmentAdapter(getContext(), new ArrayList<>());
            treatmentRv.setAdapter(currentTreatmentAdapter);
        } else if (id == ID_FRAGMENT_PAST){
            pastTreatmentAdapter = new PastTreatmentAdapter(getContext(),
                    new ArrayList<>());
            treatmentRv.setAdapter(pastTreatmentAdapter);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (id == ID_FRAGMENT_CURRENT){
            retrieveCurrentTreatments();
        } else if (id == ID_FRAGMENT_PAST){
            retrievePastTreatments();
        }
    }

    @Nullable
    @OnClick(R.id.addSymptom)
    public void addSymptom(){
        if (id == ID_FRAGMENT_CURRENT){
            addCurrentTreatment();
        } else if (id == ID_FRAGMENT_PAST){
            addPastTreatment();
        }
    }

    private void retrieveCurrentTreatments() {
        model.getCurrentTreatments().observe(this, treatmentEntities ->
                currentTreatmentAdapter.replaceDataSet(treatmentEntities));
    }

    private void retrievePastTreatments() {
        model.getPastTreatments().observe(this, treatmentEntities ->
                pastTreatmentAdapter.replaceDataSet(treatmentEntities));
    }

    public void addCurrentTreatment() {
        AddCurrentTreatmentDialog addCurrentTreatmentDialog = new AddCurrentTreatmentDialog();
        addCurrentTreatmentDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        addCurrentTreatmentDialog.setOnSaveCurrentTreatmentListener(this);
        addCurrentTreatmentDialog.show(getFragmentManager(), FRAGMENT_ADD_CURRENT_TREATMENT);
    }

    public void addPastTreatment() {
        AddPastTreatmentDialog addPastTreatmentDialog = new AddPastTreatmentDialog();
        addPastTreatmentDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        addPastTreatmentDialog.setOnSavePastTreatmentListener(this);
        addPastTreatmentDialog.show(getFragmentManager(), FRAGMENT_ADD_PAST_TREATMENT);
    }

    @Override
    public void onSaveCurrentTreatment(String name, long takesEffectIn) {
        final TreatmentEntity treatment = new TreatmentEntity(symptomId, name,
                takesEffectIn, 3, true);
        repository.saveTreatment(treatment);
    }

    @Override
    public void onSavePastTreatment(String name, long takesEffectIn, int wasSuccessful) {
        final TreatmentEntity treatment = new TreatmentEntity(symptomId, name,
                takesEffectIn, wasSuccessful, false);
        repository.saveTreatment(treatment);
    }
}
