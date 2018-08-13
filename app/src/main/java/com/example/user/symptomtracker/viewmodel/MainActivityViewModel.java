package com.example.user.symptomtracker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.Symptom;

import java.util.List;
public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Symptom>> unresolvedSymptomsLiveData;
    private LiveData<List<Symptom>> resolvedSymptomsLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Repository repository = Repository.getInstance(db);
        unresolvedSymptomsLiveData = repository.getUnresolvedSymptomLiveData();
        resolvedSymptomsLiveData = repository.getResolvedSymptomLiveData();
    }

    public LiveData<List<Symptom>> getUnresolvedSymptomsLiveData() {
        return unresolvedSymptomsLiveData;
    }

    public LiveData<List<Symptom>> getResolvedSymptomsLiveData() {
        return resolvedSymptomsLiveData;
    }
}
