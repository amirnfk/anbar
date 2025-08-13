package com.oshanak.mobilemarket.Activity.Enum;

public enum DeliverOrderStatus
{
    Unknown(0, "Unknown", "نامشخص"),
    NewOrder(1, "NewOrder", "سفارش جدید"),
    Packing(2, "Packing", "درحال جمع آوری"),
    ReadyToSend(3, "ReadyToSend", "تحویل شده به توزیع"),
    Delivered(4, "Delivered", "تحویل کامل داده شده"),
    Return(5, "Return", "برگشت کامل"),
    ReturnItem(6, "ReturnItem", "برگشت جزیی");


    private final int Code;
    private final String Name;
    private final String Description;

    DeliverOrderStatus(int Code, String Name, String Description)
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
