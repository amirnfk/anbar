package com.oshanak.mobilemarket.Activity.Models;

public class ItemModel {
    int ItemId;
    String Name;
    String PurchareOrderUMId;
    String UmId;
    String Barcode;

    public ItemModel(int itemId, String name, String purchareOrderUMId, String umId, String barcode) {
        ItemId = itemId;
        Name = name;
        PurchareOrderUMId = purchareOrderUMId;
        UmId = umId;
        Barcode = barcode;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPurchareOrderUMId() {
        return PurchareOrderUMId;
    }

    public void setPurchareOrderUMId(String purchareOrderUMId) {
        PurchareOrderUMId = purchareOrderUMId;
    }

    public String getUmId() {
        return UmId;
    }

    public void setUmId(String umId) {
        UmId = umId;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }
}
