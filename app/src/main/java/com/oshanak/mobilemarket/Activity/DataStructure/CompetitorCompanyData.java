package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class CompetitorCompanyData implements KvmSerializable, Serializable
{
    private int Code = 0; //0
    private String Name = ""; //1

    public CompetitorCompanyData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return Code;
            case 1: return Name;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 3;
    }
    @Override
    public void setProperty(int arg0, Object value)
    {
        switch (arg0)
        {
            case 0:
                Code = Integer.parseInt( value.toString());
                break;
            case 1:
                Name = value.toString();
                break;
        }
    }
    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2)
    {
        switch (arg0)
        {
            case 0:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "Code";
                break;
            case 1:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Name";
                break;
        }
    }
    ///////////////////

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
