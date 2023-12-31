package com.mikaauer.workingtimemeasurement.Database;

public class DatabaseConstants {
    public static String DATABASE_URL = "mongodb://admin:admin@0.0.0.0:9999/";
    public static String TIME_DATABASE_NAME = "TimeEntries";
    public static String WORKDAYS_COLLECTION_NAME = "workdays";

    public static String KEY_DAY = "day";
    public static String KEY_MONTH = "month";
    public static String KEY_YEAR = "year";
    public static String KEY_STARTING_HOUR = "starting_hour";
    public static String KEY_STARTING_MINUTE = "starting_minute";
    public static String KEY_END_HOUR = "end_hour";
    public static String KEY_END_MINUTE = "end_minute";
    public static String KEY_BREAK_DURATION = "break_duration";
}
