package com.oshanak.mobilemarket.Activity.Models;

public class NotificationPreviewRequest {
    private int detailId;
    private int storeId;
    private String reciveDate;


    public NotificationPreviewRequest(int detailId, int storeId, String reciveDate ) {
        this.detailId = detailId;
        this.storeId = storeId;
        this.reciveDate = reciveDate;

    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getReciveDate() {
        return reciveDate;
    }

    public void setReciveDate(String reciveDate) {
        this.reciveDate = reciveDate;
    }



    @Override
    public String toString() {
        return "NotificationPreviewRequest{" +
                "detailId=" + detailId +
                ", storeId=" + storeId +
                ", reciveDate='" + reciveDate + '\'' +

                '}';
    }
}
