package com.oshanak.mobilemarket.Activity.LocalDB;

/**
 * Created by mfarahani.
 */

public class Param
{
    public static final String UserName = "UserName";
    public static final String RememberLoginPassword = "RememberLoginPassword";
    public static final String LoginPassword = "LoginPassword";
    public static final String ServiceUrlType = "ServiceUrlType";
    public static final String DeviceID = "DeviceID";
    public static final String StoreID = "StoreID";
    public static final String AutoScanEnable = "AutoScanEnable";

    private String paramName;
    private String paramValue;

    public Param()
    {}

    public Param(String paramName, String paramValue)
    {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }
    public String getParamName()
    {
        return paramName;
    }

    public void setParamValue(String paramValue)
    {
        this.paramValue = paramValue;
    }
    public String getParamValue()
    {
        return paramValue;
    }
}
