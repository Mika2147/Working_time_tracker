package com.mikaauer.wtmauthorizationserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/register")
@SessionScope
public class WtmAuthorizationServerRegisterController {

    @PostMapping()
    public ResponseEntity<String> handleLoginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Credentials credentials) {
        if (authorization.startsWith("Basic ")) {
            String auth = authorization.split("Basic ")[1];
            // TODO: Validate Token here
            if (!auth.isEmpty()) {
                // TODO: Start Registration here
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
