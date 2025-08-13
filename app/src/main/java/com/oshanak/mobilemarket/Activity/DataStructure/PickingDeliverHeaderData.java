package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;



public class PickingDeliverHeaderData  implements Serializable
{
    private int ID = 0;//0
    private String VBELN = "";//1
    private String BLDAT = "";//2
    private String WERKS = "";//3
    private String PLANT_NAME = "";//4
    private String KUNNR = "";//5
    private String PEYKAR = "";//6
    private String PEYKAR_NAME = "";//7
    private String UserName = "";//8
    private String CollectorName = "";//8

    private int StatusID = 0;//9
    private String StatusName = "";//10
    private String OrderType = "";//11
    private String Line = "";
    private String StoreID = "";
    private String LineID = "";

    private int EKGRP;

    private String EKGRP_TEXT;

    private String InsertDate;
    private String InsertUser;
    private String UpdateDate;
    private String UpdateUser;




    public PickingDeliverHeaderData(int ID,
                                    String VBELN,
                                    String BLDAT,
                                    String WERKS,
                                    String PLANT_NAME,
                                    String KUNNR,
                                    String PEYKAR,
                                    String PEYKAR_NAME,
                                    String userName,
                                    String collectorName,
                                    int statusID,
                                    String statusName,
                                    String orderType,
                                    String line,
                                    String storeID,
                                    String lineID ,
                                    int EKGRP,
                                    String EKGRP_TEXT ) {
        this.ID = ID;
        this.VBELN = VBELN;
        this.BLDAT = BLDAT;
        this.WERKS = WERKS;
        this.PLANT_NAME = PLANT_NAME;
        this.KUNNR = KUNNR;
        this.PEYKAR = PEYKAR;
        this.PEYKAR_NAME = PEYKAR_NAME;
        UserName = userName;
        CollectorName = collectorName;
        StatusID = statusID;
        StatusName = statusName;
        OrderType = orderType;
        Line = line;
        StoreID = storeID;
        LineID = lineID;
        this.EKGRP=EKGRP;
        this.EKGRP_TEXT=EKGRP_TEXT;
    }

    public PickingDeliverHeaderData(int ID,
                                    String VBELN,
                                    String BLDAT,
                                    String WERKS,
                                    String PLANT_NAME,
                                    String KUNNR,
                                    String PEYKAR,
                                    String PEYKAR_NAME,
                                    String userName,
                                    int statusID,
                                    String statusName,
                                    String orderType,
                                    String line,
                                    int EKGRP,
                                    String EKGRP_TEXT) {
        this.ID = ID;
        this.VBELN = VBELN;
        this.BLDAT = BLDAT;
        this.WERKS = WERKS;
        this.PLANT_NAME = PLANT_NAME;
        this.KUNNR = KUNNR;
        this.PEYKAR = PEYKAR;
        this.PEYKAR_NAME = PEYKAR_NAME;
        UserName = userName;
        StatusID = statusID;
        StatusName = statusName;
        OrderType = orderType;
        Line = line;
        this.EKGRP=EKGRP;
        this.EKGRP_TEXT=EKGRP_TEXT;

    }

    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public String getInsertUser() {
        return InsertUser;
    }

    public void setInsertUser(String insertUser) {
        InsertUser = insertUser;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getUpdateUser() {
        return UpdateUser;
    }

    public void setUpdateUser(String updateUser) {
        UpdateUser = updateUser;
    }

    public int getEKGRP() {
        return EKGRP;
    }

    public void setEKGRP(int EKGRP) {
        this.EKGRP = EKGRP;
    }

    public String getEKGRP_TEXT() {
        return EKGRP_TEXT;
    }

    public void setEKGRP_TEXT(String EKGRP_TEXT) {
        this.EKGRP_TEXT = EKGRP_TEXT;
    }

    public String getLineID() {
        return LineID;
    }

    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public String getCollectorName() {
        return CollectorName;
    }

    public void setCollectorName(String collectorName) {
        CollectorName = collectorName;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public PickingDeliverHeaderData() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN;
    }

    public String getBLDAT() {
        return BLDAT;
    }

    public void setBLDAT(String BLDAT) {
        this.BLDAT = BLDAT;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getPLANT_NAME() {
        return PLANT_NAME;
    }

    public void setPLANT_NAME(String PLANT_NAME) {
        this.PLANT_NAME = PLANT_NAME;
    }

    public String getKUNNR() {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR) {
        this.KUNNR = KUNNR;
    }

    public String getPEYKAR() {
        return PEYKAR;
    }

    public void setPEYKAR(String PEYKAR) {
        this.PEYKAR = PEYKAR;
    }

    public String getPEYKAR_NAME() {
        return PEYKAR_NAME;
    }

    public void setPEYKAR_NAME(String PEYKAR_NAME) {
        this.PEYKAR_NAME = PEYKAR_NAME;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    @Override
    public String toString() {
        return "PickingDeliverHeaderData{" +
                "ID=" + ID +
                ", VBELN='" + VBELN + '\'' +
                ", BLDAT='" + BLDAT + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", PLANT_NAME='" + PLANT_NAME + '\'' +
                ", KUNNR='" + KUNNR + '\'' +
                ", PEYKAR='" + PEYKAR + '\'' +
                ", PEYKAR_NAME='" + PEYKAR_NAME + '\'' +
                ", UserName='" + UserName + '\'' +
                ", CollectorName='" + CollectorName + '\'' +
                ", StatusID=" + StatusID +
                ", StatusName='" + StatusName + '\'' +
                ", OrderType='" + OrderType + '\'' +
                ", Line='" + Line + '\'' +
                ", StoreID='" + StoreID + '\'' +
                ", LineID='" + LineID + '\'' +
                ", EKGRP=" + EKGRP +
                ", EKGRP_TEXT='" + EKGRP_TEXT + '\'' +
                ", InsertDate='" + InsertDate + '\'' +
                ", InsertUser='" + InsertUser + '\'' +
                ", UpdateDate='" + UpdateDate + '\'' +
                ", UpdateUser='" + UpdateUser + '\'' +
                '}';
    }
}
    //    public PickingDeliverHeaderData(){}
//
//    @Override
//    public Object getProperty(int arg0)
//    {
//        switch (arg0)
//        {
//            case 0: return ID;
//            case 1: return VBELN;
//            case 2: return BLDAT;
//            case 3: return WERKS;
//            case 4: return PLANT_NAME;
//            case 5: return KUNNR;
//            case 6: return PEYKAR;
//            case 7: return PEYKAR_NAME;
//            case 8: return UserName;
//            case 9: return StatusID;
//            case 10: return StatusName;
//            case 11: return OrderType;
//        }
//        return null;
//    }
//    @Override
//    public int getPropertyCount()
//    {
//        return 12;
//    }
//    @Override
//    public void setProperty(int arg0, Object value)
//    {
//        switch (arg0)
//        {
//            case 0:
//                ID = Integer.parseInt( value.toString());
//                break;
//            case 1:
//                VBELN = value.toString();
//                break;
//            case 2:
//                BLDAT = value.toString();
//                break;
//            case 3:
//                WERKS = value.toString();
//                break;
//            case 4:
//                PLANT_NAME = value.toString();
//                break;
//            case 5:
//                KUNNR = value.toString();
//                break;
//            case 6:
//                PEYKAR = value.toString();
//                break;
//            case 7:
//                PEYKAR_NAME = value.toString();
//                break;
//            case 8:
//                UserName = value.toString();
//                break;
//            case 9:
//                StatusID = Integer.parseInt( value.toString());
//                break;
//            case 10:
//                StatusName = value.toString();
//                break;
//            case 11:
//                OrderType = value.toString();
//                break;
//        }
//    }
//    @Override
//    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2)
//    {
//        switch (arg0)
//        {
//            case 0:
//                arg2.type = PropertyInfo.INTEGER_CLASS;
//                arg2.name = "ID";
//                break;
//            case 1:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "VBELN";
//                break;
//            case 2:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "BLDAT";
//                break;
//            case 3:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "WERKS";
//                break;
//            case 4:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "PLANT_NAME";
//                break;
//            case 5:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "KUNNR";
//                break;
//            case 6:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "PEYKAR";
//                break;
//            case 7:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "PEYKAR_NAME";
//                break;
//            case 8:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "UserName";
//                break;
//            case 9:
//                arg2.type = PropertyInfo.INTEGER_CLASS;
//                arg2.name = "StatusID";
//                break;
//            case 10:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "StatusName";
//                break;
//            case 11:
//                arg2.type = PropertyInfo.STRING_CLASS;
//                arg2.name = "OrderType";
//                break;
//        }
//    }
//    ///////////////////////////
//
//    public int getID() {
//        return ID;
//    }
//
//    public void setID(int ID) {
//        this.ID = ID;
//    }
//
//    public String getVBELN() {
//        return VBELN;
//    }
//
//    public void setVBELN(String VBELN) {
//        this.VBELN = VBELN;
//    }
//
//    public String getBLDAT() {
//        return BLDAT;
//    }
//
//    public void setBLDAT(String BLDAT) {
//        this.BLDAT = BLDAT;
//    }
//
//    public String getWERKS() {
//        return WERKS;
//    }
//
//    public void setWERKS(String WERKS) {
//        this.WERKS = WERKS;
//    }
//
//    public String getPLANT_NAME() {
//        return PLANT_NAME;
//    }
//
//    public void setPLANT_NAME(String PLANT_NAME) {
//        this.PLANT_NAME = PLANT_NAME;
//    }
//
//    public String getKUNNR() {
//        return KUNNR;
//    }
//
//    public void setKUNNR(String KUNNR) {
//        this.KUNNR = KUNNR;
//    }
//
//    public String getPEYKAR() {
//        return PEYKAR;
//    }
//
//    public void setPEYKAR(String PEYKAR) {
//        this.PEYKAR = PEYKAR;
//    }
//
//    public String getPEYKAR_NAME() {
//        return PEYKAR_NAME;
//    }
//
//    public void setPEYKAR_NAME(String PEYKAR_NAME) {
//        this.PEYKAR_NAME = PEYKAR_NAME;
//    }
//
//    public String getUserName() {
//        return UserName;
//    }
//
//    public void setUserName(String userName) {
//        UserName = userName;
//    }
//
//    public int getStatusID() {
//        return StatusID;
//    }
//
//    public void setStatusID(int statusID) {
//        StatusID = statusID;
//    }
//
//    public String getStatusName() {
//        return StatusName;
//    }
//
//    public void setStatusName(String statusName) {
//        StatusName = statusName;
//    }
//
//    public String getOrderType() {
//        return OrderType;
//    }
//
//    public void setOrderType(String orderType) {
//        OrderType = orderType;
//    }
//
//    @Override
//    public String toString() {
//        return "PickingDeliverHeaderData{" +
//                "ID=" + ID +
//                ", VBELN='" + VBELN + '\'' +
//                ", BLDAT='" + BLDAT + '\'' +
//                ", WERKS='" + WERKS + '\'' +
//                ", PLANT_NAME='" + PLANT_NAME + '\'' +
//                ", KUNNR='" + KUNNR + '\'' +
//                ", PEYKAR='" + PEYKAR + '\'' +
//                ", PEYKAR_NAME='" + PEYKAR_NAME + '\'' +
//                ", UserName='" + UserName + '\'' +
//                ", StatusID=" + StatusID +
//                ", StatusName='" + StatusName + '\'' +
//                ", OrderType='" + OrderType + '\'' +
//                '}';
//    }

