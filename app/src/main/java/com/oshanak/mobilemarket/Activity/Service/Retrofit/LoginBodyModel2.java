package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.Models.metaData;

public class LoginBodyModel2 {
    String userName;
    String password;
    com.oshanak.mobilemarket.Activity.Models.metaData metaData;

    public LoginBodyModel2(String userName, String password,  metaData metaData) {
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

    public  metaData getMetaData() {
        return metaData;
    }

    public void setMetaData( metaData metaData) {
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
