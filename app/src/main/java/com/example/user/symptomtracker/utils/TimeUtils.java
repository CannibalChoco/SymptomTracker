package com.example.user.symptomtracker.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.user.symptomtracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Simple Utility methods for converting time from and to timestamps
 */
public class TimeUtils {

    private static final long DEBUG_MINUTE_TO_MILLIS = TimeUnit.MINUTES.toMillis(1);
    public static final long WEEK_TO_MILLIS = TimeUnit.DAYS.toMillis(7);

    public static final int DAY = 1;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 30;

    public static final int TIME_UNIT_NOT_SELECTED = -1;
    public static final int TIME_UNIT_HOUR = 0;
    public static final int TIME_UNIT_DAY = 1;
    public static final int TIME_UNIT_WEEK = 2;
    public static final int TIME_UNIT_MONTH = 3;

    private static final String DAY_FORMAT = "EEE";
    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static String getDateStringFromTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static String getTimeStringFromTimestamp(Context context, long timestamp) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(timestamp);
        Resources res = context.getResources();

        if (days < DAY) {
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timestamp);
            return res.getQuantityString(R.plurals.number_of_hours, hours, hours);
        } else if (days < DAYS_IN_WEEK) {
            return res.getQuantityString(R.plurals.number_of_days, days, days);
        } else if (days < DAYS_IN_MONTH) {
            int weeks = days / DAYS_IN_WEEK;
            return res.getQuantityString(R.plurals.number_of_weeks, weeks, weeks);
        } else {
            int months = days / DAYS_IN_MONTH;
            return res.getQuantityString(R.plurals.number_of_months, months, months);
        }
    }

    public static int getTimeUnitForMillis(long timestamp) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(timestamp);

        if (days < DAY) {
            return  (int) TimeUnit.MILLISECONDS.toHours(timestamp);
        } else if (days < DAYS_IN_WEEK) {
            return days;
        } else if (days < DAYS_IN_MONTH) {
            return days / DAYS_IN_WEEK;
        } else {
            return days / DAYS_IN_MONTH;
        }
    }

    public static int getRadioButtonIdForTimestamp(long timestamp) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(timestamp);

        if (days < DAY) {
            return  R.id.radioTimeHour;
        } else if (days < DAYS_IN_WEEK) {
            return R.id.radioTimeDay;
        } else if (days < DAYS_IN_MONTH) {
            return R.id.radioTimeWeek;
        } else {
            return R.id.radioTimeMonth;
        }
    }

    /**
     * Calculate time in milliseconds from the data user has entered in dialog
     * @param time count of time units
     * @return time in milliseconds
     */
    public static long getTimeInMillis(int timeUnit, int time) {
        if (timeUnit != TIME_UNIT_NOT_SELECTED){
            switch (timeUnit){
                case TIME_UNIT_HOUR:
                    return TimeUnit.HOURS.toMillis(time);
                case TIME_UNIT_DAY:
                    return TimeUnit.DAYS.toMillis(time);
                case TIME_UNIT_WEEK:
                    return TimeUnit.DAYS.toMillis(time) * DAYS_IN_WEEK;
                case TIME_UNIT_MONTH:
                    return TimeUnit.DAYS.toMillis(time) * DAYS_IN_MONTH;
            }
        }
        return 0;
    }

    public static long getTimeWeekAgo(){
        return System.currentTimeMillis() - WEEK_TO_MILLIS;
    }

    public static String getDay(long millis){
        Date date = new Date(millis);
        DateFormat format = new SimpleDateFormat(DAY_FORMAT);
        return String.valueOf(format.format(date));
    }

    /**
     * Compare timestamp with todays start of day
     *
     * @param timestamp to compare
     * @return true if timestamp not smaller than todays start of day, false otherwise
     */
    public static boolean severityAddedToday(long timestamp){
        return timestamp >= getStartOfDay();
    }

    /**
     * Get todays start of day in milliseconds
     * @return start of day in millis
     */
    private static long getStartOfDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
