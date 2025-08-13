package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;


 import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;

import java.util.List;

public class PickingControlHeaderDataResponseBodyModel {
    private List<PickingDeliverHeaderData> pickingControlList;
    public   boolean isSuccessful;
    private String message;

    public PickingControlHeaderDataResponseBodyModel(List<PickingDeliverHeaderData> pickingControlList, boolean isSuccessful, String message) {
        this.pickingControlList = pickingControlList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<PickingDeliverHeaderData> getPickingHeaderList() {
        return pickingControlList;
    }

    public void setPickingHeaderList(List<PickingDeliverHeaderData> pickingHeaderList) {
        this.pickingControlList = pickingHeaderList;
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
                "pickingHeaderList=" + pickingControlList +
                ", isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                '}';
    }
}
