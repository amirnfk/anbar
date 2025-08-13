package com.oshanak.mobilemarket.Activity.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.oshanak.mobilemarket.Activity.Common.Utility;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.SapPickingResult;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingProductControlData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingWrapper;
import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class PickingDeliverService extends AsyncTask<Void, Void, Void>
{
    public OnTaskCompleted listener;
    private Common common;
    private PickingDeliverServiceMode pickingDeliverServiceMode;

    public ArrayList<PropertyInfo> piList = new ArrayList<>();
//    public PickingDeliverHeaderData pickingDeliverHeaderData;

    public PickingDeliverService(PickingDeliverServiceMode pickingDeliverServiceMode, Context context)
    {
        this.pickingDeliverServiceMode = pickingDeliverServiceMode;

        common = new Common(context);
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected Void doInBackground(Void... params)
    {
        switch (pickingDeliverServiceMode)
        {
            case GetPickingDeliverHeader:
                GetPickingDeliverHeader();
                break;
            case GetPickingDeliverItem:
                GetPickingDeliverItem();
                break;
            case UpdatePickingHeaderStatus:
                UpdatePickingHeaderStatus();
                break;
            case UpdatePickingItemStatus:
                UpdatePickingItemStatus();
                break;
            case UpdatePickingDeliverItemAmount:
                UpdatePickingDeliverItemAmount();
                break;
            case ProductControl:
                ProductControl();
                break;
            case InsertCounting:
                InsertCounting();
                break;
            case UpdateCounting:
                UpdateCounting();
                break;
            case GetCounting:
                GetCounting();
                break;
            case DeleteCounting:
                DeleteCounting();
                break;
            case SendCountingToSAP:
                SendCountingToSAP();
                break;
            case GetPickingControlHeader:
                GetPickingControlHeader();
                break;
            case UpdatePickingItemIsControlled:
                UpdatePickingItemIsControlled();
                break;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result)
    {
        listener.onTaskCompleted(common.taskResult);
    }
    public void addParam(String name, Object value)
    {
        Utility.addParam(piList, name, value);
    }
    private void UpdatePickingItemIsControlled()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "UpdatePickingItemIsControlled"
                , piList);
    }
    private void GetPickingControlHeader()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "GetPickingControlHeader"
                , piList);
        updatePickingControlHeader();
    }
    private void SendCountingToSAP()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "SendCountingToSAP"
                , piList);
    }
    private void DeleteCounting()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "DeleteCounting"
                , piList);
    }
    private void GetCounting()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "GetCounting"
                , piList);
        updateCountingProperties();
    }

    private void UpdateCounting()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "UpdateCounting"
                , piList);
    }
    private void InsertCounting()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "InsertCounting"
                , piList);
    }
    private void ProductControl()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "ProductControl"
                , piList);
        updateProductControl();
    }
    private void GetPickingDeliverHeader()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "GetPickingDeliverHeader"
                , piList);
        updatePickingDeliverHeader();
    }
    private void GetPickingDeliverItem()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "GetPickingDeliverItem"
                , piList);
        updatePickingDeliverItem();
    }
    private void UpdatePickingHeaderStatus()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "UpdatePickingHeaderStatus"
                , piList);
        updateSapPickingResult();
    }
    private void UpdatePickingItemStatus()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "UpdatePickingItemStatus"
                , piList);
    }
    private void UpdatePickingDeliverItemAmount()
    {
        common.GeneralServiceAction("PickingDeliver.svc","IPickingDeliver", "UpdatePickingDeliverItemAmount"
                , piList);
    }
    ///////////////////////////
    private void updateProductControl()
    {
        if(!common.taskResult.isSuccessful) return;
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        WarehouseCountingProductControlData data = new WarehouseCountingProductControlData();

        data.setProductCode( soapObject.getProperty("ProductCode").toString().replace("anyType{}",""));
        data.setProductName( soapObject.getProperty("ProductName").toString().replace("anyType{}",""));

        common.taskResult.dataStructure = data;
    }
    private void updateSapPickingResult()
    {
//        if(!common.taskResult.isSuccessful) return;
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        SapPickingResult data = new SapPickingResult();

        data.setError(Boolean.parseBoolean( soapObject.getProperty("Error").toString().replace("anyType{}","false")));
        data.setSuccess(Boolean.parseBoolean( soapObject.getProperty("Success").toString().replace("anyType{}","false")));
        data.setItemRejectedByUser(Boolean.parseBoolean( soapObject.getProperty("ItemRejectedByUser").toString().replace("anyType{}","false")));
        data.setItemDeletedDeficit(Boolean.parseBoolean( soapObject.getProperty("ItemDeletedDeficit").toString().replace("anyType{}","false")));
        data.setItemNotExistInSAP(Boolean.parseBoolean( soapObject.getProperty("ItemNotExistInSAP").toString().replace("anyType{}","false")));
        data.setMessage( soapObject.getProperty("Message").toString().replace("anyType{}",""));
        data.setItemInventoryAmountNotEnough( Boolean.parseBoolean(soapObject.getProperty("ItemInventoryAmountNotEnough").toString().replace("anyType{}","false")));
        data.setItemMinInventory(Integer.parseInt( soapObject.getProperty("ItemMinInventory").toString().replace("anyType{}","0")));
        data.setItemID(soapObject.getProperty("ItemID").toString().replace("anyType{}","0"));

        common.taskResult.dataStructure = data;
    }
    private void updatePickingDeliverHeader( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<PickingDeliverHeaderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            PickingDeliverHeaderData data = new PickingDeliverHeaderData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setVBELN( obj.getProperty("VBELN").toString().replace("anyType{}",""));
            data.setBLDAT( obj.getProperty("BLDAT").toString().replace("anyType{}",""));
            data.setWERKS( obj.getProperty("WERKS").toString().replace("anyType{}",""));
            data.setPLANT_NAME( obj.getProperty("PLANT_NAME").toString().replace("anyType{}",""));
            data.setKUNNR( obj.getProperty("KUNNR").toString().replace("anyType{}",""));
            data.setPEYKAR( obj.getProperty("PEYKAR").toString().replace("anyType{}",""));
            data.setPEYKAR_NAME( obj.getProperty("PEYKAR_NAME").toString().replace("anyType{}",""));
            data.setUserName( obj.getProperty("UserName").toString().replace("anyType{}",""));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
            data.setStatusName( obj.getProperty("StatusName").toString().replace("anyType{}",""));
            data.setOrderType( obj.getProperty("OrderType").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }

    private void updatePickingDeliverItem( )
    {
//        if(!common.taskResult.isSuccessful) return;
//        List<PickingDeliverItemData> list = new ArrayList<>();
//        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
//        for(int i= 0; i< soapObject.getPropertyCount(); i++)
//        {
//            SoapObject obj = (SoapObject)soapObject.getProperty(i);
//            PickingDeliverItemData data = new PickingDeliverItemData();
//
//            data.setID(Long.parseLong( obj.getProperty("ID").toString().replace("anyType{}","0")));
//            data.setHeaderID(Integer.parseInt( obj.getProperty("HeaderID").toString().replace("anyType{}","0")));
//            data.setMATNR( obj.getProperty("MATNR").toString().replace("anyType{}",""));
//            data.setLGPBE( obj.getProperty("LGPBE").toString().replace("anyType{}",""));
//            data.setARKTX( obj.getProperty("ARKTX").toString().replace("anyType{}",""));
//            data.setUMVKZ(Integer.parseInt( obj.getProperty("UMVKZ").toString().replace("anyType{}","0")));
//            data.setUMVKN(Integer.parseInt( obj.getProperty("UMVKN").toString().replace("anyType{}","0")));
//            data.setLFIMG(Integer.parseInt( obj.getProperty("LFIMG").toString().replace("anyType{}","0")));
//            data.setVRKME( obj.getProperty("VRKME").toString().replace("anyType{}",""));
//            data.setLBKUM(Integer.parseInt( obj.getProperty("LBKUM").toString().replace("anyType{}","0")));
//            data.setDeliverAmount(Integer.parseInt( obj.getProperty("DeliverAmount").toString().replace("anyType{}","0")));
//            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
//            data.setStatusName( obj.getProperty("StatusName").toString().replace("anyType{}",""));
//            data.setDeliverUnit( obj.getProperty("DeliverUnit").toString().replace("anyType{}",""));
//            data.setWholeUnit( obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
//            data.setPartUnit( obj.getProperty("PartUnit").toString().replace("anyType{}",""));
//            data.setBARCODE( obj.getProperty("BARCODE").toString().replace("anyType{}",""));
//            data.setFinalControlled( Boolean.parseBoolean(obj.getProperty("FinalControlled").toString().replace("anyType{}","false")));
//            data.setIs_PC_Unit( Boolean.parseBoolean(obj.getProperty("Is_PC_Unit").toString().replace("anyType{}","false")));
//            list.add(data);
//        }
//        common.taskResult.dataStructure = list;
    }
    private void updateCountingProperties( )
    {
        if(!common.taskResult.isSuccessful) return;
        WarehouseCountingWrapper warehouseCountingWrapper = new WarehouseCountingWrapper();

        SoapObject soapWrapper = (SoapObject) common.taskResult.dataStructure;
        SoapObject  soapHeader = (SoapObject)soapWrapper.getProperty("warehouseCountingHeaderData");
        SoapObject  soapDetail = (SoapObject)soapWrapper.getProperty("warehouseCountingDetailDataList");

        warehouseCountingWrapper.getWarehouseCountingHeaderData().setID(Integer.parseInt( soapHeader.getProperty("ID").toString()));
        warehouseCountingWrapper.getWarehouseCountingHeaderData().setInsertDate( soapHeader.getProperty("InsertDate").toString());
        warehouseCountingWrapper.getWarehouseCountingHeaderData().setStatusID(Integer.parseInt( soapHeader.getProperty("StatusID").toString()));
        warehouseCountingWrapper.getWarehouseCountingHeaderData().setLineNumber(Integer.parseInt( soapHeader.getProperty("LineNumber").toString()));
        warehouseCountingWrapper.getWarehouseCountingHeaderData().setUserName( soapHeader.getProperty("UserName").toString());

        for(int i= 0; i< soapDetail.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapDetail.getProperty(i);
            WarehouseCountingDetailData data = new WarehouseCountingDetailData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setHeaderID(Integer.parseInt( obj.getProperty("HeaderID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}","0"));
            data.setAmount(Double.parseDouble(obj.getProperty("Amount").toString().replace("anyType{}","0")));
            warehouseCountingWrapper.getWarehouseCountingDetailDataList().add(data);
        }
        common.taskResult.dataStructure = warehouseCountingWrapper;
    }

    private void updatePickingControlHeader( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<PickingDeliverHeaderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            PickingDeliverHeaderData data = new PickingDeliverHeaderData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setVBELN( obj.getProperty("VBELN").toString().replace("anyType{}",""));
            data.setBLDAT( obj.getProperty("BLDAT").toString().replace("anyType{}",""));
//            data.setItemCount(Integer.parseInt( obj.getProperty("ItemCount").toString().replace("anyType{}","0")));
//            data.setPalletCount(Integer.parseInt( obj.getProperty("PalletCount").toString().replace("anyType{}","0")));
//            data.setCollectorName( obj.getProperty("UserName").toString().replace("anyType{}",""));
            data.setStoreID(String.valueOf(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0"))));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
}
