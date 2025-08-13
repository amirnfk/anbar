package com.oshanak.mobilemarket.Activity.Service.Retrofit;

public class LoginResponse {
    private boolean isSuccess;
    private ErrorResponse error;

    public boolean isSuccess() {
        return isSuccess;
    }

    public ErrorResponse getError() {
        return error;
    }
}