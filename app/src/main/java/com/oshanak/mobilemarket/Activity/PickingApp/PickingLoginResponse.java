package com.oshanak.mobilemarket.Activity.PickingApp;


import com.google.gson.annotations.SerializedName;

public class PickingLoginResponse {
    @SerializedName("isSuccessful")
    private boolean isSuccessful;

    @SerializedName("message")
    private String message;

    @SerializedName("UserType")
    private String userType;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Getters and setters
}
