package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class ProductControlData implements Serializable
{
    private String ProductCode = "";//0
    private String ProductName = "";//1
    private String LatestSaleDate = "";//2
    private String LatestReceiptDate = "";//3
    private String Promotion = "";//4
    private String Provider = "";//5
    private String SalePrice = "";//6
    private String Price = "";//7
    private int SoldCount = 0;//8
    //////////////////////


    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getLatestSaleDate() {
        return LatestSaleDate;
    }

    public void setLatestSaleDate(String latestSaleDate) {
        LatestSaleDate = latestSaleDate;
    }

    public String getLatestReceiptDate() {
        return LatestReceiptDate;
    }

    public void setLatestReceiptDate(String latestReceiptDate) {
        LatestReceiptDate = latestReceiptDate;
    }

    public String getPromotion() {
        return Promotion;
    }

    public void setPromotion(String promotion) {
        Promotion = promotion;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getSoldCount() {
        return SoldCount;
    }

    public void setSoldCount(int soldCount) {
        SoldCount = soldCount;
    }
}
