package com.oshanak.mobilemarket.Activity.Activity.Comparator;



import com.oshanak.mobilemarket.Activity.DataStructure.Promotion_Info_Model;

import java.util.Comparator;

public class DiscountComparator_High_to_Low implements Comparator {

    @Override
    public int compare(Object object1, Object object2) {
        Promotion_Info_Model promotion_info_model1= (Promotion_Info_Model) object1;
        Promotion_Info_Model promotion_info_model2= (Promotion_Info_Model) object2;
        int tempnum1;
        int tempnum2;
         tempnum1=promotion_info_model1.getPromotion_Product_Discount();
         tempnum2=promotion_info_model2.getPromotion_Product_Discount();
        tempnum1=checknull(tempnum1);
        tempnum2=checknull(tempnum2);

        if (tempnum1==tempnum2)
            return 0;
        if (tempnum1<tempnum2)
            return 1;
        return -1;

    }
    private Integer checknull(Integer d) {
        if (d == null ){

            d= Integer.valueOf(0);

        }
        return d;
    }
}