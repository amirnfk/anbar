package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdatePickingLineStatusRequest {

    private String LineID;
    private String StatusID;
    private MetaData metaData;

    public UpdatePickingLineStatusRequest(String lineID, String statusID, MetaData metaData) {
        LineID = lineID;
        StatusID = statusID;
        this.metaData = metaData;
    }

    public String getLineID() {
        return LineID;
    }

    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "UpdatePickingLineStatusRequest{" +
                "LineID='" + LineID + '\'' +
                ", StatusID='" + StatusID + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
