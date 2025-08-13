package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class PickingControlHeaderRequestBodyModel {
    private String storeID;
    private String VBELN;
    private String fromDate;
    private String toDate;

    private MetaData metaData;

    public PickingControlHeaderRequestBodyModel(String VBELN, String fromDate, String toDate, MetaData metaData) {
        this.VBELN = VBELN;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.metaData = metaData;
    }

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN;
    }

    public PickingControlHeaderRequestBodyModel() {
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String userName) {
        this.storeID = userName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "PickingControlHeaderRequestBodyModel{" +
                "storeID='" + storeID + '\'' +
                ", VBELN='" + VBELN + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
