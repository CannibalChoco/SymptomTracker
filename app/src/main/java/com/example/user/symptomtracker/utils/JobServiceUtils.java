package com.example.user.symptomtracker.utils;

import android.content.Context;

import com.example.user.symptomtracker.service.ReminderNotificationJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Utility class for initializing Jobs
 */
public class JobServiceUtils {

    private static final int REMINDER_INTERVAL_HOURS = 24;

    private static final String REMINDER_JOB_TAG = "check_unresolved_symptoms_job";

    public static boolean sInitialized;

    /**
     * Initialize a job for ReminderNotificationJobService
     * @param context for instantiating GooglePlayDriver
     */
    synchronized public static void scheduleCheckUnresolvedSymptoms(final Context context){
        if (sInitialized) return;

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job checkSymptomsJob = dispatcher.newJobBuilder()
                .setService(ReminderNotificationJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(checkSymptomsJob);

        sInitialized = true;
    }
}
