package com.example.user.symptomtracker.utils;

import android.content.Context;
import android.content.res.Resources;

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

    public static String getDateStringFromTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    // TODO: make return value prettier
    public static String getTimeUnitFromTimestamp(Context context, long timestamp) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(timestamp);
        Resources res = context.getResources();

        if (days < DAY) {
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timestamp);
            return res.getQuantityString(R.plurals.number_of_hours, hours, hours);
        } else if (days < WEEK) {
            return res.getQuantityString(R.plurals.number_of_days, days, days);
        } else if (days < MONTH) {
            int weeks = days / WEEK;
            return res.getQuantityString(R.plurals.number_of_weeks, weeks, weeks);
        } else {
            int months = days / MONTH;
            return res.getQuantityString(R.plurals.number_of_months, months, months);
        }
    }
}
