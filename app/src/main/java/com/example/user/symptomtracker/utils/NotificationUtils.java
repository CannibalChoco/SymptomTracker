package com.example.user.symptomtracker.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.user.symptomtracker.R;

/**
 * Building of notification code base taken from Udacity Water Reminder app
 */
public class NotificationUtils {

    private static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int SCHEDULE_APPOINTMENT_NOTIFICATION_ID = 3421;

    public static void buildNotification (Context context){

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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setContentTitle(context.getString(R.string.notification_schedule_appointment_title))
                .setContentText(context.getString(R.string.notification_schedule_appointment_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.notification_schedule_appointment_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(SCHEDULE_APPOINTMENT_NOTIFICATION_ID, notificationBuilder.build());
    }

}
