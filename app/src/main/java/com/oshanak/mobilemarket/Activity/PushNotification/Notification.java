package com.oshanak.mobilemarket.Activity.PushNotification;

// Notification.java
public class Notification {
    private int id;
    private String title;
    private String body;

    private String persianStartDate;
    private String startDate;
    private String imageUrl;
    private String voiceUrl;
    private String typeName;
    private int type;
    private int orderTypeId;
    private boolean isSeen;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public String getTypeName() {
        return typeName;
    }

    public String getPersianStartDate() {
        return persianStartDate;
    }

    public void setPersianStartDate(String persianCreateDate) {
        this.persianStartDate = persianCreateDate;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", persianStartDate='" + persianStartDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", typeName='" + typeName + '\'' +
                ", type=" + type +
                ", orderTypeId=" + orderTypeId +
                ", isSeen=" + isSeen +
                '}';
    }
}