package com.ternobo.pikano.RESTobjects.requests;

public class Profile {

    private String token;
    private String name;
    private int grade;
    private String profile;

    public Profile(String token, String name, int grade, String profile) {
        this.token = token;
        this.name = name;
        this.grade = grade;
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
