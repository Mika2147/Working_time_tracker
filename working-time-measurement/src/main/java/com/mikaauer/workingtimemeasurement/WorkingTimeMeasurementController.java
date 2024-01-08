package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Database.TimeMeasurementDatabaseConnector;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDayDTO;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/time")
@SessionScope
public class WorkingTimeMeasurementController {

    @Autowired
    private TimeMeasurementDatabaseConnector databaseConnector;

    @GetMapping()
    public ResponseEntity<WorkDayResponse> handleMonthOverviewRequest(@RequestParam(value = "month") Optional<String> month,
                                                                      @RequestParam(value = "year") Optional<String> year) {
        int localMonth = 0;
        int localYear = 0;

        LocalDate now = LocalDate.now();

        if(month.isPresent()){
            localMonth = Integer.parseInt(month.get());
        }else {
            localMonth = now.getMonthValue();
        }

        if(year.isPresent()){
            localYear = Integer.parseInt(year.get());
        }else{
            localYear = now.getYear();
        }

        List<WorkDay> workDays = databaseConnector.getWorkdays(localMonth, localYear);

        workDays.sort((WorkDay i1, WorkDay i2) -> Integer.compare(i1.getDay(), i2.getDay()));

        WorkDayResponse response = new WorkDayResponse(workDays);

        ResponseEntity entity = ResponseEntity
                .ok()
                .body(response);

        return entity;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<WorkDay> handleWorkDayPostRequest(@RequestBody WorkDayDTO body){
        System.out.println(body);
        WorkDay object = new WorkDay(body.getDate(), body.getStartingHour() + ":" + body.getStartingMinute(),
                body.getEndHour() + ":" + body.getEndMinute(), Integer.parseInt(body.getBreakDuration()));
        databaseConnector.insertWorkday(object);
        return ResponseEntity.ok(object);
    }
}
