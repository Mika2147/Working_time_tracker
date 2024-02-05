package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
import com.mikaauer.wtmauthorizationserver.UserDatabase.UserRepository;
import com.mikaauer.wtmauthorizationserver.UserDatabase.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/register")
@SessionScope
public class WtmAuthorizationServerRegisterController {
    private final UserRepository userRepository;

    public WtmAuthorizationServerRegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Boolean> canRegisterRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        if (authorization.startsWith("Basic ")) {
            String auth = authorization.split("Basic ")[1];
            String[] splitted = auth.split(":");
            String username = splitted[0];
            String tokenString = splitted[1];

            if (TokenManager.getInstance().validate(username, tokenString, true)) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping()
    public ResponseEntity<String> handleRegisterRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Credentials credentials) {
        if (authorization.startsWith("Basic ")) {
            String auth = authorization.split("Basic ")[1];
            String[] splitted = auth.split(":");
            String username = splitted[0];
            String tokenString = splitted[1];

            if (TokenManager.getInstance().validate(username, tokenString, true)) {
                Users user = new Users(null, credentials.getUsername(), credentials.getPassword(), credentials.isAdmin());
                userRepository.save(user);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
