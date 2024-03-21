package com.mikaauer.vacation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

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
