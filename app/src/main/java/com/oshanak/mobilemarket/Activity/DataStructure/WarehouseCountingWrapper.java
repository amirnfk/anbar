package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;
import java.util.ArrayList;

public class WarehouseCountingWrapper implements Serializable
{
    private WarehouseCountingHeaderData warehouseCountingHeaderData;
    private ArrayList<WarehouseCountingDetailData> warehouseCountingDetailDataList;
    /////////////////
    public WarehouseCountingWrapper()
    {
        warehouseCountingHeaderData = new WarehouseCountingHeaderData();
        warehouseCountingDetailDataList = new ArrayList<>();
    }

    public WarehouseCountingHeaderData getWarehouseCountingHeaderData() {
        return warehouseCountingHeaderData;
    }

    public void setWarehouseCountingHeaderData(WarehouseCountingHeaderData warehouseCountingHeaderData) {
        this.warehouseCountingHeaderData = warehouseCountingHeaderData;
    }

    public ArrayList<WarehouseCountingDetailData> getWarehouseCountingDetailDataList() {
        return warehouseCountingDetailDataList;
    }

    public void setWarehouseCountingDetailDataList(ArrayList<WarehouseCountingDetailData> warehouseCountingDetailDataList) {
        this.warehouseCountingDetailDataList = warehouseCountingDetailDataList;
    }
}
