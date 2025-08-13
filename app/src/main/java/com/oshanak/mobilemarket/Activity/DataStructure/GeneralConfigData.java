package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class GeneralConfigData implements Serializable
{
    private int Code;//0
    private String Name; //1
    private String Description; //2
    private String Value;//3
    //////////////////////

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
