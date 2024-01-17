package com.mikaauer.wtmauthorizationserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/login")
@SessionScope
public class WtmAuthorizationServerLoginController {


    @GetMapping()
    public ResponseEntity<String> handleLoginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        if(authorization.startsWith("Basic ")){
            String[] auth =  authorization.split("Basic ")[1].split(":");
            // TODO: Validate user here and create token
            if(auth[0].equals("21232f297a57a5a743894a0e4a801fc3")){
                if (auth[1].equals("21232f297a57a5a743894a0e4a801fc3")){
                    return ResponseEntity.ok("token");
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
