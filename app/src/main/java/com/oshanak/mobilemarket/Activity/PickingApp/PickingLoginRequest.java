package com.oshanak.mobilemarket.Activity.PickingApp;


import com.google.gson.annotations.SerializedName;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class PickingLoginRequest {
    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("metaData")
    private MetaData metaData;

    public PickingLoginRequest(String userName, String password, MetaData metaData) {
        this.userName = userName;
        this.password = password;
        this.metaData = metaData;
    }

    // Getters and setters
}


