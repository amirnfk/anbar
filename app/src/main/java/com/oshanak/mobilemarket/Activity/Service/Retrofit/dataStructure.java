package com.oshanak.mobilemarket.Activity.Service.Retrofit;

import com.oshanak.mobilemarket.Activity.Models.DocsTypeModel_Id_name;

import java.util.ArrayList;

public class dataStructure {
    private int StoreId;

    private double StoreLatitude;
    private double StoreLongitude;
    private String __type;
    private String OrderID;
    private float TotalPrice;
    private String CustomerID;
    private String CustomerName;
    private String CustomerMobile;
    private float CustomerAddressID;
    private float CustomerLat;
    private float CustomerLon;
    private float StoreID;
    private String StoreName;
    private  float StoreLat;
    private float StoreLon;
    private String ID;
    private String FirstName;
    private String LastName;
    private String Title;
    private String PersonnelNo;
    private String UserName;
    ArrayList<DocsTypeModel_Id_name> docstypes;
    private ArrayList <ServerConfig> generalConfigData;

    public dataStructure(String __type, String orderID, float totalPrice, String customerID, String customerName, String customerMobile, float customerAddressID, float customerLat, float customerLon, float storeID, String storeName, float storeLat, float storeLon, String ID, String firstName, String lastName, String title, String personnelNo, String userName, ArrayList<ServerConfig> generalConfigData) {
        this.__type = __type;
        OrderID = orderID;
        TotalPrice = totalPrice;
        CustomerID = customerID;
        CustomerName = customerName;
        CustomerMobile = customerMobile;
        CustomerAddressID = customerAddressID;
        CustomerLat = customerLat;
        CustomerLon = customerLon;
        StoreID = storeID;
        StoreName = storeName;
        StoreLat = storeLat;
        StoreLon = storeLon;
        this.ID = ID;
        FirstName = firstName;
        LastName = lastName;
        Title = title;
        PersonnelNo = personnelNo;
        UserName = userName;
        this.generalConfigData = generalConfigData;
    }

    public ArrayList<DocsTypeModel_Id_name> getDocstypes() {
        return docstypes;
    }

    public void setDocstypes(ArrayList<DocsTypeModel_Id_name> docstypes) {
        this.docstypes = docstypes;
    }

    public ArrayList<ServerConfig> getGeneralConfigData() {
        return generalConfigData;
    }

    public void setGeneralConfigData(ArrayList<ServerConfig> generalConfigData) {
        this.generalConfigData = generalConfigData;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }

    public float getCustomerAddressID() {
        return CustomerAddressID;
    }

    public void setCustomerAddressID(float customerAddressID) {
        CustomerAddressID = customerAddressID;
    }

    public float getCustomerLat() {
        return CustomerLat;
    }

    public void setCustomerLat(float customerLat) {
        CustomerLat = customerLat;
    }

    public float getCustomerLon() {
        return CustomerLon;
    }

    public void setCustomerLon(float customerLon) {
        CustomerLon = customerLon;
    }

    public float getStoreID() {
        return StoreID;
    }

    public void setStoreID(float storeID) {
        StoreID = storeID;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public float getStoreLat() {
        return StoreLat;
    }

    public void setStoreLat(float storeLat) {
        StoreLat = storeLat;
    }

    public float getStoreLon() {
        return StoreLon;
    }

    public void setStoreLon(float storeLon) {
        StoreLon = storeLon;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPersonnelNo() {
        return PersonnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        PersonnelNo = personnelNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return "dataStructure{" +
                "__type='" + __type + '\'' +
                ", OrderID='" + OrderID + '\'' +
                ", TotalPrice=" + TotalPrice +
                ", CustomerID='" + CustomerID + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", CustomerMobile='" + CustomerMobile + '\'' +
                ", CustomerAddressID=" + CustomerAddressID +
                ", CustomerLat=" + CustomerLat +
                ", CustomerLon=" + CustomerLon +
                ", StoreID=" + StoreID +
                ", StoreName='" + StoreName + '\'' +
                ", StoreLat=" + StoreLat +
                ", StoreLon=" + StoreLon +
                ", ID='" + ID + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Title='" + Title + '\'' +
                ", PersonnelNo='" + PersonnelNo + '\'' +
                ", UserName='" + UserName + '\'' +
                '}';
    }
}
