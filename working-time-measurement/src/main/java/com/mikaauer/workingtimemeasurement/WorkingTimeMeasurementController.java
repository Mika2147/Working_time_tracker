package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Database.TimeMeasurementDatabaseConnector;
import com.mikaauer.workingtimemeasurement.Export.Excel.ExcelExporter;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDayDTO;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                                                                      @RequestParam(value = "year") Optional<String> year,
                                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        LocalDate now = LocalDate.now();

        int localMonth = now.getMonthValue();
        int localYear = now.getYear();

        if(month.isPresent()){
            localMonth = Integer.parseInt(month.get());
        }

        if(year.isPresent()){
            localYear = Integer.parseInt(year.get());
        }

        List<WorkDay> workDays = databaseConnector.getWorkdays(localMonth, localYear);

        workDays.sort((WorkDay i1, WorkDay i2) -> Integer.compare(i1.getDay(), i2.getDay()));

        WorkDayResponse response = new WorkDayResponse(workDays);

        ResponseEntity entity = ResponseEntity
                .ok()
                .body(response);

        return entity;
    }

    @GetMapping()
    @RequestMapping("/day")
    public ResponseEntity<WorkDay> handleDayOverviewRequest(@RequestParam(value="day")Optional<Integer> day,
                                                            @RequestParam(value="month")Optional<Integer> month,
                                                            @RequestParam(value="year")Optional<Integer> year,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){

        LocalDate now = LocalDate.now();

        int localDay = now.getDayOfMonth();
        int localMonth = now.getMonthValue();
        int localYear = now.getYear();

        if(day.isPresent()){
            localDay = day.get();
        }

        if(month.isPresent()){
            localMonth = month.get();
        }

        if(year.isPresent()){
            localYear = year.get();
        }

        Optional<WorkDay> workday = databaseConnector.getWorkday(localDay, localMonth, localYear);

        if(workday.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workday.get());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<WorkDay> handleWorkDayPostRequest(@RequestBody WorkDayDTO body,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        System.out.println(body);
        WorkDay object = new WorkDay(body.getDate(), body.getStartingHour() + ":" + body.getStartingMinute(),
                body.getEndHour() + ":" + body.getEndMinute(), Integer.parseInt(body.getBreakDuration()));
        databaseConnector.insertWorkday(object);
        return ResponseEntity.ok(object);
    }

    @GetMapping()
    @RequestMapping("/export")
    public ResponseEntity<Resource> handleOverviewExportDownload(@RequestParam(value="month") Optional<Integer> month,
                                                                 @RequestParam(value="year")Optional<Integer> year,
                                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        LocalDate now = LocalDate.now();

        int localMonth = now.getMonthValue();
        int localYear = now.getYear();

        if(month.isPresent()){
            localMonth = month.get();
        }

        if(year.isPresent()){
            localYear = year.get();
        }

        List<WorkDay> workdays = databaseConnector.getWorkdays(localMonth, localYear);
        workdays.sort((WorkDay i1, WorkDay i2) -> Integer.compare(i1.getDay(), i2.getDay()));

        ExcelExporter exporter = new ExcelExporter();
        String filePath = exporter.writeTimeFile(workdays);

        try {
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath));
            return ResponseEntity
                    .ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}
