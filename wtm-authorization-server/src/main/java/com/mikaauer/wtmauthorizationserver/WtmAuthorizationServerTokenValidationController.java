package com.mikaauer.wtmauthorizationserver;

import com.mikaauer.wtmauthorizationserver.Token.TokenManager;
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
    public ResponseEntity<String> handleValidationRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestParam(value = "needsadminrights", defaultValue = "false") boolean needsAdminRights) {

        if (TokenManager.getInstance().validate(authorization, needsAdminRights)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
