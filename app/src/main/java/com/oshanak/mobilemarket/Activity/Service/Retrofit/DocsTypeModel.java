package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.Models.DocsTypeModel_Id_name;

import java.util.ArrayList;

public class DocsTypeModel {
    public boolean isSuccessful ;
    public String message ;
    public ArrayList<DocsTypeModel_Id_name> docList ;
    public String tag ;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DocsTypeModel_Id_name> getDocList() {
        return docList;
    }

    public void setDocList(ArrayList<DocsTypeModel_Id_name> docList) {
        this.docList = docList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DocsTypeModel(boolean isSuccessful, String message, ArrayList<DocsTypeModel_Id_name> docList, String tag) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.docList = docList;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "DocsTypeModel{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", docList=" + docList +
                ", tag='" + tag + '\'' +
                '}';
    }
}


