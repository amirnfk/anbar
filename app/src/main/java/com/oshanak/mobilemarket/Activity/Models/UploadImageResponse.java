package com.oshanak.mobilemarket.Activity.Models;

public class UploadImageResponse {
    String isSuccessful;
    String message;
    int imageID;

    public UploadImageResponse(String isSuccessful, String message, int imageID) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.imageID = imageID;
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    @Override
    public String toString() {
        return "UploadImageResponse{" +
                "isSuccessful='" + isSuccessful + '\'' +
                ", message='" + message + '\'' +
                ", imageID=" + imageID +
                '}';
    }
}
