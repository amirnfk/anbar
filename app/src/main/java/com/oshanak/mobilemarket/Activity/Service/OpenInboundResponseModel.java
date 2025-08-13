package com.oshanak.mobilemarket.Activity.Service;

import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.Activity.Models.DataStructure;

import java.util.List;

public class OpenInboundResponseModel {
    private boolean isSuccessful;
    private String message;
    private List<InboundHeaderData> inboundHeaderList;
    private Object tag; // Change the type according to the actual data type

    public OpenInboundResponseModel(boolean isSuccessful, String message, List<InboundHeaderData> inboundHeaderList, Object tag) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.inboundHeaderList = inboundHeaderList;
        this.tag = tag;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<InboundHeaderData> getInboundHeaderData() {
        return inboundHeaderList;
    }

    public void setInboundHeaderData(List<InboundHeaderData> inboundHeaderList) {
        this.inboundHeaderList = inboundHeaderList;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "OpenInboundResponseModel{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", inboundHeaderData=" + inboundHeaderList +
                ", tag=" + tag +
                '}';
    }
}
