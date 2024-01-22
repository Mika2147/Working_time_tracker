package com.mikaauer.wtmauthorizationserver.Token;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TokenManager {
    Long TOKEN_LIFETIME = 900L;
    Long TOKEN_LENGTH = 128L;

    private static TokenManager shared;

    private Map<String, Token> tokenMap;

    private TokenManager(){
        this.tokenMap = new ConcurrentHashMap<>();
    }

    public static TokenManager getInstance(){
        if(shared == null){
            shared = new TokenManager();
        }

        return shared;
    }

    public boolean validate(String username, String tokenString){
        if(tokenMap.containsKey(username)){
            Token token = tokenMap.get(username);
            if(token.getTokenString().equals(tokenString)){
                if((token.getCreatedTimestamp() + TOKEN_LIFETIME) > (new Date()).getTime()){
                    return true;
                }else {
                    tokenMap.remove(username);
                }
            }
        }
        return true;
    }

    public Token generateNewToken(String username){
        Token token = new Token(username, generateTokenString(), ((new Date()).getTime() + TOKEN_LIFETIME));
        tokenMap.put(username, token);
        return token;
    }

    public void removeToken(String username){
        if(tokenMap.containsKey(username)){
            tokenMap.remove(username);
        }
    }

    private String generateTokenString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(TOKEN_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}
