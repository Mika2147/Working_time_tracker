package com.mikaauer.workingtimemeasurement.Validation;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mikaauer.workingtimemeasurement.Constants;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class Validator {

    private static final String JWK_PROVIDER_URL = "https://login.microsoftonline.com/8bde5d04-5399-492f-bb60-2767517ae322/discovery/v2.0/keys";

    public Validator() {
    }

    public boolean validate(String token, boolean needsAdminRights){
        if (token != null && token.startsWith("Bearer ")) {
            int index = token.indexOf("Bearer ") + 7;
            token = token.substring(index);
        } else {
            System.out.println("Malformed Token");
            return false;
        }
        DecodedJWT jwt = JWT.decode(token);

        Jwk jwk;
        JwkProvider provider;
        Algorithm algorithm;
        try {
            provider = new UrlJwkProvider(new URL(JWK_PROVIDER_URL));
            jwk = provider.get(jwt.getKeyId());
            algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
            Date now = new Date();
            Date notBefore = jwt.getNotBefore();
            Date expiresAt = jwt.getExpiresAt();
            boolean rightsCheck = needsAdminRights ? jwt.getClaim("roles").asList(String.class).contains("Admin") : true;
            System.out.println("Access rigths fullffiled: " + rightsCheck);
            return notBefore != null && expiresAt != null
                    && now.toInstant().compareTo(notBefore.toInstant()) >= 0
                    && now.toInstant().isBefore(expiresAt.toInstant())
                    && rightsCheck;
        } catch (MalformedURLException | JwkException | SignatureVerificationException e) {
            System.out.println(e);
            return false;
        }
    }

    public String getUsername(String token) throws IllegalAccessException {
        if (token != null && token.startsWith("Bearer ")) {
            int index = token.indexOf("Bearer ") + 7;
            token = token.substring(index);
        } else {
            throw new IllegalArgumentException("Token is not valid");
        }
        DecodedJWT jwt = JWT.decode(token);

        Jwk jwk;
        JwkProvider provider;
        Algorithm algorithm;
        try {
            provider = new UrlJwkProvider(new URL(JWK_PROVIDER_URL));
            jwk = provider.get(jwt.getKeyId());
            algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            String username = jwt.getClaim("unique_name").asString();
            return username;
        } catch (MalformedURLException | JwkException | SignatureVerificationException e) {
            System.out.println(e);
            throw new IllegalArgumentException("Token is not valid");
        }
    }
}
