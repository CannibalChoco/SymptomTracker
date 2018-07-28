package com.example.user.symptomtracker.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.user.symptomtracker.database.AppDatabase;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private AppDatabase db;
    private final int symptomId;

    public DetailActivityViewModelFactory(AppDatabase db, int symptomId) {
        this.db = db;
        this.symptomId = symptomId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(db, symptomId);
    }
}
