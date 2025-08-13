package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class DeliverOrderData implements KvmSerializable, Serializable
{
    private String CustomerId;//0
    private String CustomerName; //1
    private String Mobile; //2
    private int RetailStoreId;//3
    private String CustomerAddress;//4
    private String OrderId;//5
    private double TotalPrice = 0;//6
    private String OrderDate;//7
    private int OrderStatusId;//8
    private String OrderStatusName;//9
    private String AxFactorNo;//10
    private String Comment; //11
    private String No; //12
    private String Unit; //13;
    private String ReceiveDateTime; //14
    private double PosPrice = 0;//15
    private double CashPrice = 0;//16
    private double BonPrice = 0;//17
    private long OrderNo = 0;//18
    private int CodeConfirm = 0;//19
    private double CustomerLat = 0;//20
    private double CustomerLon = 0;//21
    private long AddressId = 0;//22
    private int OrderTypeCode = 0;//23
    private String OrderTypeName = "";//24

    public DeliverOrderData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return CustomerId;
            case 1: return CustomerName;
            case 2: return Mobile;
            case 3: return RetailStoreId;
            case 4: return CustomerAddress;
            case 5: return OrderId;
            case 6: return TotalPrice;
            case 7: return OrderDate;
            case 8: return OrderStatusId;
            case 9: return OrderStatusName;
            case 10: return AxFactorNo;
            case 11: return Comment;
            case 12: return No;
            case 13: return Unit;
            case 14: return ReceiveDateTime;
            case 15: return PosPrice;
            case 16: return CashPrice;
            case 17: return BonPrice;
            case 18: return OrderNo;
            case 19: return CodeConfirm;
            case 20: return CustomerLat;
            case 21: return CustomerLon;
            case 22: return AddressId;
            case 23: return OrderTypeCode;
            case 24: return OrderTypeName;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 25;
    }
    @Override
    public void setProperty(int arg0, Object value)
    {
        switch (arg0)
        {
            case 0:
                CustomerId = value.toString();
                break;
            case 1:
                CustomerName = value.toString();
                break;
            case 2:
                Mobile = value.toString();
                break;
            case 3:
                RetailStoreId = Integer.parseInt( value.toString());
                break;
            case 4:
                CustomerAddress = value.toString();
                break;
            case 5:
                OrderId = value.toString();
                break;
            case 6:
                TotalPrice = Double.parseDouble( value.toString());
                break;
            case 7:
                OrderDate = value.toString();
                break;
            case 8:
                OrderStatusId = Integer.parseInt( value.toString());
                break;
            case 9:
                OrderStatusName = value.toString();
                break;
            case 10:
                AxFactorNo = value.toString();
                break;
            case 11:
                Comment = value.toString();
                break;
            case 12:
                No = value.toString();
                break;
            case 13:
                Unit = value.toString();
                break;
            case 14:
                ReceiveDateTime = value.toString();
                break;
            case 15:
                PosPrice = Double.parseDouble( value.toString());
                break;
            case 16:
                CashPrice = Double.parseDouble( value.toString());
                break;
            case 17:
                BonPrice = Double.parseDouble( value.toString());
                break;
            case 18:
                OrderNo = Long.parseLong( value.toString());
                break;
            case 19:
                CodeConfirm = Integer.parseInt( value.toString());
                break;
            case 20:
                CustomerLat = Double.parseDouble( value.toString());
                break;
            case 21:
                CustomerLon = Double.parseDouble( value.toString());
                break;
            case 22:
                AddressId = Long.parseLong( value.toString());
                break;
            case 23:
                OrderTypeCode = Integer.parseInt( value.toString());
                break;
            case 24:
                OrderTypeName = value.toString();
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
                arg2.name = "CustomerId";
                break;
            case 1:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "CustomerName";
                break;
            case 2:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Mobile";
                break;
            case 3:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "RetailStoreId";
                break;
            case 4:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "CustomerAddress";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "OrderId";
                break;
            case 6:
                arg2.type = Double.class;
                arg2.name = "TotalPrice";
                break;
            case 7:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "OrderDate";
                break;
            case 8:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "OrderStatusId";
                break;
            case 9:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "OrderStatusName";
                break;
            case 10:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "AxFactorNo";
                break;
            case 11:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Comment";
                break;
            case 12:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "No";
                break;
            case 13:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Unit";
                break;
            case 14:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "ReceiveDateTime";
                break;
            case 15:
                arg2.type = Double.class;
                arg2.name = "PosPrice";
                break;
            case 16:
                arg2.type = Double.class;
                arg2.name = "CashPrice";
                break;
            case 17:
                arg2.type = Double.class;
                arg2.name = "BonPrice";
                break;
            case 18:
                arg2.type = Long.class;
                arg2.name = "OrderNo";
                break;
            case 19:
                arg2.type = Integer.class;
                arg2.name = "CodeConfirm";
                break;
            case 20:
                arg2.type = Double.class;
                arg2.name = "CustomerLat";
                break;
            case 21:
                arg2.type = Double.class;
                arg2.name = "CustomerLon";
                break;
            case 22:
                arg2.type = Long.class;
                arg2.name = "AddressId";
                break;
            case 23:
                arg2.type = Integer.class;
                arg2.name = "OrderTypeCode";
                break;
            case 24:
                arg2.type = String.class;
                arg2.name = "OrderTypeName";
                break;
        }
    }
    ///////////////////////////
    public double getPayablePrice() {
        return TotalPrice - BonPrice;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(int retailStoreId) {
        RetailStoreId = retailStoreId;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public int getOrderStatusId() {
        return OrderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        OrderStatusId = orderStatusId;
    }

    public String getOrderStatusName() {
        return OrderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        OrderStatusName = orderStatusName;
    }

    public String getAxFactorNo() {
        return AxFactorNo;
    }

    public void setAxFactorNo(String axFactorNo) {
        AxFactorNo = axFactorNo;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getReceiveDateTime() {
        return ReceiveDateTime;
    }

    public void setReceiveDateTime(String receiveDateTime) {
        ReceiveDateTime = receiveDateTime;
    }

    public double getPosPrice() {
        return PosPrice;
    }

    public void setPosPrice(double posPrice) {
        PosPrice = posPrice;
    }

    public double getCashPrice() {
        return CashPrice;
    }

    public void setCashPrice(double cashPrice) {
        CashPrice = cashPrice;
    }

    public double getBonPrice() {
        return BonPrice;
    }

    public void setBonPrice(double bonPrice) {
        BonPrice = bonPrice;
    }

    public long getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(long orderNo) {
        OrderNo = orderNo;
    }

    public int getCodeConfirm() {
        return CodeConfirm;
    }

    public void setCodeConfirm(int codeConfirm) {
        CodeConfirm = codeConfirm;
    }

    public double getCustomerLat() {
        return CustomerLat;
    }

    public void setCustomerLat(double customerLat) {
        CustomerLat = customerLat;
    }

    public double getCustomerLon() {
        return CustomerLon;
    }

    public void setCustomerLon(double customerLon) {
        CustomerLon = customerLon;
    }

    public long getAddressId() {
        return AddressId;
    }

    public void setAddressId(long addressId) {
        AddressId = addressId;
    }

    public int getOrderTypeCode() {
        return OrderTypeCode;
    }

    public void setOrderTypeCode(int orderTypeCode) {
        OrderTypeCode = orderTypeCode;
    }

    public String getOrderTypeName() {
        return OrderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        OrderTypeName = orderTypeName;
    }
}
