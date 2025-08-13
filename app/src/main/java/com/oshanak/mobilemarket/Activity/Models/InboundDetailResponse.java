package com.oshanak.mobilemarket.Activity.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.InboundDetailData;

import java.util.List;

public class InboundDetailResponse {
    private boolean isSuccessful;
    private String message;
    private List<InboundDetailData> inboundDetailList;
    private Object tag;

    public InboundDetailResponse(boolean isSuccessful, String message, List<InboundDetailData> inboundDetailList, Object tag) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.inboundDetailList = inboundDetailList;
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

    public List<InboundDetailData> getInboundDetailList() {
        return inboundDetailList;
    }

    public void setInboundDetailList(List<InboundDetailData> inboundDetailList) {
        this.inboundDetailList = inboundDetailList;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "InboundDetailResponse{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", inboundDetailList=" + inboundDetailList +
                ", tag=" + tag +
                '}';
    }
}
