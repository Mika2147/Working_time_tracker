package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.UserDatabase.UserDatabaseConnector;
import com.mikaauer.wtmauthorizationserver.UserDatabase.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/login")
@SessionScope
public class WtmAuthorizationServerLoginController {

    @Autowired
    UserDatabaseConnector userDatabaseConnector;


    @GetMapping()
    public ResponseEntity<String> handleLoginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        if(authorization.startsWith("Basic ")){
            String[] auth =  authorization.split("Basic ")[1].split(":");
            // TODO: Validate user here and create token
            if (auth.length > 1) {
                String username = auth[0];
                Optional<Users> user = userDatabaseConnector.getUser(username);

                if(user.isPresent()){
                    if(user.get().getPassword().equals(auth[1])){
                        return ResponseEntity.ok("token");
                    }
                }
            }
            /*if(auth[0].equals("21232f297a57a5a743894a0e4a801fc3")){
                if (auth[1].equals("21232f297a57a5a743894a0e4a801fc3")){
                    return ResponseEntity.ok("token");
                }
            }*/
        }
        return ResponseEntity.notFound().build();
    }
}
