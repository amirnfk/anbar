package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class InboundDetailData implements Serializable
{
    private int ID = 0;//0
    private int InboundHeaderID = 0;//1
    private int InboundItemID = 0;//2
    private int ItemId = 0;//3
    private double UserCount = 0;//4
    private String UserMeins = "";//5
    private String ItemName = "";//6
    private int UMREZ = 0;//7
    private String Meins = "";//8
    private String Translate_Meinh = "";//9
    //////////////////

    private int StatusID;
    private String StatusName;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getInboundHeaderID() {
        return InboundHeaderID;
    }

    public void setInboundHeaderID(int inboundHeaderID) {
        InboundHeaderID = inboundHeaderID;
    }

    public int getInboundItemID() {
        return InboundItemID;
    }

    public void setInboundItemID(int inboundItemID) {
        InboundItemID = inboundItemID;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public double getUserCount() {
        return UserCount;
    }

    public void setUserCount(double userCount) {
        UserCount = userCount;
    }

    public String getUserMeins() {
        return UserMeins;
    }

    public void setUserMeins(String userMeins) {
        UserMeins = userMeins;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getUMREZ() {
        return UMREZ;
    }

    public void setUMREZ(int UMREZ) {
        this.UMREZ = UMREZ;
    }

    public String getMeins() {
        return Meins;
    }

    public void setMeins(String meins) {
        Meins = meins;
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

    public String getTranslate_Meinh() {
        return Translate_Meinh;
    }

    public void setTranslate_Meinh(String translate_Meinh) {
        Translate_Meinh = translate_Meinh;
    }

    @Override
    public String toString() {
        return "InboundDetailData{" +
                "ID=" + ID +
                ", InboundHeaderID=" + InboundHeaderID +
                ", InboundItemID=" + InboundItemID +
                ", ItemId=" + ItemId +
                ", UserCount=" + UserCount +
                ", UserMeins='" + UserMeins + '\'' +
                ", ItemName='" + ItemName + '\'' +
                ", UMREZ=" + UMREZ +
                ", Meins='" + Meins + '\'' +
                ", Translate_Meinh='" + Translate_Meinh + '\'' +
                ", StatusID=" + StatusID +
                ", StatusName='" + StatusName + '\'' +
                '}';
    }
}
