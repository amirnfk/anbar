package com.oshanak.mobilemarket.Activity.Enum;

public enum ReturnReason
{
    Unknown(0, "Unknown", "نامشخص"),
    Absent(1, "Absent", "عدم حضور مشتري"),
    WrongAddress(2, "WrongAddress", "آدرس اشتباه"),
    Cancel(3, "Cancel", "انصراف مشتري"),
    WrongOrder(4, "WrongOrder", "تحويل سفارش نادرست"),
    Corrupt(5, "Corrupt", "تخريب يا فساد كالا"),
    Other(6, "Other", "ساير...");


    private final int Code;
    private final String Name;
    private final String Description;

    ReturnReason(int Code, String Name, String Description)
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
