package com.oshanak.mobilemarket.Activity.Models;

public class DocModel {
    String Title;
    String Comment;
    String DocumentType;
    String StoreID;

    metaData metaData;

    public DocModel(String title, String comment, String documentType, String storeID, com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        Title = title;
        Comment = comment;
        DocumentType = documentType;
        StoreID = storeID;

        this.metaData = metaData;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
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



    public com.oshanak.mobilemarket.Activity.Models.metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "DocModel{" +
                "Title='" + Title + '\'' +
                ", Comment='" + Comment + '\'' +
                ", DocumentType='" + DocumentType + '\'' +
                ", StoreID='" + StoreID + '\'' +

                ", metaData=" + metaData +
                '}';
    }
}
