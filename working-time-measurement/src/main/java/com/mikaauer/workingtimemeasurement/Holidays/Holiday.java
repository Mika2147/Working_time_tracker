package com.mikaauer.workingtimemeasurement.Holidays;

public class Holiday {
    private int day;
    private int month;

    public Holiday(int day, int month) {
        this.day = day;
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
