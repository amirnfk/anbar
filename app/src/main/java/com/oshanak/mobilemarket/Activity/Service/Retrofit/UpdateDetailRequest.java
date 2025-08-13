package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class UpdateDetailRequest {
    String inboundDetailID;
    int userCount;
    String userMeins;
    MetaData metaData;

    public UpdateDetailRequest(String inboundDetailID, int userCount, String userMeins, MetaData metaData) {
        this.inboundDetailID = inboundDetailID;
        this.userCount = userCount;
        this.userMeins = userMeins;
        this.metaData = metaData;
    }

    public String getInboundDetailID() {
        return inboundDetailID;
    }

    public void setInboundDetailID(String inboundDetailID) {
        this.inboundDetailID = inboundDetailID;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getUserMeins() {
        return userMeins;
    }

    public void setUserMeins(String userMeins) {
        this.userMeins = userMeins;
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
                "inboundDetailID='" + inboundDetailID + '\'' +
                ", userCount=" + userCount +
                ", userMeins='" + userMeins + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
