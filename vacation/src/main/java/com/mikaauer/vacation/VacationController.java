package com.mikaauer.vacation;

import com.mikaauer.vacation.Database.VacationDatabaseConnector;
import com.mikaauer.vacation.Model.Vacation;
import com.mikaauer.vacation.Model.VacationDTO;
import com.mikaauer.vacation.Model.VacationResponse;
import com.mikaauer.vacation.Validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/vacation")
@SessionScope
public class VacationController {

    @Autowired
    private Validator validator;

    @Autowired
    private VacationDatabaseConnector databaseConnector;

    @GetMapping
    public ResponseEntity<VacationResponse> handleVacationOverviewRequest(@RequestParam(value = "year") Optional<Integer> year,
                                                                          @RequestHeader(HttpHeaders.AUTHORIZATION)String authorization){
        try{
            if(validator.validate(authorization)){
                List<Vacation> vacations;

                if(year.isPresent()){
                    vacations = databaseConnector.getVacations(year.get(), getUsername(authorization));
                }else {
                    vacations = databaseConnector.getVacations(getUsername(authorization));
                }

                sortVacations(vacations);

                int restVacationDays = calculateRestVacationDays(vacations, year);

                // TODO: Replace restVacationDays with computed value
                VacationResponse response = new VacationResponse(vacations, restVacationDays);
                return ResponseEntity.ok(response);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/future")
    public ResponseEntity<VacationResponse> handleFutureVacationOverviewRequest(@RequestHeader(HttpHeaders.AUTHORIZATION)String authorization){
        try{
            if(validator.validate(authorization)){
                List<Vacation> vacations = databaseConnector.getFutureVacations(getUsername(authorization));

                int year = Calendar.getInstance().get(Calendar.YEAR);
                List<Vacation> vacationsInYear = databaseConnector.getVacations(year, getUsername(authorization));

                sortVacations(vacations);

                int restVacationDays = calculateRestVacationDays(vacations, Optional.empty());

                VacationResponse response = new VacationResponse(vacations, restVacationDays);
                return ResponseEntity.ok(response);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Vacation> handleVacationPostRequest(@RequestBody VacationDTO body,
                                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        try {
            if ((new Validator()).validate(authorization)) {
                Vacation vacation = new Vacation(body.getStartingDate(), body.getEndDate(), getUsername(authorization));
                if(databaseConnector.insertVacation(vacation)){
                    return ResponseEntity.ok(vacation);
                }
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }


    private String getUsername(String authorization) throws IllegalAccessException {
        if (authorization.startsWith("Basic ")) {
            String[] auth = authorization.split("Basic ")[1].split(":");
            if (auth.length > 1) {
                return auth[0];
            } else {
                throw new IllegalArgumentException("Authorization Header does not contain username");
            }
        } else {
            throw new IllegalAccessException("Authorization Header is not valid");
        }
    }

    private void sortVacations(List<Vacation> vacations){
        vacations.sort(new Comparator<Vacation>() {
            @Override
            public int compare(Vacation o1, Vacation o2) {
                Date start1 = new GregorianCalendar(o1.getStartingDay(), o1.getStartingMonth() - 1, o1.getStartingDay()).getTime();
                Date start2 = new GregorianCalendar(o2.getStartingYear(), o2.getStartingMonth() - 1, o2.getStartingDay()).getTime();

                return start1.compareTo(start2);
            }
        });
    }

    private int calculateRestVacationDays(List<Vacation> sortedVacations, Optional<Integer> year){
        //TODO: calculate with individual vacation days
        int restVacationDays = 30;

        if(year.isEmpty()){
            year = Optional.of(Calendar.getInstance().get(Calendar.YEAR));
        }

        for(Vacation vacation: sortedVacations){
            if(vacation.getStartingYear() > year.get()){
                return restVacationDays;
            }
            restVacationDays = restVacationDays - Utils.getVacationDaysInYear(vacation, year.get());
        }

        return restVacationDays;
    }
}
