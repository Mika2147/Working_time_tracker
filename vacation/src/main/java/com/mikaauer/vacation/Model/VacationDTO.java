package com.mikaauer.vacation.Model;

import java.util.Optional;

public class VacationDTO {

    private String startingDate;
    private String endDate;

    public VacationDTO(String startingDate, String endDate) {
        this.startingDate = startingDate;
        this.endDate = endDate;
    }



    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
