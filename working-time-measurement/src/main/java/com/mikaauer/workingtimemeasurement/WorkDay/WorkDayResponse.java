package com.mikaauer.workingtimemeasurement.WorkDay;

import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;

import java.util.List;

public class WorkDayResponse {
    private List<WorkDay> items;

    public WorkDayResponse(List<WorkDay> items) {
        this.items = items;
    }

    public List<WorkDay> getItems() {
        return items;
    }

    public void setItems(List<WorkDay> items) {
        this.items = items;
    }
}
