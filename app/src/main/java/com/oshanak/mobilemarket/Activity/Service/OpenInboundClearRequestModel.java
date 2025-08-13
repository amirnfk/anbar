package com.oshanak.mobilemarket.Activity.Service;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class OpenInboundClearRequestModel {
    String ID;
    MetaData metaData;

    public OpenInboundClearRequestModel(String ID, MetaData metaData) {
        this.ID = ID;
        this.metaData = metaData;
    }

    public OpenInboundClearRequestModel() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
