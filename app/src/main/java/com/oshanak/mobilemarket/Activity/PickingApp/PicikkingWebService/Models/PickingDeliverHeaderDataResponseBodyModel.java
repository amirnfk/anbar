package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;


import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;

import java.util.List;

public class PickingDeliverHeaderDataResponseBodyModel {
    private List<PickingDeliverHeaderData> pickingHeaderList;
    public   boolean isSuccessful;
    private String message;

    public PickingDeliverHeaderDataResponseBodyModel(List<PickingDeliverHeaderData> pickingHeaderList, boolean isSuccessful, String message) {
        this.pickingHeaderList = pickingHeaderList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<PickingDeliverHeaderData> getPickingHeaderList() {
        return pickingHeaderList;
    }

    public void setPickingHeaderList(List<PickingDeliverHeaderData> pickingHeaderList) {
        this.pickingHeaderList = pickingHeaderList;
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

    @Override
    public String toString() {
        return "PickingHeaderDataResponseBodyModel{" +
                "pickingHeaderList=" + pickingHeaderList +
                ", isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                '}';
    }
}
