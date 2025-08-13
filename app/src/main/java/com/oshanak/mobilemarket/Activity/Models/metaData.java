package com.oshanak.mobilemarket.Activity.Models;


import java.io.Serializable;

public class metaData implements Serializable {
    public String UserName = ""; //0
    public int AppVersionCode = -1;//1
    //    public String IdentityKey = "";//2
    public String Password = "";//2
    public String AppMode = "";//3
    public String DeviceInfo = "";//4
    public String StoreID = "";//5

    public metaData(String userName, int appVersionCode, String password, String appMode, String deviceInfo, String storeID) {
        UserName = userName;
        AppVersionCode = appVersionCode;
        Password = password;
        AppMode = appMode;
        DeviceInfo = deviceInfo;
        StoreID = storeID;
    }
    public metaData(String userName, int appVersionCode, String deviceInfo, String appMode) {
        UserName = userName;
        AppVersionCode = appVersionCode;
        DeviceInfo = deviceInfo;
        AppMode = appMode;


    }
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getAppVersionCode() {
        return AppVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        AppVersionCode = appVersionCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAppMode() {
        return AppMode;
    }

    public void setAppMode(String appMode) {
        AppMode = appMode;
    }

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    @Override
    public String toString() {
        return
                "UserName='" + UserName + '\'' +
                        ", AppVersionCode=" + AppVersionCode +
                        ", Password='" + Password + '\'' +
                        ", AppMode='" + AppMode + '\'' +
                        ", DeviceInfo='" + DeviceInfo + '\'' +
                        ", StoreID='" + StoreID + '\'' ;
    }
}
