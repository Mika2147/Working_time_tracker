package com.mikaauer.workingtimemeasurement;

public class WorkDay {
    private String date;
    private String startingTime;
    private String endTime;
    private int breakDuration;
    private String totalWorkingTime;

    public WorkDay(String date, String startingTime, String endTime, int breakDuration, String totalWorkingTime) {
        this.date = date;
        this.startingTime = startingTime;
        this.endTime = endTime;
        this.breakDuration = breakDuration;
        this.totalWorkingTime = totalWorkingTime;
    }

    public String getDate() {
        return date;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public String getTotalWorkingTime() {
        return totalWorkingTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public void setTotalWorkingTime(String totalWorkingTime) {
        this.totalWorkingTime = totalWorkingTime;
    }
}
