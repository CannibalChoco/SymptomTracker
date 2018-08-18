package com.example.user.symptomtracker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.ui.DialogFragments.EditTreatmentDialog;
import com.example.user.symptomtracker.ui.adapter.TreatmentAdapter;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModel;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreatmentFragment extends Fragment implements
        EditTreatmentDialog.OnSaveTreatment,
        TreatmentAdapter.OnEditTreatment {

    private static final String KEY_TREATMENT_RV_STATE = "treatmentRvState";

    public static final int ID_FRAGMENT_CURRENT = 0;
    public static final int ID_FRAGMENT_PAST = 1;
    public static final String KEY_FRAGMENT_ID = "fragmentId";
    public static final String KEY_SYMPTOM_ID = "symptomId";
    public static final String DIALOG_TAG = "addOrEditTreatment";


    @BindView(R.id.treatmentRv)
    RecyclerView treatmentRv;
    @BindView(R.id.overviewProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.overviewEmptyText)
    TextView emptyStateText;

    /**
     * Differentiate for weather to get current or past treatments_shared
     */
    private int fragmentId;
    private int symptomId;
    private TreatmentAdapter treatmentAdapter;
    private AppDatabase db;
    private DetailActivityViewModel model;
    private Repository repository;

    private LinearLayoutManager treatmentLayoutManager;

    public TreatmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.treatments_shared, container, false);

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

        treatmentLayoutManager = new LinearLayoutManager(getContext());
        treatmentRv.setLayoutManager(treatmentLayoutManager);
        treatmentRv.setHasFixedSize(true);

        treatmentAdapter = new TreatmentAdapter(getContext(),
                new ArrayList<>(), this);
        treatmentRv.setAdapter(treatmentAdapter);

        if (savedInstanceState != null){
            treatmentLayoutManager.onRestoreInstanceState(savedInstanceState
                    .getParcelable(KEY_TREATMENT_RV_STATE));
        }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_TREATMENT_RV_STATE, treatmentLayoutManager.onSaveInstanceState());
    }

    @Nullable
    @OnClick(R.id.addTreatment)
    public void addTreatment(){
        showEditTreatmentDialog(EditTreatmentDialog.ID_NEW, null,
                getString(R.string.add_treatment_title));
    }

    private void retrieveCurrentTreatments() {
        model.getCurrentTreatments().observe(this, treatmentEntities ->
            refreshData(treatmentEntities, ID_FRAGMENT_CURRENT));
    }

    private void retrievePastTreatments() {
        model.getPastTreatments().observe(this, treatmentEntities ->
                    refreshData(treatmentEntities, ID_FRAGMENT_PAST));
    }

    private void showEditTreatmentDialog(int editTypeId, @Nullable TreatmentEntity treatment,
                                         String title) {
        EditTreatmentDialog editTreatmentDialog = EditTreatmentDialog.newInstance(editTypeId,
                treatment, symptomId, title, this);
        editTreatmentDialog.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void onSaveTreatment(TreatmentEntity treatment, int actionSaveOrEdit) {
        if (actionSaveOrEdit == EditTreatmentDialog.ID_NEW){
            repository.saveTreatment(treatment);
        } else if (actionSaveOrEdit == EditTreatmentDialog.ID_UPDATE){
            repository.updateTreatment(treatment);
        }
    }

    @Override
    public void onEditTreatment(TreatmentEntity treatment) {
        showEditTreatmentDialog(EditTreatmentDialog.ID_UPDATE, treatment,
                getString(R.string.edit_treatment));
    }

    private void refreshData(List<TreatmentEntity> treatmentEntities, int fragmentId) {
        treatmentAdapter.replaceDataSet(treatmentEntities);
        int treatmentListSize = treatmentEntities.size();
        setProgressBarAndEmptyStateText(treatmentListSize, fragmentId);
    }

    private void setProgressBarAndEmptyStateText(int treatmentListSize, int fragmentId) {
        progressBar.setVisibility(View.GONE);
        if (treatmentListSize > 0){
            emptyStateText.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.VISIBLE);
        }

        if (fragmentId == ID_FRAGMENT_CURRENT){
            emptyStateText.setText(getString(R.string.empty_treatments_current));
        } else if (fragmentId == ID_FRAGMENT_PAST){
            emptyStateText.setText(getString(R.string.empty_treatments_past));
        }
    }
}
