package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Database.TimeMeasurementDatabaseConnector;
import com.mikaauer.workingtimemeasurement.Export.Excel.ExcelExporter;
import com.mikaauer.workingtimemeasurement.Validation.Validator;
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
import java.util.*;

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
                                                                      @RequestParam(value = "username") Optional<String> username,
                                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if ((username.isPresent() && new Validator().validate(authorization, true)) ||
                    (username.isEmpty() && (new Validator()).validate(authorization, false))) {
                LocalDate now = LocalDate.now();

                int localMonth = now.getMonthValue();
                int localYear = now.getYear();

                if (month.isPresent()) {
                    localMonth = Integer.parseInt(month.get());
                }

                if (year.isPresent()) {
                    localYear = Integer.parseInt(year.get());
                }

                List<WorkDay> workDays = databaseConnector.getWorkdays(localMonth, localYear, username.orElse(getUsername(authorization)));

                workDays.sort((WorkDay i1, WorkDay i2) -> Integer.compare(i1.getDay(), i2.getDay()));

                Calendar calendar = Calendar.getInstance();
                calendar.set(localYear, localMonth, 1);
                Date start = calendar.getTime();

                Date end;

                if (now.getMonthValue() == localMonth && now.getYear() == localYear) {
                    calendar.set(localYear, localMonth, now.getDayOfMonth());
                    end = calendar.getTime();
                } else {
                    calendar.set(localYear, localMonth, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
                    end = calendar.getTime();
                }

                // TODO: Make individual daily working hours
                double neededWorkingHours = Utils.getWorkingDaysBetweenTwoDates(start, end) * 8;

                WorkDayResponse response = new WorkDayResponse(workDays, neededWorkingHours);

                ResponseEntity entity = ResponseEntity
                        .ok()
                        .body(response);

                return entity;
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(401).build();
    }


    @GetMapping()
    @RequestMapping("/day")
    public ResponseEntity<WorkDay> handleDayOverviewRequest(@RequestParam(value = "day") Optional<Integer> day,
                                                            @RequestParam(value = "month") Optional<Integer> month,
                                                            @RequestParam(value = "year") Optional<Integer> year,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if ((new Validator().validate(authorization, false))) {
                LocalDate now = LocalDate.now();

                int localDay = now.getDayOfMonth();
                int localMonth = now.getMonthValue();
                int localYear = now.getYear();

                if (day.isPresent()) {
                    localDay = day.get();
                }

                if (month.isPresent()) {
                    localMonth = month.get();
                }

                if (year.isPresent()) {
                    localYear = year.get();
                }

                Optional<WorkDay> workday = databaseConnector.getWorkday(localDay, localMonth, localYear, getUsername(authorization));

                if (workday.isPresent()) {
                    return ResponseEntity.ok(workday.get());
                }


            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<WorkDay> handleWorkDayPostRequest(@RequestBody WorkDayDTO body, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if ((new Validator()).validate(authorization, false)) {
                WorkDay object = new WorkDay(body.getDate(), body.getStartingHour() + ":" + body.getStartingMinute(),
                        body.getEndHour() + ":" + body.getEndMinute(), Integer.parseInt(body.getBreakDuration()),
                        getUsername(authorization), body.getTasks(), body.getComment());
                databaseConnector.insertWorkday(object);
                return ResponseEntity.ok(object);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping()
    @RequestMapping("/export")
    public ResponseEntity<Resource> handleOverviewExportDownload(@RequestParam(value = "month") Optional<Integer> month,
                                                                 @RequestParam(value = "year") Optional<Integer> year,
                                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            LocalDate now = LocalDate.now();

            int localMonth = now.getMonthValue();
            int localYear = now.getYear();

            if (month.isPresent()) {
                localMonth = month.get();
            }

            if (year.isPresent()) {
                localYear = year.get();
            }

            List<WorkDay> workdays = databaseConnector.getWorkdays(localMonth, localYear, getUsername(authorization));
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    private String getUsername(String authorization) throws IllegalAccessException {
        if (authorization.startsWith("Basic ")) {
            String[] auth = authorization.split("Basic ")[1].split(":");
            // TODO: Validate user here and create token
            if (auth.length > 1) {
                return auth[0];
            } else {
                throw new IllegalArgumentException("Authorization Header does not contain username");
            }
        } else {
            throw new IllegalAccessException("Authorization Header is not valid");
        }
    }
}
