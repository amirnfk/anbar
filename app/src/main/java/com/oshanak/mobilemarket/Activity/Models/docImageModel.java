package com.oshanak.mobilemarket.Activity.Models;

public class docImageModel {
    int DetailID;
    String ImageURL;

    public docImageModel(int detailID, String imageURL) {
        DetailID = detailID;
        ImageURL = imageURL;
    }

    public int getDetailID() {
        return DetailID;
    }

    public void setDetailID(int detailID) {
        DetailID = detailID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
