package com.example.user.symptomtracker.utils;

import android.content.Context;

import com.example.user.symptomtracker.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Simple Utility methods for converting time from and to timestamps
 */
public class TimeUtils {

    public static final int DAY = 1;
    public static final int WEEK = 7;
    public static final int MONTH = 30;

    public static String getDateStringFromTimestamp(long timestamp){
        Date date = new Date(timestamp);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    // TODO: make return value prettier
    public static String getTimeUnitFromTimestamp(Context context, long timestamp){
        int days = (int) TimeUnit.MILLISECONDS.toDays(timestamp);

        if (days < DAY){
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timestamp);
            return context.getString(R.string.radio_time_hour) + " " + hours;
        } else if (days < WEEK){
            return context.getString(R.string.radio_time_day) + " " + days;
        } else if (days < MONTH){
            String weeks = String.valueOf(days / WEEK);
            return context.getString(R.string.radio_time_week) + " " + weeks;
        } else {
            String months = String.valueOf(days / MONTH);
            return context.getString(R.string.radio_time_month) + " " + months;
        }
    }
}
