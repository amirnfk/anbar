package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.SapPickingResult;

public class updatePickingHeaderStatusResponse {
    private boolean isSuccessful;
    private String message;
    private SapPickingResult sapPickingResult;

    public updatePickingHeaderStatusResponse(boolean isSuccessful, String message, SapPickingResult sapPickingResult) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.sapPickingResult = sapPickingResult;
    }

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

    public SapPickingResult getSapPickingResult() {
        return sapPickingResult;
    }

    public void setSapPickingResult(SapPickingResult sapPickingResult) {
        this.sapPickingResult = sapPickingResult;
    }

    @Override
    public String toString() {
        return "updatePickingHeaderStatusResponse{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", sapPickingResult=" + sapPickingResult +
                '}';
    }
    public boolean isExceptionOccured(String exceptionKeyWord)
    {
        return message.toLowerCase().contains(exceptionKeyWord.toLowerCase());
    }

    // Getters and setters (if needed)
}
