package com.example.user.symptomtracker.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

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
     * @param graph            the view
     * @param severityEntities List of data
     */
    public static void initBarChart(Context context, BarChart graph, List<SeverityEntity> severityEntities) {
        graph.invalidate();
        List<BarEntry> entries = new ArrayList<>();
        int listSize = severityEntities.size();

        ArrayList<String> labelX = new ArrayList<>();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                SeverityEntity severity = severityEntities.get(i);
                entries.add(new BarEntry(i, severity.getSeverity()));
                labelX.add(TimeUtils.getDay(severity.getTimestamp()));
            }
        }

        graph.setDescription(null);

        BarData barData = getBarData(context, entries);
        graph.setData(barData);

        formatYAxis(graph);
        formatXAxis(graph, listSize, labelX);


        styleGraph(context, graph, listSize);
    }

    private static void styleGraph(Context context, BarChart graph, int listSize) {
        graph.setVisibleXRange(VISIBLE_DAYS, VISIBLE_DAYS);
        graph.setBackgroundColor(context.getResources().getColor(R.color.graphBackground));
        graph.moveViewToX(listSize);
        graph.getLegend().setEnabled(false);
    }

    @NonNull
    private static BarData getBarData(Context context, List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(context.getResources().getColor(R.color.graphBar));
        BarData barData = new BarData(dataSet);
        // display bar value as 3 instead of 3.00
        barData.setValueFormatter(new IntegerValueFormatter());
        return barData;
    }

    private static void formatXAxis(BarChart graph, int listSize, ArrayList<String> labelX) {
        XAxis xAxis = graph.getXAxis();

        // set position and count of labels
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelCount(listSize);

        xAxis.setDrawGridLines(false);

        float barWidth = graph.getBarData().getBarWidth();
        float barMin = graph.getBarData().getXMin();

        xAxis.setAxisMinimum(barMin - barWidth / 2 );
        xAxis.setAxisMaximum(barWidth * listSize);

        xAxis.setCenterAxisLabels(false);

        setFormattedXlabel(labelX, xAxis);
    }

    private static void formatYAxis(BarChart graph) {
        YAxis rightAxis = graph.getAxisRight();
        rightAxis.setDrawLabels(false);
        // removes extra horizontal lines
        rightAxis.setEnabled(false);

        YAxis yAxis = graph.getAxisLeft();
        yAxis.setAxisMaximum(10);
        yAxis.setAxisMinimum(0);
        // total labels
        yAxis.setLabelCount(10);
    }

    /**
     * Format X Axis label to display day name
     * @param labelX a list of day name strings
     * @param xAxis the axis for which the labels will be set
     */
    private static void setFormattedXlabel(ArrayList<String> labelX, XAxis xAxis) {
        /**
         * Formatting XAxis label implementation taken from
         * https://stackoverflow.com/a/41499401/9831831
         */

        // set granularity for catching exception
        xAxis.setGranularity(1f);
        // Display Day names as bar labels
        if (labelX.size() > 0){
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    String label = null;
                    try{
                        label = labelX.get((int) value);
                    } catch (IndexOutOfBoundsException e){
                        axis.setGranularityEnabled(false);
                    }

                    return label;
                }
            });
        }
    }
}
