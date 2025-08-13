package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdatePickingRequest {
    private String ItemID;
    private String deliverAmount;
    private String deliverUnit;
    private MetaData metaData;

    public UpdatePickingRequest(String itemID, String deliverAmount, String deliverUnit, MetaData metaData) {
        ItemID = itemID;
        this.deliverAmount = deliverAmount;
        this.deliverUnit = deliverUnit;
        this.metaData = metaData;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getDeliverAmount() {
        return deliverAmount;
    }

    public void setDeliverAmount(String deliverAmount) {
        this.deliverAmount = deliverAmount;
    }

    public String getDeliverUnit() {
        return deliverUnit;
    }

    public void setDeliverUnit(String deliverUnit) {
        this.deliverUnit = deliverUnit;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "UpdatePickingRequest{" +
                "ItemID='" + ItemID + '\'' +
                ", deliverAmount='" + deliverAmount + '\'' +
                ", deliverUnit='" + deliverUnit + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
