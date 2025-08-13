package com.oshanak.mobilemarket.Activity.Enum;

public enum PickingLineItemFilter
{
    All(0, "همه"),
    NonDetermined(1, "تعيين تكليف نشده"),
    NotEnoughInventory(2, "موجودي ناكافي"),
    DeliverLessThanInventory(3, "موجودي كافي"),
    NotFinalControlled(4, "کنترل نهایی نشده");

    private int Code;
    private String Name;

    PickingLineItemFilter(int Code, String Name)
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
