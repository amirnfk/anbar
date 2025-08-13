package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdateStatusDetailRequest {
    private int inboundDetailID;
    private int statusID;
    private MetaData metaData;

    public UpdateStatusDetailRequest(int inboundDetailID, int statusID, MetaData metaData) {
        this.inboundDetailID = inboundDetailID;
        this.statusID = statusID;
        this.metaData = metaData;
    }


    public int getInboundDetailID() {
        return inboundDetailID;
    }

    public void setInboundDetailID(int inboundDetailID) {
        this.inboundDetailID = inboundDetailID;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "UpdateDetailRequest{" +
                "InboundHeaderID='" + inboundDetailID + '\'' +
                ", statusID='" + statusID + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
