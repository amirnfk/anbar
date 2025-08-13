package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehouseOrderData implements Serializable
{
    private int ID;//0
    private int StoreID;//1
    private int ItemID;//2
    private int Amount;//3
    private String SelectedUnit;//4
    private String ItemName;//5
    private String PartUnit;//6
    private String WholeUnit;//7
    private int BoxQuantity;//8
    private int StatusID;//9
    /////////////////////////////

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getSelectedUnit() {
        return SelectedUnit;
    }

    public void setSelectedUnit(String selectedUnit) {
        SelectedUnit = selectedUnit;
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

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }
}
