package com.example.user.symptomtracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.Symptom;

import java.util.ArrayList;
import java.util.List;
public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Symptom>> unresolvedSymptomsLiveData;
    private List<Symptom> unresolvedSymptoms;
    private boolean symptomDataForTodayValid;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Repository repository = Repository.getInstance(db);
        unresolvedSymptomsLiveData = repository.getUnresolvedSymptomLiveData();
        unresolvedSymptoms = new ArrayList<>();
        symptomDataForTodayValid = false;
    }

    public LiveData<List<Symptom>> getUnresolvedSymptomsLiveData() {
        return unresolvedSymptomsLiveData;
    }

    public List<Symptom> getUnresolvedSymptoms(){
        return unresolvedSymptoms;
    }

    public void setUnresolvedSymptoms(List<Symptom> symptoms){
        this.unresolvedSymptoms = symptoms;
        symptomDataForTodayValid = true;
    }

    public void invalidateSymptomDataForToday() {
        this.symptomDataForTodayValid = false;
    }

    public boolean isSymptomDataForTodayValid() {
        return symptomDataForTodayValid;
    }
}
