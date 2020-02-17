package com.ternobo.pikano.database;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String profileurl;
    private int id;
    private int grade;

    public User(String name, String profileurl, int id, int grade) {
        this.name = name;
        this.profileurl = profileurl;
        this.id = id;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
