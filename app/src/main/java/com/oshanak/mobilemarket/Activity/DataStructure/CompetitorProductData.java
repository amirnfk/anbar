package com.oshanak.mobilemarket.Activity.DataStructure;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class CompetitorProductData implements KvmSerializable, Serializable
{
    private int Code = 0; //0
    private int CompetitorCode = 0;//1
    private String Name = ""; //2
    private int Price = 0; //3
    private int IsPromotion = 0; //4
    private String Barcode = "";//5
    private String RegisterUserName = ""; //6
    private String InsertDate = ""; //7

    public CompetitorProductData(){}

    @Override
    public Object getProperty(int arg0)
    {
        switch (arg0)
        {
            case 0: return Code;
            case 1: return CompetitorCode;
            case 2: return Name;
            case 3: return Price;
            case 4: return IsPromotion;
            case 5: return Barcode;
            case 6: return RegisterUserName;
            case 7: return InsertDate;
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
                CompetitorCode = Integer.parseInt( value.toString());
                break;
            case 2:
                Name = value.toString();
                break;
            case 3:
                Price = Integer.parseInt( value.toString());
                break;
            case 4:
                IsPromotion = Integer.parseInt( value.toString());
                break;
            case 5:
                Barcode = value.toString();
                break;
            case 6:
                RegisterUserName = value.toString();
                break;
            case 7:
                InsertDate = value.toString();
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
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "CompetitorCode";
                break;
            case 2:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Name";
                break;
            case 3:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "Price";
                break;
            case 4:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "IsPromotion";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "Barcode";
                break;
            case 6:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "RegisterUserName";
                break;
            case 7:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "InsertDate";
                break;

        }
    }
    ///////////////////
    public String getDateTime()
    {
        return getInsertDate().substring(0,16);
    }
    public String getDate()
    {
        return getInsertDate().substring(0,10);
    }
    public String getTime()
    {
        return getInsertDate().substring(11,16);
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public int getCompetitorCode() {
        return CompetitorCode;
    }

    public void setCompetitorCode(int competitorCode) {
        CompetitorCode = competitorCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getIsPromotion() {
        return IsPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        IsPromotion = isPromotion;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getRegisterUserName() {
        return RegisterUserName;
    }

    public void setRegisterUserName(String registerUserName) {
        RegisterUserName = registerUserName;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

}
