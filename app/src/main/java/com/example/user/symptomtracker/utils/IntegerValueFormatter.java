package com.example.user.symptomtracker.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class IntegerValueFormatter implements IValueFormatter{

    private  DecimalFormat format;

    public IntegerValueFormatter() {
        format = new DecimalFormat("###");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return format.format(value);
    }
}
