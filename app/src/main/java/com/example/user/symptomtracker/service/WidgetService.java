package com.example.user.symptomtracker.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.ui.DetailActivity;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WIDGET_SERVICE", "onGetViewFactory");
        return new WidgetServiceRemoteViewsFactory(this.getApplicationContext());
    }
}

class WidgetServiceRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ITEM_COUNT = 7;

    Context context;
    List<Symptom> symptoms;

    public WidgetServiceRemoteViewsFactory(Context applicationContext) {
        this.context = applicationContext;
        Log.d("WIDGET_SERVICE", "factory constructor setting context");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Repository repository = Repository.getInstance(AppDatabase.getInstance(context));
        symptoms = repository.getUnresolvedSymptoms();
        Log.d("WIDGET_SERVICE", "onDataSetChanged " + symptoms.toString());

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return symptoms != null ? symptoms.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Symptom symptom = symptoms.get(position);
        Log.d("WIDGET_SERVICE", "getViewAt " + symptom.toString());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        views.setTextViewText(R.id.widget_text_name, symptom.getSymptom().getName());

        List<SeverityEntity> severityList = symptom.getSeverityList();
        int severityListSize = severityList.size();
        if (severityListSize > 0) {

            for (int i = 0; i < severityListSize; i++) {
                int dayIndex = ITEM_COUNT - severityListSize + i;
                String value = String.valueOf(severityList.get(i).getSeverity());
                int id = getDayView(dayIndex);
                views.setTextViewText(id, value);
            }
        }

        // set fillInIntent for launching detail view of clicked item
        Bundle bundle = new Bundle();
        bundle.putInt(DetailActivity.KEY_ID, symptom.getSymptom().getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Get days view ID by index
     *
     * @param dayIndex index 0-6
     * @return ID for view corresponding to index
     */
    private int getDayView(int dayIndex) {
        switch (dayIndex) {
            case 0:
                return R.id.widget_day_1;
            case 1:
                return R.id.widget_day_2;
            case 2:
                return R.id.widget_day_3;
            case 3:
                return R.id.widget_day_4;
            case 4:
                return R.id.widget_day_5;
            case 5:
                return R.id.widget_day_6;
            case 6:
                return R.id.widget_day_7;
            default:
                return R.id.widget_day_7;
        }
    }
}