package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class InboundDetailRequest {
    private String InboundHeaderID;
    private MetaData metaData;

    // Constructor, getters, and setters
    public InboundDetailRequest(String inboundHeaderID, MetaData metaData) {
        InboundHeaderID = inboundHeaderID;
        this.metaData = metaData;
    }

    public String getInboundHeaderID() {
        return InboundHeaderID;
    }

    public void setInboundHeaderID(String inboundHeaderID) {
        InboundHeaderID = inboundHeaderID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}