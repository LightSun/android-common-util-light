package com.heaven7.util.extra;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *  the time calculator
 * Created by heaven7 on 2016/9/24.
 */
public final class TimeCalculator {

    public static String calculate(long milliseconds, ITimeFormatter formatter){
        Calendar calendar = Calendar.getInstance(formatter.getTimeZone());
        calendar.setTimeInMillis(milliseconds);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);      //from 0
        int day = calendar.get(Calendar.DAY_OF_MONTH); //from 1

        return formatter.format(calendar, year, month, day, hour, minute, second);
    }

    public interface ITimeFormatter{
         TimeZone getTimeZone();
         String format(Calendar cal, int year, int month, int day, int hour,int minute, int second);
    }
    public static abstract class DefaultTimeFormatter implements ITimeFormatter{
        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }
        @Override
        public String format(Calendar cal, int year, int month, int day, int hour, int minute, int second) {
             //yyyy-MM-dd HH:mm:ss
            return null;
        }
    }
}
