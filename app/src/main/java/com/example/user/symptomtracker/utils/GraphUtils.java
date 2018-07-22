package com.example.user.symptomtracker.utils;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for managing GraphView
 */
public class GraphUtils {

    /**
     *
     * @param graph
     * @param severityEntities
     */
    public static void initBarChart(BarChart graph, List<SeverityEntity> severityEntities){
        graph.invalidate();
        List<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < severityEntities.size(); i++) {
            SeverityEntity severity = severityEntities.get(i);
            entries.add(new BarEntry(i, severity.getSeverity()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Label");
        dataSet.setColor(R.color.colorPrimaryDark);
        BarData barData = new BarData(dataSet);
        graph.setData(barData);
    }
}
