package com.ternobo.pikano.RESTobjects.requests;

public class URLs {

    private final static String baseURL = "https://pikano.ternobo.info/api/";

    public final static String sendVerification = baseURL + "sendvcode";
    public final static String verifyPhone = baseURL + "verifyPhone";
    public final static String setProfileName = baseURL + "setprofilename";
    public final static String setProfilePicture = baseURL + "setprofilepicture";
    public final static String setGrade = baseURL + "setgrade";
    public final static String getProfilePicture = "avatar/";
    public final static String getUserInfo = baseURL + "userinfo";
    public final static String getGrades = baseURL + "getrades";

}
