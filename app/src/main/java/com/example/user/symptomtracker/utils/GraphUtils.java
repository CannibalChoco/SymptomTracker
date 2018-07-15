package com.example.user.symptomtracker.utils;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

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

        graph.addSeries(series);
        graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
    }

    public static DataPoint[] getRandomDataPoints(){
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, (Math.random()*10-0));
        }

        return points;
    }
}
