package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;

public class CustomerAddress implements Serializable
{
    private long Id;//0
    private String Address;//1
    private double Latitude;//2
    private double Longitude;//3
    ///////////////////////

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
