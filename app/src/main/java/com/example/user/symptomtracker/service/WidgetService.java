package com.example.user.symptomtracker.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.Symptom;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WIDGET_SERVICE", "onGetViewFactory");
        return new WidgetServiceRemoteViewsFactory(this.getApplicationContext());
    }
}

class WidgetServiceRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final int DAY_ONE = 0;
    private static final int DAY_TWO = 1;
    private static final int DAY_THREE = 2;
    private static final int DAY_FOUR = 3;
    private static final int DAY_FIVE = 4;
    private static final int DAY_SIX = 5;
    private static final int DAY_SEVEN = 6;
    private static final int ITEM_COUNT = 7;

    Context context;
    List<Symptom> symptoms;

    public WidgetServiceRemoteViewsFactory(Context applicationContext){
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

    // TODO: squish the bug
    @Override
    public RemoteViews getViewAt(int position) {
        // TODO: set view data like in onBindViewHolder
        Symptom symptom = symptoms.get(position);
        Log.d("WIDGET_SERVICE", "getViewAt " + symptom.toString());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        views.setTextViewText(R.id.widget_text_name, symptom.getSymptom().getName());
        //views.setViewVisibility(R.id.widget_empty_state_text, View.GONE);

        // TODO: go through symptom severity and set it on severity views
        List<SeverityEntity> severity = symptom.getSeverityList();
        int severityListSize = severity.size();
        int dayOne = severityListSize - ITEM_COUNT;
        int seventh = dayOne + DAY_SEVEN;
        int sixth = dayOne + DAY_SIX;
        int fifth = dayOne + DAY_FIVE;
        int fourth = dayOne + DAY_FOUR;
        int third = dayOne + DAY_THREE;
        int second = dayOne + DAY_TWO;
        int first = dayOne;

        for (int i = severityListSize; i >= dayOne; i--){
            String value = String.valueOf(severity.get(i));
            if (i == seventh){
                views.setTextViewText(R.id.widget_day_7, value);
            } else if (i == sixth){
                views.setTextViewText(R.id.widget_day_6, value);
            } else if (i == fifth){
                views.setTextViewText(R.id.widget_day_5, value);
            } else if (i == fourth){
                views.setTextViewText(R.id.widget_day_4, value);
            } else if (i == third){
                views.setTextViewText(R.id.widget_day_3, value);
            } else if (i == second){
                views.setTextViewText(R.id.widget_day_2, value);
            } else if (i == first){
                views.setTextViewText(R.id.widget_day_1, value);
            }
        }

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
}