package com.oshanak.mobilemarket.Activity.Models;

public class DocResponse {
    String isSuccessful;
    String message;
    int HeaderID;

    public DocResponse(String isSuccessful, String message, int headerID) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        HeaderID = headerID;
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

    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int headerID) {
        HeaderID = headerID;
    }
}
