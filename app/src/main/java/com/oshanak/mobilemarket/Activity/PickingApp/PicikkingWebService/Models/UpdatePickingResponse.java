package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

public class UpdatePickingResponse {
    private boolean isSuccessful;
    private String message;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "UpdatePickingResponse{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                '}';
    }
}
