package com.example.user.symptomtracker.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
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
        // TODO: get data from db
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
        // TODO: set view data like in onBindViewHolder
        Symptom symptom = symptoms.get(position);
        Log.d("WIDGET_SERVICE", "getViewAt " + symptom.toString());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        views.setTextViewText(R.id.widget_text_name, symptom.getSymptom().getName());
        //views.setViewVisibility(R.id.widget_empty_state_text, View.GONE);

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