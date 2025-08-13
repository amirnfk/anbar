package com.oshanak.mobilemarket.Activity.Models;

public class NotificationRequestBodyModel {
    metaData metaData;

    public NotificationRequestBodyModel(com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.metaData = metaData;
    }

    public com.oshanak.mobilemarket.Activity.Models.metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(com.oshanak.mobilemarket.Activity.Models.metaData metaData) {
        this.metaData = metaData;
    }
}
