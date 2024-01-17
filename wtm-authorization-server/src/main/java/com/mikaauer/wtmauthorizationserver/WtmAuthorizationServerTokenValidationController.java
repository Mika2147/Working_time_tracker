package com.mikaauer.wtmauthorizationserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/token-validation")
@SessionScope
public class WtmAuthorizationServerTokenValidationController {

    @GetMapping()
    public ResponseEntity<String> handleLoginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization.startsWith("Basic ")) {
            String auth = authorization.split("Basic ")[1];
            // TODO: Validate Token here
            if (!auth.isEmpty()) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
