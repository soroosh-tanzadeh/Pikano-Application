package com.ternobo.pikano.RESTobjects.requests;

public class URLs {

    private final static String baseURL = "https://pikano.ternobo.info/api/";

    public final static String sendVerification = baseURL + "sendvcode";
    public final static String verifyPhone = baseURL + "verifyPhone";
    public final static String setupProfile = baseURL + "setupprofile";
    public final static String getUserInfo = baseURL + "userinfo";
    public final static String getGrades = baseURL + "getgrades";
    public final static String getBooks = baseURL + "getbooks";
    public final static String getMostRatedFiles = baseURL + "getmostratedfiles";
    public final static String getFiles = baseURL + "getfiles";

    // Download
    public final static String downloadFile = baseURL + "files/";
}
