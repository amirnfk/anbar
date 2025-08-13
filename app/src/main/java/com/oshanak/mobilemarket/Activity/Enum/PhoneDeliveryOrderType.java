package com.oshanak.mobilemarket.Activity.Enum;

public enum PhoneDeliveryOrderType
{
    Unknown(0, "Unknown", "نامشخص"),
    Phone(1, "Phone", "تلفنی"),
    Modiseh(2, "Modiseh", "مدیسه"),
    Snap(3, "Snap", "اسنپ"),
    Evano(4, "Evano", "اوانو"),
    HaftKala(5, "HaftKala", "هفت کالا");

    private final int Code;
    private final String Name;
    private final String Description;

    PhoneDeliveryOrderType(int Code, String Name, String Description)
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
