package com.oshanak.mobilemarket.Activity.Models;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class DataStructure{
    private String __type;
    private String ItemId;
    private String ItemName;
    private String Unit;
    private int SalesPrice;
    private int PromotionPrice;
    private int MasrafKonandePrice;
    private int SellingPrice;
    private String PromotionFromDate_S;

    private String PromotionToDate_S;
    private String PromotionFromDate_M;

    private String PromotionToDate_M;

    public String getPromotionFromDate_S() {
        return PromotionFromDate_S;
    }

    public void setPromotionFromDate_S(String promotionFromDate_S) {
        PromotionFromDate_S = promotionFromDate_S;
    }

    public String getPromotionToDate_S() {
        return PromotionToDate_S;
    }

    public void setPromotionToDate_S(String promotionToDate_S) {
        PromotionToDate_S = promotionToDate_S;
    }

    public String getPromotionFromDate_M() {
        return PromotionFromDate_M;
    }

    public void setPromotionFromDate_M(String promotionFromDate_M) {
        PromotionFromDate_M = promotionFromDate_M;
    }

    public String getPromotionToDate_M() {
        return PromotionToDate_M;
    }

    public void setPromotionToDate_M(String promotionToDate_M) {
        PromotionToDate_M = promotionToDate_M;
    }

    private String ImageURL;

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        SalesPrice = salesPrice;
    }

    public int getPromotionPrice() {
        return PromotionPrice;
    }

    public void setPromotionPrice(int promotionPrice) {
        PromotionPrice = promotionPrice;
    }

    public int getMasrafKonandePrice() {
        return MasrafKonandePrice;
    }

    public void setMasrafKonandePrice(int masrafKonandePrice) {
        MasrafKonandePrice = masrafKonandePrice;
    }

    public int getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        SellingPrice = sellingPrice;
    }







    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    @Override
    public String toString() {
        return "DataStructure{" +
                "__type='" + __type + '\'' +
                ", ItemId=" + ItemId +
                ", ItemName='" + ItemName + '\'' +
                ", Unit='" + Unit + '\'' +
                ", SalesPrice=" + SalesPrice +
                ", PromotionPrice=" + PromotionPrice +
                ", MasrafKonandePrice=" + MasrafKonandePrice +
                ", SellingPrice=" + SellingPrice +
                ", PromotionFromDate='" + PromotionFromDate_S + '\'' +
                ", PromotionToDate='" + PromotionToDate_S + '\'' +
                ", ImageURL='" + ImageURL + '\'' +
                '}';
    }
}

