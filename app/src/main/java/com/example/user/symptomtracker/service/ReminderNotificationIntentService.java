package com.example.user.symptomtracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.utils.NotificationUtils;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ReminderNotificationIntentService extends IntentService {

    public static final String ACTION_CHECK_UNRESOLVED_SYMPTOMS =
            "com.example.user.symptomtracker.service.action.CHECK_UNRESOLVED_SYMPTOMS";

    public ReminderNotificationIntentService() {
        super("ReminderNotificationIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionCheckUnresolvedSymptoms(Context context) {
        Intent intent = new Intent(context, ReminderNotificationIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHECK_UNRESOLVED_SYMPTOMS.equals(action)) {
                handleActionCheckUnresolvedSymptoms();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCheckUnresolvedSymptoms() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<SymptomEntity> symptoms = db.symptomDao().loadAllUnresolvedSymptoms();

        if (symptoms.size() > 0){
            // TODO: move to string.xml
            String title = symptoms.get(0).getName() + " is unresolved";
            NotificationUtils.buildNotification(getApplicationContext(), title);
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
