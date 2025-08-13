package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdatePickingItemStatusRequestModel {

    int ItemID;

    int StatusID;

    MetaData metaData;


    public UpdatePickingItemStatusRequestModel(int itemID, int statusID, MetaData metaData) {
        ItemID = itemID;
        StatusID = statusID;
        this.metaData = metaData;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    @Override
    public String toString() {
        return "UpdatePickingItemStatusRequestModel{" +
                "ItemID='" + ItemID + '\'' +
                ", StatusID='" + StatusID + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
