package com.example.user.symptomtracker.utils;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.receiver.SymptomWidgetProvider;

public class WidgetUtils {

    /**
     * Get instance of AppWidgetManager and call WidgetProviders updateAppWidgets()
     * Notify remote adapter to update widget data
     */
    public static void updateWidget(Application application) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(application);
        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(application, SymptomWidgetProvider.class));

        SymptomWidgetProvider.updateAppWidgets(application, appWidgetManager, ids);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);
    }

}
