package com.mikaauer.workingtimemeasurement.WorkDay;

public class WorkDayDTO {
    private String date;
    private String startingHour;
    private String startingMinute;
    private String endHour;
    private String endMinute;
    private String breakDuration;

    private String tasks;
    private String comment;

    public WorkDayDTO(String date, String startingHour, String startingMinute, String endHour, String endMinute, String breakDuration) {
        this.date = date;
        this.startingHour = startingHour;
        this.startingMinute = startingMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.breakDuration = breakDuration;
    }

    public String getDate() {
        return date;
    }

    public String getStartingHour() {
        return startingHour;
    }

    public String getStartingMinute() {
        return startingMinute;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public String getBreakDuration() {
        return breakDuration;
    }

    public String getTasks() {
        return tasks;
    }

    public String getComment() {
        return comment;
    }
}
