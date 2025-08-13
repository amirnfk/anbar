package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class WarehousingDetailData implements Serializable
{
    private int ID;//0
    private int ItemID;//1
    private int HeaderCode;//2
    private String ItemName;//3
    private int CountValue;//4
    private int Location;//5
    private String CreateDate;//6
    private boolean CountingDone;//7
    private String PartUnit;//8
    private String WholeUnit;//9
    private int MultipleConvert;//10
    ////////////////////////////////

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

    public int getHeaderCode() {
        return HeaderCode;
    }

    public void setHeaderCode(int headerCode) {
        HeaderCode = headerCode;
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

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public boolean isCountingDone() {
        return CountingDone;
    }

    public void setCountingDone(boolean countingDone) {
        CountingDone = countingDone;
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

    public int getMultipleConvert() {
        return MultipleConvert;
    }

    public void setMultipleConvert(int multipleConvert) {
        MultipleConvert = multipleConvert;
    }
}
