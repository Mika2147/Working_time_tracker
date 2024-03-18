package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Database.TimeMeasurementDatabaseConnector;
import com.mikaauer.workingtimemeasurement.Validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user")
@SessionScope
public class UserController {

    @Autowired
    private TimeMeasurementDatabaseConnector databaseConnector;

    @GetMapping()
    public ResponseEntity<List<String>> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){

        if ((new Validator()).validate(authorization, true)) {
            List<String> usernames = databaseConnector.getUsernames();

            if (usernames.size() > 0) {
                return ResponseEntity.ok(usernames);
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
