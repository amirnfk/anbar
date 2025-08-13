package com.oshanak.mobilemarket.Activity.Enum;

public enum StoreReturnItemReason
{
    Unknown(0, "", ""),
    R001(1, "001", "عدم رعايت عمر انبارش"),
    R002(2, "002", "دفرمگي در توزيع و بارگيري"),
    R003(3, "003", "عدم انطباق كيفي"),
    R004(4, "004", "دفرمگي كالا در فروشگاه"),
    R005(5, "005", "فراخوان كالا"),
    R006(6, "006", "ارسال مازاد از سفارش"),
    R007(7, "007", "مشاهده كسري در كارتن"),
    R008(8, "008", "مرجوعي كالا توسط مشتري"),
    R009(9, "009", "ناخوانا بودن مشخصات");

    private final int ID;
    private final String Code;
    private final String Name;

    StoreReturnItemReason(int ID, String Code, String Name)
    {
        this.ID = ID;
        this.Code = Code;
        this.Name = Name;
    }

    public int getID() {
        return ID;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }
}
