package com.example.user.symptomtracker;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Class for setting up Stetho
 */
public class SymptomTrackerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
