package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

public class UpdatePickingItemStatusResponseModel {
    boolean isSuccessful;

    String  message;

    int OrgDeliverAmount;

    public UpdatePickingItemStatusResponseModel(boolean isSuccessful, String message, int orgDeliverAmount) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        OrgDeliverAmount = orgDeliverAmount;
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

    public int getOrgDeliverAmount() {
        return OrgDeliverAmount;
    }

    public void setOrgDeliverAmount(int orgDeliverAmount) {
        OrgDeliverAmount = orgDeliverAmount;
    }

    @Override
    public String toString() {
        return "UpdatePickingItemStatusResponseModel{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", OrgDeliverAmount=" + OrgDeliverAmount +
                '}';
    }
}
