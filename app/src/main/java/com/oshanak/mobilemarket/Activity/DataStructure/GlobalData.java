package com.oshanak.mobilemarket.Activity.DataStructure;

/**
 * Created by Masoud.
 */

public class GlobalData// extends Application
{
//    private static ShopData shopData = null;
    private static String UserName = "";
    private static String Password = "";
    private static int appVersionCode = 0;
    private static final int barcodeActivityRequestCode = 49374;
    private static final int searchByNameActivityRequestCode = 49375;
    private static String storeID = "";
    private static String storeName = "";
    private static String BasicAuthorization = "MobileService:Aa123456";
//    private static final String cafeBazaarKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDY0WBW6vhb0CbCwqo5u5cs0ulankh2UV8g3egVqANEUF0dmarIR+TyVJOqKDCXTzH+jJMOdzREDdVlaodGateA4Fgw8ad8EmS8BqqJ7qfEFAMs5XxPAQIU4y7TDc33RdJu4bb9Hq83sAYjJVyYYVXUzA4uhW3FlXfVN3SoSRlwvMmZTWXvpnGtEelxM3+vAuy3Epd5ToIZtwsK7gWMRJ3bnrXLUdnjlQk4U/mzxPcCAwEAAQ==";
    ////////////////////


    public static String getUserName() {
        return UserName.trim();
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }
    public static int getAppVersionCode() {
        return appVersionCode;
    }
    public static void setAppVersionCode(int _appVersionCode) {
        appVersionCode = _appVersionCode;
    }
    public static int getBarcodeActivityRequestCode() {
        return barcodeActivityRequestCode;
    }    public static int getSearchByNameActivityRequestCode() {
        return searchByNameActivityRequestCode;
    }
    public static String getStoreName() {
        return storeName;
    }
    public static void setStoreName(String storeName) {
        GlobalData.storeName = storeName;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }

    public static String getStoreID() {
        return storeID;
    }

    public static void setStoreID(String storeID) {
        GlobalData.storeID = storeID;
    }

    public static String getBasicAuthorization() {
        return BasicAuthorization;
    }
}
