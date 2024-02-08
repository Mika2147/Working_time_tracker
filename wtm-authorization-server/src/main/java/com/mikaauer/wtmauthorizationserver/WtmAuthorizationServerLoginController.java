package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
import com.mikaauer.wtmauthorizationserver.User.UserDatabase.UserRepository;
import com.mikaauer.wtmauthorizationserver.User.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/login")
@SessionScope
public class WtmAuthorizationServerLoginController {

    private final UserRepository userRepository;

    public WtmAuthorizationServerLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping()
    public ResponseEntity<String> handleLoginRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        if(authorization.startsWith("Basic ")){
            String[] auth =  authorization.split("Basic ")[1].split(":");
            // TODO: Validate user here and create token
            if (auth.length > 1) {
                String username = auth[0];
                var res = userRepository.findByName(username);

                Optional<Users> user = res.stream().findFirst();

                if (user.isPresent()) {
                    if (user.get().getPassword().equals(auth[1])) {
                        String tokenString = TokenManager.getInstance().generateNewToken(username, user.get().getIsadmin()).getTokenString();
                        return ResponseEntity.ok(tokenString);
                    }
                }
                return ResponseEntity.status(401).build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
