package com.example.user.symptomtracker.utils;

import android.content.Context;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for managing GraphView
 */
public class GraphUtils {

    private static final int VISIBLE_DAYS = 7;

    /**
     * Set up the default graph
     *
     * @param graph the view
     * @param severityEntities List of data
     */
    public static void initBarChart(Context context, BarChart graph, List<SeverityEntity> severityEntities){
        graph.invalidate();
        List<BarEntry> entries = new ArrayList<>();
        int listSize = severityEntities.size();

        /**
         * Formatting XAxis label implementation taken from https://stackoverflow.com/a/41499401/9831831
         */
        ArrayList<String> labelX = new ArrayList<>();

        if (listSize > 0){
            for (int i = 0; i < listSize; i++) {
                SeverityEntity severity = severityEntities.get(i);
                entries.add(new BarEntry(i, severity.getSeverity()));
                labelX.add(TimeUtils.getDay(severity.getTimestamp()));
            }
        }

        graph.setDescription(null);

        XAxis xAxis = graph.getXAxis();
        YAxis yAxis = graph.getAxisLeft();

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelCount(listSize, true);

        // remove X axis lines
        xAxis.setDrawGridLines(false);
        if (labelX.size() > 0){
            xAxis.setValueFormatter((value, axis) -> labelX.get((int) value));
        }

        int color = context.getResources().getColor(R.color.graphBar);
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(color);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new IntegerValueFormatter());


        graph.setData(barData);
        graph.setVisibleXRange(VISIBLE_DAYS, VISIBLE_DAYS);

        YAxis rightAxis = graph.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setEnabled(false);

        yAxis.setAxisMaximum(10);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(10);

        graph.moveViewToX(listSize);
        graph.getLegend().setEnabled(false);
    }
}
