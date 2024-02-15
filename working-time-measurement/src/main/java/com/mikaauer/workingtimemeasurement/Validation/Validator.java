package com.mikaauer.workingtimemeasurement.Validation;

import com.mikaauer.workingtimemeasurement.Constants;

import java.net.HttpURLConnection;
import java.net.URL;

public class Validator {

    public Validator() {
    }

    public boolean validate(String authorization, boolean needsAdminRights){
        try{
            URL url = new URL((Constants.AUTHORIZATION_SERVER_VALIDATION_ENDPOINT_URL + "?needsadminrights=" + needsAdminRights));
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
