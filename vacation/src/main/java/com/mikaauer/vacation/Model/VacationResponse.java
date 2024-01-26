package com.mikaauer.vacation.Model;

import java.util.List;

public class VacationResponse {
    private List<Vacation> vacations;

    public VacationResponse(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }
}
