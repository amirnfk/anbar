package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.Models.metaData;

public class LoginBodyModel {
    String userName;
    String password;
    metaData metaData;

    public LoginBodyModel(String userName, String password, metaData metaData) {
        this.userName = userName;
        this.password = password;
        this.metaData = metaData;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public com.oshanak.mobilemarket.Activity.Models.metaData getMetaData() {
        return metaData;
    }

    public void setMetaData(metaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "LoginBodyModel{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}
