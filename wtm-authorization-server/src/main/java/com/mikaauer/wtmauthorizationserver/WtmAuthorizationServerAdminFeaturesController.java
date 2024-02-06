package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
import com.mikaauer.wtmauthorizationserver.User.DataTransfer.UserDTO;
import com.mikaauer.wtmauthorizationserver.User.DataTransfer.UsersResponse;
import com.mikaauer.wtmauthorizationserver.User.UserDatabase.UserRepository;
import com.mikaauer.wtmauthorizationserver.User.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/admin")
@SessionScope
public class WtmAuthorizationServerAdminFeaturesController {

    private final UserRepository userRepository;

    public WtmAuthorizationServerAdminFeaturesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponse> getUserOverview(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (TokenManager.getInstance().validate(authorization, true)) {
            List<UserDTO> users = new ArrayList<>();
            userRepository.findAll().forEach(user -> {
                users.add(new UserDTO(user.getId(), user.getName(), user.getIsadmin()));
            });

            return ResponseEntity.ok(new UsersResponse(users));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable UUID id) {
        if (TokenManager.getInstance().validate(authorization, true)) {
            if(!id.equals(UUID.fromString("0e467611-4f82-4736-871f-f4095d770011"))){
                userRepository.deleteById(id);
                return ResponseEntity.ok(true);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable UUID id, @RequestBody Users user) {
        if (TokenManager.getInstance().validate(authorization, true)) {
            Optional<Users> current = userRepository.findById(id);

            if(current.isPresent()){
                if(user.getPassword().isBlank()){
                    user.setPassword(current.get().getPassword());
                }

                userRepository.save(user);
                return ResponseEntity.ok(new UserDTO(user.getId(), user.getName(), user.getIsadmin()));
            }

        }

        return ResponseEntity.badRequest().build();
    }

}
