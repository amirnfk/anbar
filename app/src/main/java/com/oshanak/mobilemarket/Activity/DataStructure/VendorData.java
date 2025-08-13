package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class VendorData implements Serializable
{
    private String ID = "";//0
    private String Name = "";//1
    /////////////

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
