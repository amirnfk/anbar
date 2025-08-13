package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class MetaData implements KvmSerializable, Serializable
{
    public String UserName = ""; //0
    public int AppVersionCode = -1;//1
//    public String IdentityKey = "";//2
    public String Password = "";//2
    public String AppMode = "";//3
    public String DeviceInfo = "";//4
    public String StoreID = "";//5

    public MetaData() {
    }

    public MetaData(String userName, int appVersionCode, String password, String appMode, String deviceInfo, String storeID) {
        UserName = userName;
        AppVersionCode = appVersionCode;
        Password = password;
        AppMode = appMode;
        DeviceInfo = deviceInfo;
        StoreID = storeID;
    }

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return UserName;
            case 1: return AppVersionCode;
            case 2: return Password;
            case 3: return AppMode;
            case 4: return DeviceInfo;
            case 5: return StoreID;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 6;
    }
    @Override
    public void setProperty(int arg0, Object value)
    {
        switch (arg0)
        {
            case 0:
                UserName = value.toString();
                break;
            case 1:
                AppVersionCode = Integer.parseInt( value.toString());
                break;
            case 2:
                Password = value.toString();
                break;
            case 3:
                AppMode = value.toString();
                break;
            case 4:
                DeviceInfo = value.toString();
                break;
            case 5:
                StoreID = value.toString();
                break;
        }
    }
    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2)
    {
        switch (arg0)
        {
            case 0:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "UserName";
                break;
            case 1:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "AppVersionCode";
                break;
            case 2:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Password";
                break;
            case 3:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "AppMode";
                break;
            case 4:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "DeviceInfo";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "StoreID";
                break;
        }
    }
    /////////////

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
        return "MetaData{" +
                "UserName='" + UserName + '\'' +
                ", AppVersionCode=" + AppVersionCode +
                ", Password='" + Password + '\'' +
                ", AppMode='" + AppMode + '\'' +
                ", DeviceInfo='" + DeviceInfo + '\'' +
                ", StoreID='" + StoreID + '\'' +
                '}';
    }
}
