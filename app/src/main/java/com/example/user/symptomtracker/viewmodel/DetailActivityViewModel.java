package com.example.user.symptomtracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.List;

public class DetailActivityViewModel extends ViewModel {

    private LiveData<SymptomEntity> symptomEntity;
    private LiveData<List<SeverityEntity>> severityList;
    private LiveData<List<NoteEntity>> noteList;
    private LiveData<List<TreatmentEntity>> currentTreatmentList;
    private LiveData<List<TreatmentEntity>> pastTreatmentList;

    public DetailActivityViewModel(AppDatabase db, int symptomId) {
        Repository repository = Repository.getInstance(db);

        symptomEntity = repository.getSymptomEntityById(symptomId);
        severityList = repository.getSeverityForSymptom(symptomId);
        noteList = repository.getNotesForSymptom(symptomId);
        currentTreatmentList = repository.getCurrentTreatments(symptomId);
        pastTreatmentList = repository.getPastTreatments(symptomId);
    }

    public LiveData<SymptomEntity> getSymptomEntity() {
        return symptomEntity;
    }

    public LiveData<List<SeverityEntity>> getSeverityList() {
        return severityList;
    }

    public LiveData<List<NoteEntity>> getNoteList() {
        return noteList;
    }

    public LiveData<List<TreatmentEntity>> getCurrentTreatments() {
        return currentTreatmentList;
    }

    public LiveData<List<TreatmentEntity>> getPastTreatments() {
        return pastTreatmentList;
    }
}
