package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class PickingDeliverItemData implements  Serializable
{
    private int ID;
    private int HeaderID;
    private String MATNR;
    private String LGPBE;
    private String ARKTX;
    private int UMVKZ;
    private int UMVKN;
    private double LFIMG;
    private String VRKME;
    private int LBKUM;
    private int DeliverAmount;
    private int StatusID;
    private String StatusName;
    private String DeliverUnit;
    private String PartUnit;
    private String WholeUnit;
    private String BARCODE;
    private boolean FinalControlled;
    private String RPMKR;
    private boolean Is_PC_Unit;

    public PickingDeliverItemData() {
    }

    public PickingDeliverItemData(int ID, int headerID, String MATNR, String LGPBE, String ARKTX, int UMVKZ, int UMVKN, int LFIMG, String VRKME, int LBKUM, int deliverAmount, int statusID, String statusName, String deliverUnit, String partUnit, String wholeUnit, String BARCODE, boolean finalControlled, String RPMKR, boolean is_PC_Unit) {
        this.ID = ID;
        HeaderID = headerID;
        this.MATNR = MATNR;
        this.LGPBE = LGPBE;
        this.ARKTX = ARKTX;
        this.UMVKZ = UMVKZ;
        this.UMVKN = UMVKN;
        this.LFIMG = LFIMG;
        this.VRKME = VRKME;
        this.LBKUM = LBKUM;
        DeliverAmount = deliverAmount;
        StatusID = statusID;
        StatusName = statusName;
        DeliverUnit = deliverUnit;
        PartUnit = partUnit;
        WholeUnit = wholeUnit;
        this.BARCODE = BARCODE;
        FinalControlled = finalControlled;
        this.RPMKR = RPMKR;
        Is_PC_Unit = is_PC_Unit;
    }

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

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getLGPBE() {
        return LGPBE;
    }

    public void setLGPBE(String LGPBE) {
        this.LGPBE = LGPBE;
    }

    public String getARKTX() {
        return ARKTX;
    }

    public void setARKTX(String ARKTX) {
        this.ARKTX = ARKTX;
    }

    public int getUMVKZ() {
        return UMVKZ;
    }

    public void setUMVKZ(int UMVKZ) {
        this.UMVKZ = UMVKZ;
    }

    public int getUMVKN() {
        return UMVKN;
    }

    public void setUMVKN(int UMVKN) {
        this.UMVKN = UMVKN;
    }

    public double getLFIMG() {
        return LFIMG;
    }

    public void setLFIMG(int LFIMG) {
        this.LFIMG = LFIMG;
    }

    public String getVRKME() {
        return VRKME;
    }

    public void setVRKME(String VRKME) {
        this.VRKME = VRKME;
    }

    public int getLBKUM() {
        return LBKUM;
    }

    public void setLBKUM(int LBKUM) {
        this.LBKUM = LBKUM;
    }

    public int getDeliverAmount() {
        return DeliverAmount;
    }

    public void setDeliverAmount(int deliverAmount) {
        DeliverAmount = deliverAmount;
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

    public String getDeliverUnit() {
        return DeliverUnit;
    }

    public void setDeliverUnit(String deliverUnit) {
        DeliverUnit = deliverUnit;
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

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public boolean isFinalControlled() {
        return FinalControlled;
    }

    public void setFinalControlled(boolean finalControlled) {
        FinalControlled = finalControlled;
    }

    public String getRPMKR() {
        return RPMKR;
    }

    public void setRPMKR(String RPMKR) {
        this.RPMKR = RPMKR;
    }

    public boolean isIs_PC_Unit() {
        return Is_PC_Unit;
    }

    public void setIs_PC_Unit(boolean is_PC_Unit) {
        Is_PC_Unit = is_PC_Unit;
    }

    @Override
    public String toString() {
        return "PickingDeliverItemData{" +
                "ID=" + ID +
                ", HeaderID=" + HeaderID +
                ", MATNR='" + MATNR + '\'' +
                ", LGPBE='" + LGPBE + '\'' +
                ", ARKTX='" + ARKTX + '\'' +
                ", UMVKZ=" + UMVKZ +
                ", UMVKN=" + UMVKN +
                ", LFIMG=" + LFIMG +
                ", VRKME='" + VRKME + '\'' +
                ", LBKUM=" + LBKUM +
                ", DeliverAmount=" + DeliverAmount +
                ", StatusID=" + StatusID +
                ", StatusName='" + StatusName + '\'' +
                ", DeliverUnit='" + DeliverUnit + '\'' +
                ", PartUnit='" + PartUnit + '\'' +
                ", WholeUnit='" + WholeUnit + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                ", FinalControlled=" + FinalControlled +
                ", RPMKR='" + RPMKR + '\'' +
                ", Is_PC_Unit=" + Is_PC_Unit +
                '}';
    }
}
