package com.mikaauer.workingtimemeasurement.Vacation;

import java.util.List;

public class VacationResponse {
    private List<Vacation> items;

    private int restVacationDays;

    public VacationResponse(List<Vacation> items, int restVacationDays) {
        this.items = items;
        this.restVacationDays = restVacationDays;
    }

    public List<Vacation> getItems() {
        return items;
    }

    public void setItems(List<Vacation> items) {
        this.items = items;
    }

    public int getRestVacationDays() {
        return restVacationDays;
    }

    public void setRestVacationDays(int restVacationDays) {
        this.restVacationDays = restVacationDays;
    }
}
