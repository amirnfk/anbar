package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;
import java.util.ArrayList;

public class WarehousingProductContainer implements Serializable
{
    private WarehousingProductInfoData warehousingProductInfoData;
    private ArrayList<WarehousingItemUMdata> warehousingItemUMdataList;
    /////////////////

    public WarehousingProductInfoData getWarehousingProductInfoData() {
        return warehousingProductInfoData;
    }

    public void setWarehousingProductInfoData(WarehousingProductInfoData warehousingProductInfoData) {
        this.warehousingProductInfoData = warehousingProductInfoData;
    }

    public ArrayList<WarehousingItemUMdata> getWarehousingItemUMdataList() {
        return warehousingItemUMdataList;
    }

    public void setWarehousingItemUMdataList(ArrayList<WarehousingItemUMdata> warehousingItemUMdataList) {
        this.warehousingItemUMdataList = warehousingItemUMdataList;
    }
    public WarehousingItemUMdata getWholeUnit()
    {
        for(int i = 0; i < warehousingItemUMdataList.size(); i++)
        {
            if(warehousingItemUMdataList.get(i).getUMID().equals("CAR") ||
                    warehousingItemUMdataList.get(i).getUMID().equals("KG"))
            {
                return warehousingItemUMdataList.get(i);
            }
        }
        return null;
    }
    public WarehousingItemUMdata getPartUnit()
    {
        for(int i = 0; i < warehousingItemUMdataList.size(); i++)
        {
            if(warehousingItemUMdataList.get(i).getUMID().equals("PC") ||
                    warehousingItemUMdataList.get(i).getUMID().equals("GR"))
            {
                return warehousingItemUMdataList.get(i);
            }
        }
        return null;
    }
}
