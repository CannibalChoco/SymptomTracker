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
import com.example.user.symptomtracker.ui.DialogFragments.EditTreatmentDialog;
import com.example.user.symptomtracker.ui.adapter.TreatmentAdapter;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModel;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreatmentFragment extends Fragment implements
        EditTreatmentDialog.OnSaveTreatment,
        TreatmentAdapter.OnEditTreatment {

    public static final int ID_FRAGMENT_CURRENT = 0;
    public static final int ID_FRAGMENT_PAST = 1;
    public static final String KEY_FRAGMENT_ID = "fragmentId";
    public static final String KEY_SYMPTOM_ID = "symptomId";
    public static final String FRAGMENT_ADD_TREATMENT = "addTreatment";

    public static final String KEY_ACTION_SAVE_OR_EDIT = "actionSaveOrEdit";
    public static final int ID_ACTION_NEW = 2;
    public static final int ID_ACTION_EDIT = 3;
    public static final String KEY_TREATMENT = "key_treatment";

    @BindView(R.id.treatmentRv)
    RecyclerView treatmentRv;

    /**
     * Differentiate for weather to get current or past treatments
     */
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
        showEditTreatmentDialog(ID_ACTION_NEW, null);
    }

    private void retrieveCurrentTreatments() {
        model.getCurrentTreatments().observe(this, treatmentEntities ->
                treatmentAdapter.replaceDataSet(treatmentEntities));
    }

    private void retrievePastTreatments() {
        model.getPastTreatments().observe(this, treatmentEntities ->
                treatmentAdapter.replaceDataSet(treatmentEntities));
    }

    private void showEditTreatmentDialog(int id, @Nullable TreatmentEntity treatment) {
        EditTreatmentDialog editTreatmentDialog = new EditTreatmentDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ACTION_SAVE_OR_EDIT, id);
        if (id == ID_ACTION_EDIT){
            bundle.putParcelable(KEY_TREATMENT, treatment);
        }
        bundle.putInt(KEY_SYMPTOM_ID, symptomId);

        editTreatmentDialog.setArguments(bundle);
        editTreatmentDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        editTreatmentDialog.setOnSaveTreatmentListener(this);
        editTreatmentDialog.show(getFragmentManager(), FRAGMENT_ADD_TREATMENT);
    }

    @Override
    public void onSaveTreatment(TreatmentEntity treatment, int actionSaveOrEdit) {
        if (actionSaveOrEdit == ID_ACTION_NEW){
            repository.saveTreatment(treatment);
        } else if (actionSaveOrEdit == ID_ACTION_EDIT){
            repository.updateTreatment(treatment);
        }
    }

    @Override
    public void onEditTreatment(TreatmentEntity treatment) {
        showEditTreatmentDialog(ID_ACTION_EDIT, treatment);
    }
}
