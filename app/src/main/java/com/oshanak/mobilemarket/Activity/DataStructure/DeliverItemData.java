package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class DeliverItemData implements KvmSerializable, Serializable
{
    private String OrderId;//0
    private int ItemId;//1
    private String ItemName;//2
    private double Quantity;//3
    private double UnitPrice;//4
    private double DeliverQuantity;//5
    private String Comment;//6

    public DeliverItemData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return OrderId;
            case 1: return ItemId;
            case 2: return ItemName;
            case 3: return Quantity;
            case 4: return UnitPrice;
            case 5: return DeliverQuantity;
            case 6: return Comment;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 7;
    }
    @Override
    public void setProperty(int arg0, Object value)
    {
        switch (arg0)
        {
            case 0:
                OrderId = value.toString();
                break;
            case 1:
                ItemId = Integer.parseInt( value.toString());
                break;
            case 2:
                ItemName = value.toString();
                break;
            case 3:
                Quantity = Double.parseDouble( value.toString());
                break;
            case 4:
                UnitPrice = Double.parseDouble( value.toString());
                break;
            case 5:
                DeliverQuantity = Double.parseDouble( value.toString());
                break;
            case 6:
                Comment = value.toString();
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
                arg2.name = "OrderId";
                break;
            case 1:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "ItemId";
                break;
            case 2:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "ItemName";
                break;
            case 3:
                arg2.type = Double.class;
                arg2.name = "Quantity";
                break;
            case 4:
                arg2.type = Double.class;
                arg2.name = "UnitPrice";
                break;
            case 5:
                arg2.type = Double.class;
                arg2.name = "DeliverQuantity";
                break;
            case 6:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Comment";
                break;
        }
    }
    //////////////////////////

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public double getDeliverQuantity() {
        return DeliverQuantity;
    }

    public void setDeliverQuantity(double deliverQuantity) {
        DeliverQuantity = deliverQuantity;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
