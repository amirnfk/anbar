package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class PickingDeliverHeaderRequestBodyModel {
    private String userName;
    private String fromDate;
    private String toDate;
    private MetaData metaData;

    public PickingDeliverHeaderRequestBodyModel(String userName, String fromDate, String toDate, MetaData metaData) {
        this.userName = userName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.metaData = metaData;
    }

    public PickingDeliverHeaderRequestBodyModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return "PickingHeaderRequestBodyModel{" +
                "userName='" + userName + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
