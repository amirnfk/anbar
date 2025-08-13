package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class CompetitorData implements KvmSerializable, Serializable
{
    private int Code = 0; //0
    private String CompanyName = ""; //1
    private String Name = ""; //2
    private String InsertDate = "";//3
    private String RegisterUserName = "";//4
    private int CompanyCode = 0;//5
    private double Latitude = 0;//6
    private double Longitude = 0;//7

    public CompetitorData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return Code;
            case 1: return CompanyName;
            case 2: return Name;
            case 3: return InsertDate;
            case 4: return RegisterUserName;
            case 5: return CompanyCode;
            case 6: return Latitude;
            case 7: return Longitude;
        }
        return null;
    }
    @Override
    public int getPropertyCount()
    {
        return 8;
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
                CompanyName = value.toString();
                break;
            case 2:
                Name = value.toString();
                break;
            case 3:
                InsertDate = value.toString();
                break;
            case 4:
                RegisterUserName = value.toString();
                break;
            case 5:
                CompanyCode = Integer.parseInt( value.toString());
                break;
            case 6:
                Latitude = Double.parseDouble( value.toString());
                break;
            case 7:
                Longitude = Double.parseDouble( value.toString());
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
                arg2.name = "CompanyName";
                break;
            case 2:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Name";
                break;
            case 3:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "InsertDate";
                break;
            case 4:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "RegisterUserName";
                break;
            case 5:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "CompanyCode";
                break;
            case 6:
                arg2.type = Double.class;
                arg2.name = "Latitude";
                break;
            case 7:
                arg2.type = Double.class;
                arg2.name = "Longitude";
                break;
        }
    }
    ///////////////////
    public String getDateTime()
    {
        return getInsertDate().substring(0,16);
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public String getRegisterUserName() {
        return RegisterUserName;
    }

    public void setRegisterUserName(String registerUserName) {
        RegisterUserName = registerUserName;
    }

    public int getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(int companyCode) {
        CompanyCode = companyCode;
    }

    public String getNameWithCompany()
    {
        return getCompanyName() + "، " + getName();
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
