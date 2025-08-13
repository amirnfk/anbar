package com.oshanak.mobilemarket.Activity.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class ItemsListRequestModel {
    String Name;
    metaData metaData;

    public ItemsListRequestModel(String name, metaData metaData) {
        Name = name;
        this.metaData = metaData;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(metaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "ItemsListRequestModel{" +
                "Name='" + Name + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
