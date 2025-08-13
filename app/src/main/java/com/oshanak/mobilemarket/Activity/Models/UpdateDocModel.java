package com.oshanak.mobilemarket.Activity.Models;

public class UpdateDocModel {
    String Title;
    String Comment;
    int DocumentType;
        int ID;
    metaData metaData;

    public UpdateDocModel(String title, String comment, int documentType, int ID, com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        Title = title;
        Comment = comment;
        DocumentType = documentType;
        this.ID = ID;
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

    public int getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(int documentType) {
        DocumentType = documentType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public com.oshanak.mobilemarket.Activity.Models.metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.metaData = metaData;
    }
}
