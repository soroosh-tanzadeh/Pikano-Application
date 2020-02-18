package com.ternobo.pikano.RESTobjects;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private String phone;
    private int grade;
    private String api_token;
    private String phone_verified_at;
    private String account_charged_at;
    private String profile;
    private String created_at;
    private String updated_at;

    public User() {
    }

    public User(int id, String name, String phone, int grade, String api_token, String phone_verified_at, String account_charged_at, String profile, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.grade = grade;
        this.api_token = api_token;
        this.phone_verified_at = phone_verified_at;
        this.account_charged_at = account_charged_at;
        this.profile = profile;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getPhone_verified_at() {
        return phone_verified_at;
    }

    public void setPhone_verified_at(String phone_verified_at) {
        this.phone_verified_at = phone_verified_at;
    }

    public String getAccount_charged_at() {
        return account_charged_at;
    }

    public void setAccount_charged_at(String account_charged_at) {
        this.account_charged_at = account_charged_at;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
