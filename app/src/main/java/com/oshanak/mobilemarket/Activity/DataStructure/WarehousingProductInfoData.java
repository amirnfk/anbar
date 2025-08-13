package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehousingProductInfoData implements Serializable
{
    private int ID;//0
    private int ItemID;//1
    private String ItemName;//2
    private int CountValue;//3
    private int Location;//4
    private boolean CountingDone;//5
    //////////////////

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getCountValue() {
        return CountValue;
    }

    public void setCountValue(int countValue) {
        CountValue = countValue;
    }

    public int getLocation() {
        return Location;
    }

    public void setLocation(int location) {
        Location = location;
    }

    public boolean getCountingDone() {
        return CountingDone;
    }

    public void setCountingDone(boolean countingDone) {
        CountingDone = countingDone;
    }
}
