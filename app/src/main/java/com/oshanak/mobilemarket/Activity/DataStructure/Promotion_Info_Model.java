package com.oshanak.mobilemarket.Activity.DataStructure;

public class Promotion_Info_Model implements Comparable {

    String Promotion_Product_Id;
    String Promotion_Product_Name;

    String Promotion_Product_Image_src;
    int Promotion_Product_Price;
    int Promotion_Product_Off_Price;
    int Promotion_Product_Discount;
    String Promotion_Product_To_Date_S;
    String Promotion_Product_To_Date_M;
    String Promotion_Product_From_Date_S;
    String Promotion_Product_From_Date_M;

    public String getPromotion_Product_To_Date_S() {
        return Promotion_Product_To_Date_S;
    }

    public void setPromotion_Product_To_Date_S(String promotion_Product_To_Date_S) {
        Promotion_Product_To_Date_S = promotion_Product_To_Date_S;
    }

    public String getPromotion_Product_To_Date_M() {
        return Promotion_Product_To_Date_M;
    }

    public void setPromotion_Product_To_Date_M(String promotion_Product_To_Date_M) {
        Promotion_Product_To_Date_M = promotion_Product_To_Date_M;
    }

    public String getPromotion_Product_From_Date_S() {
        return Promotion_Product_From_Date_S;
    }

    public void setPromotion_Product_From_Date_S(String promotion_Product_From_Date_S) {
        Promotion_Product_From_Date_S = promotion_Product_From_Date_S;
    }

    public String getPromotion_Product_From_Date_M() {
        return Promotion_Product_From_Date_M;
    }

    public void setPromotion_Product_From_Date_M(String promotion_Product_From_Date_M) {
        Promotion_Product_From_Date_M = promotion_Product_From_Date_M;
    }

    public Promotion_Info_Model(String promotion_Product_Id, String promotion_Product_Name, String promotion_Product_Image_src, int promotion_Product_Price, int promotion_Product_Off_Price, int promotion_Product_Discount, String promotion_Product_To_Date_S, String promotion_Product_To_Date_M, String promotion_Product_From_Date_S, String promotion_Product_From_Date_M) {
        Promotion_Product_Id = promotion_Product_Id;
        Promotion_Product_Name = promotion_Product_Name;
        Promotion_Product_Image_src = promotion_Product_Image_src;
        Promotion_Product_Price = promotion_Product_Price;
        Promotion_Product_Off_Price = promotion_Product_Off_Price;
        Promotion_Product_Discount = promotion_Product_Discount;
        Promotion_Product_To_Date_S = promotion_Product_To_Date_S;
        Promotion_Product_To_Date_M = promotion_Product_To_Date_M;
        Promotion_Product_From_Date_S = promotion_Product_From_Date_S;
        Promotion_Product_From_Date_M = promotion_Product_From_Date_M;
    }

    public String getPromotion_Product_To_Date() {
        return Promotion_Product_To_Date_S;
    }

    public void setPromotion_Product_To_Date(String promotion_Product_To_Date) {
        Promotion_Product_To_Date_S = promotion_Product_To_Date;
    }

    public String getPromotion_Product_Id() {
        return Promotion_Product_Id;
    }

    public void setPromotion_Product_Id(String promotion_Product_Id) {
        Promotion_Product_Id = promotion_Product_Id;
    }

    public int getPromotion_Product_Discount() {
        return Promotion_Product_Discount;
    }

    public void setPromotion_Product_Discount(int promotion_Product_Discount) {
        Promotion_Product_Discount = promotion_Product_Discount;
    }

    public String getPromotion_Product_Name() {
        return Promotion_Product_Name;
    }

    public void setPromotion_Product_Name(String promotion_Product_Name) {
        Promotion_Product_Name = promotion_Product_Name;
    }

    public String getPromotion_Product_Image_src() {
        return Promotion_Product_Image_src;
    }

    public void setPromotion_Product_Image_src(String promotion_Product_Image_src) {
        Promotion_Product_Image_src = promotion_Product_Image_src;
    }

    public int getPromotion_Product_Price() {
        return Promotion_Product_Price;
    }

    public void setPromotion_Product_Price(int promotion_Product_Price) {
        Promotion_Product_Price = promotion_Product_Price;
    }

    public int getPromotion_Product_Off_Price() {
        return Promotion_Product_Off_Price;
    }

    public void setPromotion_Product_Off_Price(int promotion_Product_Off_Price) {
        Promotion_Product_Off_Price = promotion_Product_Off_Price;
    }

    @Override
    public int compareTo(Object o) {
        Promotion_Info_Model promotion_info_model= (Promotion_Info_Model) o;
        if(promotion_info_model.getPromotion_Product_Name().equals(this.Promotion_Product_Name)
                &&promotion_info_model.getPromotion_Product_Id().equals(this.getPromotion_Product_Id()))

            return 0;

        else{
            return 1;

        }
    }
}
