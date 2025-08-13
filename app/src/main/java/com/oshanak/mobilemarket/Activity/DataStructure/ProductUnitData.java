package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class ProductUnitData implements KvmSerializable, Serializable
{
    private String UnitName = "";//0
    private String Amount = "";//1

    public ProductUnitData(String unitName, String amount)
    {
        this.UnitName = unitName;
        this.Amount = amount;
    }
    public ProductUnitData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return UnitName;
            case 1: return Amount;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 2;
    }
    @Override
    public void setProperty(int arg0, Object value) {
        switch (arg0) {
            case 0:
                UnitName = value.toString();
                break;
            case 1:
                Amount = value.toString();
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
                arg2.name = "UnitName";
                break;
            case 1:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Amount";
                break;
        }
    }
    //////////////

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
