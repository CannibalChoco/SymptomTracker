package com.example.user.symptomtracker.utils;


import android.graphics.Paint;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for managing GraphView
 */
public class GraphUtils {

    /**
     * Initializes the passed in graph view to the app defaults
     *
     * @param graph GraphView to be initialized
     * @param dataPoints DataPoints to be displayed in the passed n graph
     */
    public static void initGraphView(GraphView graph, DataPoint[] dataPoints){

        graph.removeAllSeries();

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        series.setColor(R.color.colorPrimaryDark);

        graph.addSeries(series);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(30);
        graph.getGridLabelRenderer().setGridColor(R.color.colorPrimary);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().reloadStyles();
    }

    public static DataPoint[] getRandomDataPoints(){
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, (Math.random()*10-0));
        }

        return points;
    }

    public static DataPoint[] getDataPoints(List<SeverityEntity> severityList){
        DataPoint[] dataPoints = new DataPoint[severityList.size()];

        for (int i = 0; i < dataPoints.length; i++){
            SeverityEntity severityEntity = severityList.get(i);
            // TODO: show time instead of i
            dataPoints[i] = new DataPoint(i, severityEntity.getSeverity());
        }

        return dataPoints;
    }
}
