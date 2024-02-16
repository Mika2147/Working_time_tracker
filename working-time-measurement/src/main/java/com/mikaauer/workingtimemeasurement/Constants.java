package com.mikaauer.workingtimemeasurement;

public class Constants {
    public static final String AUTHORIZATION_SERVER_URL = System.getenv("AUTHORIZATION_SERVICE_PATH") != null ? System.getenv("AUTHORIZATION_SERVICE_PATH") : "http://localhost:8083";
    public static final String AUTHORIZATION_SERVER_VALIDATION_ENDPOINT_URL = AUTHORIZATION_SERVER_URL + "/token-validation";
    public static final String VACATION_SERVER_URL = System.getenv("VACATION_SERVER_PATH") != null ? System.getenv("VACATION_SERVER_PATH") : "http://localhost:8081";
    public static final String EXCEL_FILE_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
