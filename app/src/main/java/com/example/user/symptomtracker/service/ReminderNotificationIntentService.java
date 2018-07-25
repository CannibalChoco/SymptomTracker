package com.example.user.symptomtracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.utils.NotificationUtils;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReminderNotificationIntentService extends IntentService {

    public static final String ACTION_CHECK_UNRESOLVED_SYMPTOMS =
            "com.example.user.symptomtracker.service.action.CHECK_UNRESOLVED_SYMPTOMS";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.user.symptomtracker.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.user.symptomtracker.service.extra.PARAM2";

    public ReminderNotificationIntentService() {
        super("ReminderNotificationIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCheckUnresolvedSymptoms(Context context) {
        Intent intent = new Intent(context, ReminderNotificationIntentService.class);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, ReminderNotificationIntentService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

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
        // TODO: Query db to see if there are unresolved symptoms.
        Log.d("CHECKDB", "checking for unresolved symptoms");
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<SymptomEntity> symptoms = db.symptomDao().loadAllUnresolvedSymptoms();

        if (symptoms.size() > 0){
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
