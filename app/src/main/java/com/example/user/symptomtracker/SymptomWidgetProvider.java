package com.example.user.symptomtracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.user.symptomtracker.service.WidgetService;
import com.example.user.symptomtracker.ui.AddSymptomActivity;
import com.example.user.symptomtracker.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class SymptomWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_KEY_FRAGMENT_ID = "widgetFragmentId";
    public static final int WIDGET_TARGET_FRAGMENT_TODAY = 0;
    public static final int WIDGET_TARGET_FRAGMENT_OVERVIEW = 1;
    public static final int WIDGET_TARGET_ACTIVITY_NEW = 2;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.symptom_widget);

        //  construct intent to launch overview fragment on "overview" click
        PendingIntent overviewPendingIntent = getOverviewPendingIntent(context);
        views.setOnClickPendingIntent(R.id.widget_overview_text, overviewPendingIntent);

        // construct intent to launch today fragment on "today" click
        PendingIntent todayPendingIntent = getTodayPendingIntent(context);
        views.setOnClickPendingIntent(R.id.widget_today_text, todayPendingIntent);

        // construct intent to launch add new activity
        PendingIntent addSymptomPendingIntent = getAddSymptomPendingIntent(context);
        views.setOnClickPendingIntent(R.id.widget_add_symptom, addSymptomPendingIntent);

        Intent widgetServiceIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, widgetServiceIntent);

        // TODO: construct intent to launch detail view on item click
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            Log.d("WIDGET_SERVICE", "provider onUpdate");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getTodayPendingIntent(Context context) {
        Intent todayIntent = new Intent(context, MainActivity.class);
        todayIntent.putExtra(WIDGET_KEY_FRAGMENT_ID, WIDGET_TARGET_FRAGMENT_TODAY);
        return PendingIntent.getActivity(context, 0,
                todayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getOverviewPendingIntent(Context context) {
        Intent overviewIntent = new Intent(context, MainActivity.class);
        overviewIntent.putExtra(WIDGET_KEY_FRAGMENT_ID, WIDGET_TARGET_FRAGMENT_OVERVIEW);
        return PendingIntent.getActivity(context, 1,
                overviewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getAddSymptomPendingIntent(Context context) {
        Intent intent = new Intent(context, AddSymptomActivity.class);
        return PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

