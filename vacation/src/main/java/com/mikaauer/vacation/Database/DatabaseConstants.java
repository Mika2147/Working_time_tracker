package com.mikaauer.vacation.Database;

public class DatabaseConstants {
    public static String DATABASE_URL = System.getenv("MONGO_DATABASE_URL") != null ? System.getenv("MONGO_DATABASE_URL") : "mongodb://admin:admin@0.0.0.0:9998/";
    public static String TIME_DATABASE_NAME = "VacationEntries";
    public static String VACATION_COLLECTION_NAME = "vacation";

    public static final String KEY_STARTING_DAY = "starting_day";
    public static final String KEY_STARTING_MONTH = "starting_month";
    public static final String KEY_STARTING_YEAR = "starting_year";
    public static final String KEY_END_DAY = "end_day";
    public static final String KEY_END_MONTH = "end_month";
    public static final String KEY_END_YEAR = "end_year";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ID = "id";
    public static final String KEY_ACCEPTED = "accepted";
}
