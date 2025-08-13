package com.oshanak.mobilemarket.Activity.Models;

import java.util.ArrayList;

public class GetNewOrderNotificationResult {

    private boolean isSuccessful;
    private String message;
    ArrayList<Object> dataStructure = new ArrayList<Object>();
    private String tag;

    // Getter Methods

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public ArrayList<Object> getDataStructure() {
        return dataStructure;
    }

    public void setDataStructure(ArrayList<Object> dataStructure) {
        this.dataStructure = dataStructure;
    }

    public boolean getIsSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public String getTag() {
        return tag;
    }

    // Setter Methods

    public void setIsSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
