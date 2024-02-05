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

                int restVacationDays = 30;

                if(year.isEmpty()){
                    year = Optional.of(Calendar.getInstance().get(Calendar.YEAR));
                }

                for(Vacation vacation: vacations){
                    restVacationDays = restVacationDays - Utils.getVacationDaysInYear(vacation, year.get());
                }

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

                //TODO: calculate with individual vacation days
                int restVacationDays = 30;

                int year = Calendar.getInstance().get(Calendar.YEAR);
                List<Vacation> vacationsInYear = databaseConnector.getVacations(year, getUsername(authorization));

                for(Vacation vacation: vacationsInYear){
                    restVacationDays = restVacationDays - Utils.getVacationDaysInYear(vacation, year);
                }

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
