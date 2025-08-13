package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;

import java.util.List;

public class GetPickingDeliverItemResponse {
    private List<PickingDeliverItemData> pickingItemList;
    private boolean isSuccessful;
    private String message;

    public List<PickingDeliverItemData> getPickingItemList() {
        return pickingItemList;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }

}