package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

public class GetPickingDeliverItemRequest {
    private String HeaderID;
    private String LineID;
    private String itemFilter;
    private MetaData metaData;

    public GetPickingDeliverItemRequest(String headerID, String lineID, String itemFilter, MetaData metaData) {
        HeaderID = headerID;
        LineID = lineID;
        this.itemFilter = itemFilter;
        this.metaData = metaData;
    }

    public String getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(String headerID) {
        HeaderID = headerID;
    }

    public String getItemFilter() {
        return itemFilter;
    }

    public void setItemFilter(String itemFilter) {
        this.itemFilter = itemFilter;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public String getLineID() {
        return LineID;
    }

    public void setLineID(String lineID) {
        LineID = lineID;
    }

    @Override
    public String toString() {
        return "GetPickingDeliverItemRequest{" +
                "HeaderID='" + HeaderID + '\'' +
                ", LineID='" + LineID + '\'' +
                ", itemFilter='" + itemFilter + '\'' +
                ", metaData=" + metaData +
                '}';
    }
}