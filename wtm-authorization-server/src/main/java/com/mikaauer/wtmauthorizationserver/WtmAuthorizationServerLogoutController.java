package com.mikaauer.wtmauthorizationserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/logout")
@SessionScope
public class WtmAuthorizationServerLogoutController {

    @GetMapping()
    public ResponseEntity<String> handleLogoutRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        if(authorization.startsWith("Basic ")){
            String token =  authorization.split("Basic ")[1];
            // TODO: Validate token here and logout
            if(!token.isEmpty()){
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
