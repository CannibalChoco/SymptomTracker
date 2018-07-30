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
import com.example.user.symptomtracker.ui.DialogFragments.AddTreatmentDialog;
import com.example.user.symptomtracker.ui.adapter.TreatmentAdapter;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModel;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.symptomtracker.ui.DetailActivity.FRAGMENT_ADD_PAST_TREATMENT;

public class TreatmentFragment extends Fragment implements
        AddTreatmentDialog.OnSaveTreatment, TreatmentAdapter.OnTreatmentLongClickListener {

    public static final int ID_FRAGMENT_CURRENT = 0;
    public static final int ID_FRAGMENT_PAST = 1;
    public static final String KEY_FRAGMENT_ID = "fragmentId";
    public static final String KEY_SYMPTOM_ID = "symptomId";


    @BindView(R.id.treatmentRv)
    RecyclerView treatmentRv;

    private int fragmentId;
    private int symptomId;
    private TreatmentAdapter treatmentAdapter;
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
                fragmentId = arguments.getInt(KEY_FRAGMENT_ID);
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

        treatmentAdapter = new TreatmentAdapter(getContext(),
                new ArrayList<>(), this);
        treatmentRv.setAdapter(treatmentAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fragmentId == ID_FRAGMENT_CURRENT){
            retrieveCurrentTreatments();
        } else if (fragmentId == ID_FRAGMENT_PAST){
            retrievePastTreatments();
        }
    }

    @Nullable
    @OnClick(R.id.addSymptom)
    public void addSymptom(){
        addTreatment(fragmentId);
    }

    private void retrieveCurrentTreatments() {
        model.getCurrentTreatments().observe(this, treatmentEntities ->
                treatmentAdapter.replaceDataSet(treatmentEntities));
    }

    private void retrievePastTreatments() {
        model.getPastTreatments().observe(this, treatmentEntities ->
                treatmentAdapter.replaceDataSet(treatmentEntities));
    }

    public void addTreatment(int id) {
        AddTreatmentDialog addTreatmentDialog = new AddTreatmentDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_ID, id);

        addTreatmentDialog.setArguments(bundle);
        addTreatmentDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        addTreatmentDialog.setOnSaveTreatmentListener(this);
        addTreatmentDialog.show(getFragmentManager(), FRAGMENT_ADD_PAST_TREATMENT);
    }

    @Override
    public void onSaveTreatment(String name, long takesEffectIn, int wasSuccessful, boolean isActive) {
        final TreatmentEntity treatment = new TreatmentEntity(symptomId, name,
                takesEffectIn, wasSuccessful, isActive);
        repository.saveTreatment(treatment);
    }

    @Override
    public void onTreatmentSuccessChanged(int id, int isSuccessful) {
        // TODO: update treatment success
        repository.updateTreatmentSuccess(id, isSuccessful);
    }
}
