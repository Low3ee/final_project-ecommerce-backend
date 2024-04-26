package com.finalproject.ecommercebackend.API.Model;

public class LoginResponse {

    private String jwt;
    private boolean success;
    private String failureReason;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getJwt() {
        return jwt;
    }
}
