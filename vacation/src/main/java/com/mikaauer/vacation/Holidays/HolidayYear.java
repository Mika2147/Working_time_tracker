package com.mikaauer.vacation.Holidays;

import java.util.List;

public class HolidayYear {
    private List<Holiday> holidays;
    private int year;

    public HolidayYear(List<Holiday> holidays, int year) {
        this.holidays = holidays;
        this.year = year;
    }

    public boolean isHoliday(int day, int month){
        for(Holiday holiday: holidays){
            if(holiday.getDay() == day && holiday.getMonth() == month){
                return true;
            }
        }

        return false;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
