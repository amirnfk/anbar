package com.oshanak.mobilemarket.Activity.Enum;

public enum PickingOrderStatus
{
    Unknown(0, "Unknown", "نامشخص"),
    Ready(1, "Ready", "آماده جمع آوري"),
    InPacking(2, "InPacking", "درحال جمع آوری"),
    PickedUp(3, "PickedUp", "جمع آوري شده"),
    InControl(4, "InControling", "در حال کنترل"),
    ControlConfirmed(5, "ControlConfirmed", " خاتمه کنترل"),
    SentToSap(1, "SentToSap", "ارسال شده به سپ");

    private final int Code;
    private final String Name;
    private final String Description;

    PickingOrderStatus(int Code, String Name, String Description)
    {
        this.Code = Code;
        this.Name = Name;
        this.Description = Description;
    }

    public int getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}
