package com.oshanak.mobilemarket.Activity.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {
    @JsonProperty("GetPromotionListResult")
    public GetPromotionListResult getPromotionListResult;

    @Override
    public String toString() {
        return "Root{" +
                "getPromotionListResult=" + getPromotionListResult +
                '}';
    }
}
