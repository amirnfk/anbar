package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class GarbageProductDetailData implements Serializable
{
    private int ID;//0
    private int HeaderID;//1
    private int ItemID;//2
    private double Amount;//3
    private String ItemName;//4
    private String PartUnit;//5
	private String WholeUnit;//6
    private int BoxQuantity;//7
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

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getPartUnit() {
        return PartUnit;
    }

    public void setPartUnit(String partUnit) {
        PartUnit = partUnit;
    }

    public String getWholeUnit() {
        return WholeUnit;
    }

    public void setWholeUnit(String wholeUnit) {
        WholeUnit = wholeUnit;
    }

    public int getBoxQuantity() {
        return BoxQuantity;
    }

    public void setBoxQuantity(int boxQuantity) {
        BoxQuantity = boxQuantity;
    }
}
