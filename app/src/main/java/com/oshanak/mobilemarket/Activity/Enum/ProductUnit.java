package com.oshanak.mobilemarket.Activity.Enum;

public enum ProductUnit
{
    Unknown(0,"Unknown", "نامشخص", "نامشخص"),
    ST(1,"ST", "واحد جزء (PC)" , "عدد"),
    KAR(2,"KAR", "واحد كل (كارتن)" , "كارتن"),
    Splash(3,"", "لطفا یکی از گزینه ها را انتخاب نمایید" , ""),
    G(4,"G", "واحد جزء (گرم)" , "گرم"),
    KG(5,"KG", "واحد كل (كيلوگرم)" , "كيلوگرم"),
    ;

    private int Code;
    private String Name;
    private String Description;
    private String ShortDesc;

    ProductUnit(int Code, String Name, String Description,String ShortDesc)
    {
        this.Code = Code;
        this.Name = Name;
        this.Description = Description;
        this.ShortDesc = ShortDesc;
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
    public String getShortDesc() {
        return ShortDesc;
    }
}
