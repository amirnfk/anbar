package com.oshanak.mobilemarket.Activity.DataStructure;

public class WarehousingItemUMdata
{
    private int ID;//0
    private int ItemID;//1
    private String UMID;//2
    private int MultipleConvert;//3
    ////////////////////

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

    public String getUMID() {
        return UMID;
    }

    public void setUMID(String UMID) {
        this.UMID = UMID;
    }

    public int getMultipleConvert() {
        return MultipleConvert;
    }

    public void setMultipleConvert(int multipleConvert) {
        MultipleConvert = multipleConvert;
    }
}
