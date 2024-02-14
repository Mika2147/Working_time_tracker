package com.mikaauer.workingtimemeasurement.WorkDay;

import java.util.List;

public class WorkDayResponse {
    private List<WorkDay> items;
    private double hoursInTimeRange;

    public WorkDayResponse(List<WorkDay> items, double hoursInTimeRange) {
        this.items = items;
        this.hoursInTimeRange = hoursInTimeRange;
    }

    public List<WorkDay> getItems() {
        return items;
    }

    public void setItems(List<WorkDay> items) {
        this.items = items;
    }

    public double getHoursInTimeRange() {
        return hoursInTimeRange;
    }

    public void setHoursInTimeRange(double hoursInTimeRange) {
        this.hoursInTimeRange = hoursInTimeRange;
    }

    public double getWorkedHours(){
        double workedHours = 0;

        for(WorkDay workDay: items){
            workedHours += (workDay.getTotalWorkingTime() / 60.0);
        }

        return workedHours;
    }

    public double getTimeBalance(){
        return getWorkedHours() - hoursInTimeRange;
    }
}
