package com.example.user.symptomtracker;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Class for setting up Stetho
 */
public class SymptomTrackerApplication extends Application {

    private static SymptomTrackerApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        Stetho.initializeWithDefaults(this);
    }

    public static synchronized SymptomTrackerApplication getInstance(){
        return application;
    }
}
