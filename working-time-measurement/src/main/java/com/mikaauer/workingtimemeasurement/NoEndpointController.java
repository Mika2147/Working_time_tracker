package com.mikaauer.workingtimemeasurement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
@SessionScope
public class NoEndpointController {

    @GetMapping("*")
    public ResponseEntity<String> handleVacationOverviewRequest() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
