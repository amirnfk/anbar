package com.oshanak.mobilemarket.Activity.Enum;

public enum ServiceUrlType
{
    Unknown("0", "Unknown", "نامشخص", ""),
    PhoneDelivery_Operational("1", "Operational",  "فروش تلفني عملياتي","https://mobile.ows.gbgnetwork.net/"),
    PhoneDelivery_Pilot("2", "Pilot", "فروش تلفني آزمايشي", "https://mobilepilot.ows.gbgnetwork.net/"),
    Picking_Operational("3", "Picking_Operational",  "جمع آوري انبار عملياتي","https://picking.ows.gbgnetwork.net/"),
    Picking_Pilot("4", "Picking_Pilot",  "جمع آوري انبار آزمايشي","https://pickingpilot.ows.gbgnetwork.net/"),
    StoreHandheld_Operational("5", "StoreHandheld_Operational",  "هندهلد فروشگاهي عملياتي","https://storehandheld.ows.gbgnetwork.net/"),
    StoreHandheld_Pilot("6", "StoreHandheld_Pilot",  "هندهلد فروشگاهي آزمايشي","https://storehandheldpilot.ows.gbgnetwork.net/"),

    Competitor_Operational("7", "Competitor_Operational",  "رقبا عملياتي","https://competitor.ows.gbgnetwork.net/"),
    Competitor_Pilot("8", "Competitor_Pilot",  "رقبا آزمايشي","https://competitorpilot.ows.gbgnetwork.net/");


    private final String Code;
    private final String Name;
    private final String Description;
    private final  String URL;

    ServiceUrlType(String Code, String Name, String Description, String URL)
    {
        this.Code = Code;
        this.Name = Name;
        this.Description = Description;
        this.URL = URL;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getURL() {
        return URL;
    }
}
