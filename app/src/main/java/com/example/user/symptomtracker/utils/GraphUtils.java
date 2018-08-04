package com.example.user.symptomtracker.utils;

import android.content.Context;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
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

        for (int i = 0; i < listSize; i++) {
            SeverityEntity severity = severityEntities.get(i);
            entries.add(new BarEntry(i, severity.getSeverity()));
            labelX.add(TimeUtils.getDay(severity.getTimestamp()));
        }

        graph.setDescription(null);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        // remove X axis lines
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((value, axis) -> labelX.get((int) value));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(R.color.colorPrimaryDark);
        BarData barData = new BarData(dataSet);
        graph.setData(barData);
        graph.setVisibleXRange(VISIBLE_DAYS, VISIBLE_DAYS);
        graph.moveViewToX(listSize);
        graph.getLegend().setEnabled(false);
    }
}
