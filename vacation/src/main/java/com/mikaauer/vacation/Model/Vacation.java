package com.mikaauer.vacation.Model;

import java.util.Objects;

public class Vacation {
    private int id;
    private int startingDay;
    private int startingMonth;
    private int startingYear;
    private int endDay;
    private int endMonth;
    private int endYear;
    private String username;

    public Vacation(int id, int startingDay, int startingMonth, int startingYear, int endDay, int endMonth, int endYear, String username) {
        this.id = id;
        this.startingDay = startingDay;
        this.startingMonth = startingMonth;
        this.startingYear = startingYear;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartingDay() {
        return startingDay;
    }

    public void setStartingDay(int startingDay) {
        this.startingDay = startingDay;
    }

    public int getStartingMonth() {
        return startingMonth;
    }

    public void setStartingMonth(int startingMonth) {
        this.startingMonth = startingMonth;
    }

    public int getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return id == vacation.id && startingDay == vacation.startingDay && startingMonth == vacation.startingMonth && startingYear == vacation.startingYear && endDay == vacation.endDay && endMonth == vacation.endMonth && endYear == vacation.endYear && Objects.equals(username, vacation.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startingDay, startingMonth, startingYear, endDay, endMonth, endYear, username);
    }
}
