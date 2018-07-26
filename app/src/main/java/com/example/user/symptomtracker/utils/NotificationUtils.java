package com.example.user.symptomtracker.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.ui.MainActivity;

/**
 * Building of notification code base taken from Udacity Hydration Reminder app
 * https://github.com/udacity/ud851-Exercises/blob/student/Lesson10-Hydration-Reminder/T10.02-Solution-CreateNotification/app/src/main/java/com/example/android/background/utilities/NotificationUtils.java
 */
public class NotificationUtils {

    private static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int SCHEDULE_APPOINTMENT_NOTIFICATION_ID = 3421;
    private static final int REMINDER_PENDING_INTENT_ID = 8362;

    /**
     * Build a notification for reminding user to schedule a doctors appointment
     * @param context activity context
     */
    public static void buildNotification (Context context, String title){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // set channel for O and later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setContentTitle(title)
                .setContentText(context.getString(R.string.notification_schedule_appointment_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.notification_schedule_appointment_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(SCHEDULE_APPOINTMENT_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Pending intent for launching app on notification click
     * @param context activity context
     * @return pending intent launches app
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
