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
    public ResponseEntity<VacationResponse> handleVacationOverviewRequest(@RequestParam(value = "year") Optional<Integer> year, @RequestParam(value = "month") Optional<Integer> month, @RequestParam(value = "username") Optional<String> username, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if ((username.isPresent() && validator.validate(authorization, true)) || (username.isEmpty() && validator.validate(authorization, false))) {
                List<Vacation> vacations;

                int restVacationDays = 0;

                if (year.isPresent()) {
                    List<Vacation> lastYearsVacation = databaseConnector.getVacations(year.get() -1, validator.getUsername(authorization));
                    List<Vacation> yearVacation = databaseConnector.getVacations(year.get(),
                            username.orElse(validator.getUsername(authorization)));
                    sortVacations(yearVacation);

                    if (month.isPresent()) {
                        vacations = databaseConnector.getVacations(year.get(), month.get(),
                                username.orElse(validator.getUsername(authorization)));
                        sortVacations(vacations);
                    } else {
                        vacations = yearVacation;
                    }

                    // TODO: if vacation is only valid until certain day change this line
                    restVacationDays += calculateRestVacationDays(lastYearsVacation, Optional.of(year.get() - 1));
                    restVacationDays += calculateRestVacationDays(yearVacation, year);
                } else {
                    vacations = databaseConnector.getVacations(username.orElse(validator.getUsername(authorization)));
                    sortVacations(vacations);
                    restVacationDays = calculateRestVacationDays(vacations, year);
                }

                VacationResponse response = new VacationResponse(vacations, restVacationDays);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/future")
    public ResponseEntity<VacationResponse> handleFutureVacationOverviewRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if (validator.validate(authorization, false)) {
                List<Vacation> vacations = databaseConnector.getFutureVacations(validator.getUsername(authorization));

                int year = Calendar.getInstance().get(Calendar.YEAR);
                List<Vacation> lastYearsVacation = databaseConnector.getVacations(year -1, validator.getUsername(authorization));
                List<Vacation> vacationsInYear = databaseConnector.getVacations(year,
                        validator.getUsername(authorization));

                sortVacations(vacations);

                int restVacationDays = calculateRestVacationDays(vacations, Optional.empty());
                // TODO: if vacation is only valid until certain day change this line
                restVacationDays += calculateRestVacationDays(lastYearsVacation, Optional.of(year - 1));

                VacationResponse response = new VacationResponse(vacations, restVacationDays);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Vacation> handleVacationPostRequest(@RequestBody VacationDTO body,
                                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if (validator.validate(authorization, false)) {

                Vacation vacation = new Vacation(body.getStartingDate(), body.getEndDate(),
                        validator.getUsername(authorization));

                if(checkRestVacationDays(vacation, validator.getUsername(authorization))) {
                    if (databaseConnector.insertVacation(vacation)) {
                        return ResponseEntity.ok(vacation);
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/accept")
    public ResponseEntity<Vacation> handleVacationAcceptRequest(@RequestParam(value = "id") int id,
                                                                @RequestParam(value = "username") String username,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if (validator.validate(authorization, true)) {
                Optional<Vacation> vacation = databaseConnector.getVacation(id, username);
                if (vacation.isPresent()) {
                    vacation.get().setAccepted(true);
                    if (databaseConnector.insertVacation(vacation.get())) {
                        return ResponseEntity.ok(vacation.get());
                    }
                }
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.badRequest().build();
    }

    private void sortVacations(List<Vacation> vacations) {
        vacations.sort(new Comparator<Vacation>() {
            @Override
            public int compare(Vacation o1, Vacation o2) {
                Date start1 = new GregorianCalendar(o1.getStartingDay(), o1.getStartingMonth() - 1,
                        o1.getStartingDay()).getTime();
                Date start2 = new GregorianCalendar(o2.getStartingYear(), o2.getStartingMonth() - 1,
                        o2.getStartingDay()).getTime();

                return start1.compareTo(start2);
            }
        });
    }

    private int calculateRestVacationDays(List<Vacation> sortedVacations, Optional<Integer> year) {
        //TODO: calculate with individual vacation days
        int restVacationDays = 30;

        if (year.isEmpty()) {
            year = Optional.of(Calendar.getInstance().get(Calendar.YEAR));
        }

        for (Vacation vacation : sortedVacations) {
            if (vacation.getStartingYear() > year.get()) {
                return restVacationDays;
            }
            restVacationDays = restVacationDays - Utils.getVacationDaysInYear(vacation, year.get());
        }

        return restVacationDays;
    }

    private boolean checkRestVacationDays(Vacation vacation, String username) {
        List<Vacation> vacations = databaseConnector.getVacations(username);
        sortVacations(vacations);

        // TODO: if days are only valid until a defined date then use this information in calculation of
        //  restdaysfromlastyear variable
        int restDaysFromLastYear = calculateRestVacationDays(vacations, Optional.of(vacation.getStartingYear() - 1));
        int vacationDaysInVacationYear = calculateRestVacationDays(vacations, Optional.of(vacation.getStartingYear()));

        if ((vacationDaysInVacationYear + restDaysFromLastYear) < Utils.getVacationDaysInYear(vacation,
                vacation.getStartingYear())) {
            return false;
        }

        if (vacation.getStartingYear() != vacation.getEndYear()) {
            if (calculateRestVacationDays(vacations, Optional.of(vacation.getEndYear())) < Utils.getVacationDaysInYear(vacation, vacation.getEndYear())) {
                return false;
            }
        }

        return true;
    }
}
