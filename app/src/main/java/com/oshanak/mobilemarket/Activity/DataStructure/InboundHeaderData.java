package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InboundHeaderData implements Serializable
{
    public int ID = 0;//0
    public String InboundId = "";//1
    public int RetailStoreId = 0;//2
    //////////////////////
    public String OrderType = "";//1
    public String DATEN_S ;//1
    public String DATEN_M ;//1
    public String WBSTK ;//1
    public String LOGGR ;//1
    public int ItemCount;

    public String LastUpdateUser;
    public String  LastUpdateUserName;
    public String  LastUpdateDate;
    public int SendToSapStatus;
    public int getItemCount() {
        return ItemCount;
    }

    public int getSendToSapStatus() {
        return SendToSapStatus;
    }

    public void setSendToSapStatus(int sendToSapStatus) {
        SendToSapStatus = sendToSapStatus;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public Long getDATEN_M() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long timestamp= Long.valueOf(0);
        try {
            Date date = dateFormat.parse(DATEN_M);
             timestamp = date.getTime();
            // Use the timestamp as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  timestamp ;
    }
    public Long getDATEN_M_From() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Long timestamp= Long.valueOf(0);
        try {
            Date date = dateFormat.parse(DATEN_M+"-00-00-00");
            timestamp = date.getTime();
            // Use the timestamp as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  timestamp ;
    }
    public Long getDATEN_M_To() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Long timestamp= Long.valueOf(0);
        try {
            Date date = dateFormat.parse(DATEN_M+"-11-59-59");
            timestamp = date.getTime();
            // Use the timestamp as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  timestamp ;
    }
    public void setDATEN_M(String DATEN_M) {

        this.DATEN_M = DATEN_M;
    }

    public String getLastUpdateUser() {
        return LastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        LastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdateUserName() {
        return LastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        LastUpdateUserName = lastUpdateUserName;
    }

    public String getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }

    public String getDATEN_S() {
        return DATEN_S;
    }

    public void setDATEN_S(String DATEN_S) {
        this.DATEN_S = DATEN_S;
    }

    public String getWBSTK() {
        return WBSTK;
    }

    public void setWBSTK(String WBSTK) {
        this.WBSTK = WBSTK;
    }

    public String getLOGGR() {
        return LOGGR;
    }

    public void setLOGGR(String LOGGR) {
        this.LOGGR = LOGGR;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getInboundId() {
        return InboundId;
    }

    public void setInboundId(String inboundId) {
        InboundId = inboundId;
    }

    public int getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(int retailStoreId) {
        RetailStoreId = retailStoreId;
    }

    public InboundHeaderData(int ID, String inboundId, int retailStoreId, String orderType, String DATEN_S, String DATEN_M, String WBSTK, String LOGGR, int itemCount, String lastUpdateUser, String lastUpdateUserName, String lastUpdateDate, int sendToSapStatus) {
        this.ID = ID;
        InboundId = inboundId;
        RetailStoreId = retailStoreId;
        OrderType = orderType;
        this.DATEN_S = DATEN_S;
        this.DATEN_M = DATEN_M;
        this.WBSTK = WBSTK;
        this.LOGGR = LOGGR;
        ItemCount = itemCount;
        LastUpdateUser = lastUpdateUser;
        LastUpdateUserName = lastUpdateUserName;
        LastUpdateDate = lastUpdateDate;
        SendToSapStatus = sendToSapStatus;
    }

    @Override
    public String toString() {
        return "InboundHeaderData{" +
                "ID=" + ID +
                ", InboundId='" + InboundId + '\'' +
                ", RetailStoreId=" + RetailStoreId +
                ", OrderType='" + OrderType + '\'' +
                ", DATEN_S='" + DATEN_S + '\'' +
                ", DATEN_M='" + DATEN_M + '\'' +
                ", WBSTK='" + WBSTK + '\'' +
                ", LOGGR='" + LOGGR + '\'' +
                ", ItemCount=" + ItemCount +
                ", LastUpdateUser='" + LastUpdateUser + '\'' +
                ", LastUpdateUserName='" + LastUpdateUserName + '\'' +
                ", LastUpdateDate='" + LastUpdateDate + '\'' +
                ", SendToSapStatus=" + SendToSapStatus +
                '}';
    }
}
