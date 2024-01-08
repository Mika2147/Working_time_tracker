package com.mikaauer.workingtimemeasurement.WorkDay;

import org.springframework.cglib.core.Local;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkDay {
    private int day;
    private int month;
    private int year;
    private int startingHour;
    private int startingMinute;
    private int endHour;
    private int endMinute;
    private int breakDuration;

    public WorkDay(String date, String startingTime, String endTime, int breakDuration) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
        LocalDate itemDate = LocalDate.parse(date, dateFormatter);
        this.day = itemDate.getDayOfMonth();
        this.month = itemDate.getMonthValue();
        this.year = itemDate.getYear();

        SimpleDateFormat timeFormatter = new SimpleDateFormat("H:mm");
        Calendar calendar = Calendar.getInstance();

        try {
            Date start = timeFormatter.parse(startingTime);
            calendar.setTime(start);
            this.startingHour = calendar.get(Calendar.HOUR_OF_DAY);
            this.startingMinute = calendar.get(Calendar.MINUTE);

            Date end = timeFormatter.parse(endTime);
            calendar.setTime(end);
            this.endHour = calendar.get(Calendar.HOUR_OF_DAY);
            this.endMinute = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        this.breakDuration = breakDuration;
    }

    public String getDate() {
        String dateString = String.format("%02d.%02d.%04d", this.day, this.month, this.year);
        return dateString;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getStartingTime() {
        return String.format("%02d:%02d", this.startingHour, this.startingMinute);
    }

    public String getEndTime() {
        return String.format("%02d:%02d", this.endHour, this.endMinute);
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public int getTotalWorkingTime() {
        int totalWorkingTime = (this.endHour - this.startingHour) * 60;
        totalWorkingTime += this.endMinute - this.startingMinute;
        return totalWorkingTime;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public int getStartingMinute() {
        return startingMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

    public void setStartingMinute(int startingMinute) {
        this.startingMinute = startingMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

}
