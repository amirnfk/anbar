package com.oshanak.mobilemarket.Activity.DataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductControlContainerData implements Serializable
{
    private ProductControlData productControlData;
    private List<ProductUnitData> productUnitDataList;
    private ArrayList<VendorData> vendorDataList;
    /////////////////
    public ProductUnitData getPartUnit()
    {
        for(int i = 0; i < productUnitDataList.size(); i++)
        {
            if(productUnitDataList.get(i).getUnitName().equalsIgnoreCase("PC") ||
                    productUnitDataList.get(i).getUnitName().equalsIgnoreCase("G"))
            {
                return productUnitDataList.get(i);
            }
        }
//        return null;
        return new ProductUnitData("", "0");
    }
    public ProductUnitData getWholeUnit()
    {
        for(int i = 0; i < productUnitDataList.size(); i++)
        {
            if(productUnitDataList.get(i).getUnitName().equalsIgnoreCase("CAR") ||
                    productUnitDataList.get(i).getUnitName().equalsIgnoreCase("KG"))
            {
                return productUnitDataList.get(i);
            }
        }
//        return null;
        return new ProductUnitData("", "0");
    }
    /////////////////
    public ProductControlData getProductControlData() {
        return productControlData;
    }

    public void setProductControlData(ProductControlData productControlData) {
        this.productControlData = productControlData;
    }

    public List<ProductUnitData> getProductUnitDataList() {
        return productUnitDataList;
    }

    public void setProductUnitDataList(List<ProductUnitData> productUnitDataList) {
        this.productUnitDataList = productUnitDataList;
    }

    public ArrayList<VendorData> getVendorDataList() {
        return vendorDataList;
    }

    public void setVendorDataList(ArrayList<VendorData> vendorDataList) {
        this.vendorDataList = vendorDataList;
    }
}
