package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;
import java.util.ArrayList;

public class WarehousingHeaderDetailWrapperData  implements Serializable
{
    private ArrayList<WarehousingHeaderData> warehousingHeaderList = null;//0
    private ArrayList<WarehousingDetailData> warehousingDetailList = null;//1
    ////////////////

    public ArrayList<WarehousingHeaderData> getWarehousingHeaderList() {
        return warehousingHeaderList;
    }

    public void setWarehousingHeaderList(ArrayList<WarehousingHeaderData> warehousingHeaderList) {
        this.warehousingHeaderList = warehousingHeaderList;
    }

    public ArrayList<WarehousingDetailData> getWarehousingDetailList() {
        return warehousingDetailList;
    }

    public void setWarehousingDetailList(ArrayList<WarehousingDetailData> warehousingDetailList) {
        this.warehousingDetailList = warehousingDetailList;
    }
}
