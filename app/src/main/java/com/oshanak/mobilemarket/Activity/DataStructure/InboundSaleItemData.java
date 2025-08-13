package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class InboundSaleItemData implements Serializable
{
    private int ID = 0;//0
    private int RetailStoreId = 0;//1
    private int ItemId = 0;//2
    private String ItemName = "";//3
    private String Barcode = "";//4
    private String Meins = "";//5
    private int UMREZ = 0;//6
    private String Translate_Meinh = "";//7
    ////////////////////

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(int retailStoreId) {
        RetailStoreId = retailStoreId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getMeins() {
        return Meins;
    }

    public void setMeins(String meins) {
        Meins = meins;
    }

    public int getUMREZ() {
        return UMREZ;
    }

    public void setUMREZ(int UMREZ) {
        this.UMREZ = UMREZ;
    }

    public String getTranslate_Meinh() {
        return Translate_Meinh;
    }

    public void setTranslate_Meinh(String translate_Meinh) {
        Translate_Meinh = translate_Meinh;
    }
}
