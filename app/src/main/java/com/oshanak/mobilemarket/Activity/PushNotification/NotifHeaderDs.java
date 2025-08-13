package com.oshanak.mobilemarket.Activity.PushNotification;

public   class NotifHeaderDs {

    private String title;
    private String body;
    private String imageUrl;
    private String voiceUrl;
    private int Type;


    // سازنده


    public NotifHeaderDs(String title, String body, String imageUrl, String voiceUrl, int type) {

        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
        Type = type;
    }


    // متدهای دسترسی (getters)
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "NotifHeaderDs{" +

                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                ", Type=" + Type +
                '}';
    }
}