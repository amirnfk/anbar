package com.oshanak.mobilemarket.Activity.Models;

public class GetDocListModel {
    String Title;
    String DocumentType;
    String StoreID;
    String fromDate;
    String toDate;
    metaData metaData;

    public GetDocListModel(String title, String documentType, String storeID, String fromDate, String toDate, com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        Title = title;
        DocumentType = documentType;
        StoreID = storeID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.metaData = metaData;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public com.oshanak.mobilemarket.Activity.Models.metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "GetDocListModel{" +
                "Title='" + Title + '\'' +
                ", DocumentType='" + DocumentType + '\'' +
                ", StoreID='" + StoreID + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
