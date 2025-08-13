package com.oshanak.mobilemarket.Activity.Enum;

public enum PickingItemStatus
{
    Unknown(0, "Unknown", "نامشخص"),
    Confirm(1, "Confirm", "جمع آوري شد"),
    Reject(2, "Reject", "عدم جمع آوري");

    private final int Code;
    private final String Name;
    private final String Description;

    PickingItemStatus(int Code, String Name, String Description)
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
