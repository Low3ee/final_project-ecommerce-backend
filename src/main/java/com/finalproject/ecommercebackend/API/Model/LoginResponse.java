package com.finalproject.ecommercebackend.API.Model;

public class LoginResponse {

    private String jwt;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
