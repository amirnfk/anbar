package com.oshanak.mobilemarket.Activity.Enum;

public enum WarehousingItemFilterMode
{
    Unknown(0, "نامشخص"),
    AllItem(1, "كليه اقلام"),
    CountedItem(2, "اقلام شمارش شده"),
    NoCountedItem(3, "اقلام شمارش نشده");

    private int Code;
    private String Name;

    WarehousingItemFilterMode(int Code, String Name)
    {
        this.Code = Code;
        this.Name = Name;
    }

    public int getCode() {
        return Code;
    }
    public String getName() {
        return Name;
    }
}
