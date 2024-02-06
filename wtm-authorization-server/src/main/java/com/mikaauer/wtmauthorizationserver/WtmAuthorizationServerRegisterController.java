package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
import com.mikaauer.wtmauthorizationserver.User.UserDatabase.UserRepository;
import com.mikaauer.wtmauthorizationserver.User.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

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
    public ResponseEntity<Boolean> canRegisterRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        if (TokenManager.getInstance().validate(authorization, true)) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.ok(false);
    }

    @PostMapping()
    public ResponseEntity<String> handleRegisterRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Credentials credentials) {
        if (TokenManager.getInstance().validate(authorization, true)) {
            Users user = new Users(null, credentials.getUsername(), credentials.getPassword(), credentials.isAdmin());
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
