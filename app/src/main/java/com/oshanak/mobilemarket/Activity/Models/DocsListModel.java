package com.oshanak.mobilemarket.Activity.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class DocsListModel implements Serializable {

    String isSuccessful;
    String message;
    ArrayList <DocModelResponse> list;

    public DocsListModel(String isSuccessful, String message, ArrayList<DocModelResponse> list) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.list = list;
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DocModelResponse> getList() {
        return list;
    }

    public void setList(ArrayList<DocModelResponse> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DocsListModel{" +
                "isSuccessful='" + isSuccessful + '\'' +
                ", message='" + message + '\'' +
                ", list=" + list +
                '}';
    }
}
