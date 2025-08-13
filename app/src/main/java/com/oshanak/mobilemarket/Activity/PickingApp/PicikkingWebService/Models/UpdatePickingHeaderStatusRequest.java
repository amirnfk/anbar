package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdatePickingHeaderStatusRequest {
    private String HeaderID;
    private String StatusID;
    private String PalletCount;
    private MetaData metaData;


    public UpdatePickingHeaderStatusRequest(String headerID, String statusID, String palletCount ,MetaData metaData) {
        HeaderID = headerID;
        StatusID = statusID;
        PalletCount = palletCount;
        this.metaData = metaData;
    }





    public String getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(String headerID) {
        HeaderID = headerID;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public String getPalletCount() {
        return PalletCount;
    }

    public void setPalletCount(String palletCount) {
        PalletCount = palletCount;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "UpdatePickingHeaderStatusRequest{" +
                "HeaderID='" + HeaderID + '\'' +
                ", StatusID='" + StatusID + '\'' +
                ", PalletCount='" + PalletCount + '\'' +
                ", metaData=" + metaData +

                '}';
    }
}
