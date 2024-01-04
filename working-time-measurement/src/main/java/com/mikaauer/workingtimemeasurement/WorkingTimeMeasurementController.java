package com.mikaauer.workingtimemeasurement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/time")
public class WorkingTimeMeasurementController {
    @CrossOrigin(origins = "*")
    @GetMapping()
    public ResponseEntity<WorkDayResponse> hello(@RequestParam(value = "month") Optional<String> month) {
        if(month.isPresent()){
            System.out.println("Month is present");
        }
        List<WorkDay> workDays = new ArrayList<WorkDay>();
        workDays.add(new WorkDay("02.01.2024", "08:00", "17:00", 60, "8:00"));
        workDays.add(new WorkDay("03.01.2024", "08:05", "17:30", 50, "8:35"));

        WorkDayResponse response = new WorkDayResponse(workDays);

        ResponseEntity entity = ResponseEntity
                .ok()
                .body(response);

        return entity;
    }
}
