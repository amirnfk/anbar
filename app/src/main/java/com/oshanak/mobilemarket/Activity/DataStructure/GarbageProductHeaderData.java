package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class GarbageProductHeaderData implements Serializable
{
    private int ID;//0
    private String InsertDate;//1
    private int StatusID;//2
    private int StoreID;//3
    ///////////////

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }
}
