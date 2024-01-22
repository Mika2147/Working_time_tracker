package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
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
            String auth =  authorization.split("Basic ")[1];
            String[] splitted = auth.split(":");
            String username = splitted[0];
            String tokenString = splitted[1];

            if(TokenManager.getInstance().validate(username, tokenString)){
                TokenManager.getInstance().removeToken(username);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
