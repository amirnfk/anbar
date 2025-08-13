package com.oshanak.mobilemarket.Activity.PushNotification;

public class NotificationDto {
    private Long id;
    private String title;
    private String body;
    private String imageUrl;
    private String voiceUrl;
    private int type;
    private String typeName;
    private Long orderTypeId;
    private String orderTypeTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTypeTitle() {
        return orderTypeTitle;
    }

    public void setOrderTypeTitle(String orderTypeTitle) {
        this.orderTypeTitle = orderTypeTitle;
    }

    @Override
    public String toString() {
        return "NotificationDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", orderTypeId=" + orderTypeId +
                ", orderTypeTitle='" + orderTypeTitle + '\'' +
                '}';
    }
}
