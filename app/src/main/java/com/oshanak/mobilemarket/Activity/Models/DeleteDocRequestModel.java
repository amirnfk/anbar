package com.oshanak.mobilemarket.Activity.Models;

public class DeleteDocRequestModel {
    int ID;
    metaData metaData;

    public DeleteDocRequestModel(int ID, com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.ID = ID;
        this.metaData = metaData;
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

    @Override
    public String toString() {
        return "DeleteDocRequestModel{" +
                "ID=" + ID +
                ", metaData=" + metaData +
                '}';
    }
}
