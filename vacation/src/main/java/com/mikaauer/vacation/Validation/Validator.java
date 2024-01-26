package com.mikaauer.vacation.Validation;

import com.mikaauer.vacation.Constants;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class Validator {

    private boolean needsAdminRights = false;

    public Validator(boolean needsAdminRights) {
        this.needsAdminRights = needsAdminRights;
    }

    public Validator() {
    }

    public boolean validate(String authorization){
        try{
            URL url = new URL((Constants.AUTHORIZATION_SERVER_VALIDATION_ENDPOINT_URL + "?needsadminrights=false"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Authorization", authorization);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("GET");

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                return true;
            }
        }catch (Exception e){
            System.err.println(e);
        }

        return false;
    }
}
