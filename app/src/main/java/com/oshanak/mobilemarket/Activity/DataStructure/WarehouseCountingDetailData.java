package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehouseCountingDetailData implements Serializable
{
    private int ID;//0
    private int HeaderID;//1
    private int ItemID;//2
    private String ItemName;//3
    private double Amount;//4
    ////////////////

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int headerID) {
        HeaderID = headerID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
