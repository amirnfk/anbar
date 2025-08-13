package com.oshanak.mobilemarket.Activity.PushNotification;

public class CreateTokenRequest {
    private String StoreId;
    private String StaffId;
    private String Password;

    public CreateTokenRequest(String storeId, String staffId, String password) {
        StoreId = storeId;
        StaffId = staffId;
        Password = password;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "CreateTokenRequest{" +
                "StoreId='" + StoreId + '\'' +
                ", StaffId='" + StaffId + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}