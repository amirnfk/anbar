package com.oshanak.mobilemarket.Activity.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class DocModelResponse implements Serializable {
    int HeaderID;
    int StoreID;
    String Title;
    String Comment;
    int DocumentTypeCode;
    String DocumentTypeTitle;
    String InsertDate;
    ArrayList<docImageModel> imageList;

    public DocModelResponse(int headerID, int storeID, String title, String comment, int documentTypeCode, String documentTypeTitle, String insertDate, ArrayList<docImageModel> imageList) {
        HeaderID = headerID;
        StoreID = storeID;
        Title = title;
        Comment = comment;
        DocumentTypeCode = documentTypeCode;
        DocumentTypeTitle = documentTypeTitle;
        InsertDate = insertDate;
        this.imageList = imageList;
    }

    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int headerID) {
        HeaderID = headerID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
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

    public int getDocumentTypeCode() {
        return DocumentTypeCode;
    }

    public void setDocumentTypeCode(int documentTypeCode) {
        DocumentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeTitle() {
        return DocumentTypeTitle;
    }

    public void setDocumentTypeTitle(String documentTypeTitle) {
        DocumentTypeTitle = documentTypeTitle;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public ArrayList<docImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<docImageModel> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "DocModelResponse{" +
                "HeaderID=" + HeaderID +
                ", StoreID=" + StoreID +
                ", Title='" + Title + '\'' +
                ", Comment='" + Comment + '\'' +
                ", DocumentTypeCode=" + DocumentTypeCode +
                ", DocumentTypeTitle='" + DocumentTypeTitle + '\'' +
                ", InsertDate='" + InsertDate + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
