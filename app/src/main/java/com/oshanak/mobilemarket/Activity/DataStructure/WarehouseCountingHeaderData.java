package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehouseCountingHeaderData implements Serializable
{
    private int ID;//0
    private String InsertDate;//1
    private int StatusID;//2
    private int LineNumber;//3
    private String UserName;//4
    ///////////////////

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

    public int getLineNumber() {
        return LineNumber;
    }

    public void setLineNumber(int lineNumber) {
        LineNumber = lineNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}


