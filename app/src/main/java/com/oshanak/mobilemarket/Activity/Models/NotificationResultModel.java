package com.oshanak.mobilemarket.Activity.Models;

public class NotificationResultModel {
    GetNewOrderNotificationResult GetNewOrderNotificationResultObject;

    public NotificationResultModel(GetNewOrderNotificationResult getNewOrderNotificationResultObject) {
        GetNewOrderNotificationResultObject = getNewOrderNotificationResultObject;
    }
// Getter Methods

    public GetNewOrderNotificationResult getGetNewOrderNotificationResult() {
        return GetNewOrderNotificationResultObject;
    }

    // Setter Methods

    public void setGetNewOrderNotificationResult(GetNewOrderNotificationResult GetNewOrderNotificationResultObject) {
        this.GetNewOrderNotificationResultObject = GetNewOrderNotificationResultObject;
    }
}
