package com.example.user.symptomtracker.service;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.utils.NotificationUtils;
import com.example.user.symptomtracker.utils.TimeUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

/**
 * Executes a job for querying db and sending a notification if needed
 */
public class ReminderNotificationJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters job) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                List<SymptomEntity> symptoms = db.symptomDao().loadAllUnresolvedSymptoms(
                        TimeUtils.getTimeWeekAgo());

                // TODO: either use the rest of symptoms returned or query just for one
                if (symptoms.size() > 0){
                    String title = symptoms.get(0).getName() +
                            getString(R.string.notification_title_append);
                    NotificationUtils.buildNotification(getApplicationContext(), title);
                }
            }
        });

        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true; // Answers the question: "Should this job be retried?"
    }
}
