package com.oshanak.mobilemarket.Activity.Service.Retrofit;

public class LoginRequest {
    private String StoreId;
    private String StaffId;
    private String Password;

    public LoginRequest(String storeId, String staffId, String password) {
        StoreId = storeId;
        StaffId = staffId;
        Password = password;
    }

    // Getters and setters
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
}