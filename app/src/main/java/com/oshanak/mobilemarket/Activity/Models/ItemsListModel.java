package com.oshanak.mobilemarket.Activity.Models;

import java.util.ArrayList;

public class ItemsListModel {
    boolean isSuccessful;
    String message;
    ArrayList <ItemModel> allItemDataList;

    public ItemsListModel(boolean isSuccessful, String message, ArrayList<ItemModel> allItemDataList) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.allItemDataList = allItemDataList;
    }

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

    public ArrayList<ItemModel> getAllItemDataList() {
        return allItemDataList;
    }

    public void setAllItemDataList(ArrayList<ItemModel> allItemDataList) {
        this.allItemDataList = allItemDataList;
    }
}
