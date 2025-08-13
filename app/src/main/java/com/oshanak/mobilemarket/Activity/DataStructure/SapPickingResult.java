package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class SapPickingResult implements Serializable
{
    public boolean Error = false;
    public boolean Success = false;
    public boolean ItemRejectedByUser = false;
    public boolean ItemDeletedDeficit = false;
    public boolean ItemNotExistInSAP = false;
    public String Message = "";
    public boolean ItemInventoryAmountNotEnough;
    public int ItemMinInventory;
    public String ItemID;

    ////////////

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public boolean isItemInventoryAmountNotEnough() {
        return ItemInventoryAmountNotEnough;
    }

    public void setItemInventoryAmountNotEnough(boolean itemInventoryAmountNotEnough) {
        ItemInventoryAmountNotEnough = itemInventoryAmountNotEnough;
    }

    public int getItemMinInventory() {
        return ItemMinInventory;
    }

    public void setItemMinInventory(int itemMinInventory) {
        ItemMinInventory = itemMinInventory;
    }

    public boolean isError() {
        return Error;
    }

    public void setError(boolean error) {
        Error = error;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public boolean isItemRejectedByUser() {
        return ItemRejectedByUser;
    }

    public void setItemRejectedByUser(boolean itemRejectedByUser) {
        ItemRejectedByUser = itemRejectedByUser;
    }

    public boolean isItemDeletedDeficit() {
        return ItemDeletedDeficit;
    }

    public void setItemDeletedDeficit(boolean itemDeletedDeficit) {
        ItemDeletedDeficit = itemDeletedDeficit;
    }

    public boolean isItemNotExistInSAP() {
        return ItemNotExistInSAP;
    }

    public void setItemNotExistInSAP(boolean itemNotExistInSAP) {
        ItemNotExistInSAP = itemNotExistInSAP;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "SapPickingResult{" +
                "Error=" + Error +
                ", Success=" + Success +
                ", ItemRejectedByUser=" + ItemRejectedByUser +
                ", ItemDeletedDeficit=" + ItemDeletedDeficit +
                ", ItemNotExistInSAP=" + ItemNotExistInSAP +
                ", Message='" + Message + '\'' +
                ", ItemInventoryAmountNotEnough=" + ItemInventoryAmountNotEnough +
                ", ItemMinInventory=" + ItemMinInventory +
                ", ItemID='" + ItemID + '\'' +
                '}';
    }
}


