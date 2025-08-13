package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehousingHeaderData implements Serializable
{
    private int Code = 0;//0
    private String ID;//1
    private int RetailStoreID;//2
    private int CountingStatusID;//3
    private String CountingStatusName;//4
    private String CreateDate;//5
    private int StatusID;//6
    private String HandheldIP;//7
    private String HandheldID;//8
    /////////////////

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getRetailStoreID() {
        return RetailStoreID;
    }

    public void setRetailStoreID(int retailStoreID) {
        RetailStoreID = retailStoreID;
    }

    public int getCountingStatusID() {
        return CountingStatusID;
    }

    public void setCountingStatusID(int countingStatusID) {
        CountingStatusID = countingStatusID;
    }

    public String getCountingStatusName() {
        return CountingStatusName;
    }

    public void setCountingStatusName(String countingStatusName) {
        CountingStatusName = countingStatusName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getHandheldIP() {
        return HandheldIP;
    }

    public void setHandheldIP(String handheldIP) {
        HandheldIP = handheldIP;
    }

    public String getHandheldID() {
        return HandheldID;
    }

    public void setHandheldID(String handheldID) {
        HandheldID = handheldID;
    }
}
