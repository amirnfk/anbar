package com.oshanak.mobilemarket.Activity.Service;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class OpenInboundRequestModel {
    private String storeID;
    private MetaData metaData;
    private int SendToSap;

    public OpenInboundRequestModel() {
    }

    public OpenInboundRequestModel(String storeID, MetaData metaData, int sendToSap) {
        this.storeID = storeID;
        this.metaData = metaData;
        SendToSap = sendToSap;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public int getSendToSap() {
        return SendToSap;
    }

    public void setSendToSap(int sendToSap) {
        SendToSap = sendToSap;
    }

    @Override
    public String toString() {
        return "OpenInboundRequestModel{" +
                "storeID='" + storeID + '\'' +
                ", metaData=" + metaData +
                ", SendToSap=" + SendToSap +
                '}';
    }
}
