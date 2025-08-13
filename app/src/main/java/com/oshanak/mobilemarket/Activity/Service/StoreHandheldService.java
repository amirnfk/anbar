package com.oshanak.mobilemarket.Activity.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundSaleItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.PrintBarcodeData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.DataStructure.StoreReturnItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.VendorData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseOrderData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingHeaderDetailWrapperData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingItemUMdata;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingProductContainer;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingProductInfoData;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class StoreHandheldService extends AsyncTask<Void, Void, Void>
{
    public OnTaskCompleted listener;
    private Common common;
    private StoreHandheldServiceMode storeHandheldServiceMode;
    public ArrayList<PropertyInfo> piList = new ArrayList<>();

    public StoreHandheldService(StoreHandheldServiceMode storeHandheldServiceMode, Context context)
    {
        this.storeHandheldServiceMode = storeHandheldServiceMode;
        common = new Common(context);
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected Void doInBackground(Void... params)
    {
        switch (storeHandheldServiceMode)
        {
            case Login:

                Login();
                break;
            case GetMaterialInfo:
                GetMaterialInfo();
                break;
            case GetOpenInboundFromSAP:
                GetOpenInboundFromSAP();
                break;
            case GetOpenInboundHeader:
                GetOpenInboundHeader();
                break;
            case GetInboundSaleItemFromSAP:
                GetInboundSaleItemFromSAP();
                break;
            case GetInboundSaleItem:
                GetInboundSaleItem();
                break;
            case InsertInboundDetail:
                InsertInboundDetail();
                break;
            case GetInboundDetail:
                GetInboundDetail();
                break;
            case DeleteInboundDetail:
                DeleteInboundDetail();
                break;
            case UpdateInboundDetail:
                UpdateInboundDetail();
                break;
            case SendInboundToSAP:
                SendInboundToSAP();
                break;
            case InsertGarbageProductDetail:
                InsertGarbageProductDetail();
                break;
            case GetGarbageProductHeader:
                GetGarbageProductHeader();
                break;
            case GetGarbageProductDetail:
                GetGarbageProductDetail();
                break;
            case DeleteGarbageProductDetail:
                DeleteGarbageProductDetail();
                break;
            case UpdateGarbageProductDetail:
                UpdateGarbageProductDetail();
                break;
            case DeleteGarbageProductHeader:
                DeleteGarbageProductHeader();
                break;
            case SendGarbageProductToSAP:
                SendGarbageProductToSAP();
                break;
            case InsertPrintBarcode:
                InsertPrintBarcode();
                break;
            case GetPrintBarcode:
                GetPrintBarcode();
                break;
            case UpdatePrintBarcode:
                UpdatePrintBarcode();
                break;
            case DeletePrintBarcode:
                DeletePrintBarcode();
                break;
            case SendPrintBarcodeToSAP:
                SendPrintBarcodeToSAP();
                break;
            case InsertWarehouseOrder:
                InsertWarehouseOrder();
                break;
            case UpdateWarehouseOrder:
                UpdateWarehouseOrder();
                break;
            case GetWarehouseOrder:
                GetWarehouseOrder();
                break;
            case DeleteWarehouseOrder:
                DeleteWarehouseOrder();
                break;
            case SendWarehouseOrderToSAP:
                SendWarehouseOrderToSAP();
                break;
            case GetDirectProductInfo:
                GetDirectProductInfo();
                break;
            case InsertDirectReceiveItem:
                InsertDirectReceiveItem();
                break;
            case UpdateDirectReceiveItem:
                UpdateDirectReceiveItem();
                break;
            case GetDirectReceiveHeader:
                GetDirectReceiveHeader();
                break;
            case DeleteDirectReceiveHeader:
                DeleteDirectReceiveHeader();
                break;
            case GetDirectReceiveDetail:
                GetDirectReceiveDetail();
                break;
            case DeleteDirectReceiveItem:
                DeleteDirectReceiveItem();
                break;
            case SendDirectReceiveToSAP:
                SendDirectReceiveToSAP();
                break;
            case InsertStoreReturnItem:
                InsertReturnItem();
                break;
            case UpdateStoreReturnItem:
                UpdateReturnItem();
                break;
            case GetStoreReturnItem:
                GetReturnItem();
                break;
            case DeleteStoreReturnItem:
                DeleteReturnItem();
                break;
            case SendStoreReturnToSAP:
                SendStoreReturnToSAP();
                break;
            case CheckForOpenWarehousing:
                CheckForOpenWarehousing();
                break;
            case StartWarehousing:
                StartWarehousing();
                break;
            case GetOpenWarehousing:
                GetOpenWarehousing();
                break;
            case GetWarehousingProductInfo:
                GetWarehousingProductInfo();
                break;
            case UpdateWarehousingItem:
                InsertWarehousingItem();
                break;
            case DeleteWarehousingItem:
                DeleteWarehousingItem();
                break;
            case SendWarehousingToSAP:
                SendWarehousingToSAP();
                break;
            case UpdateDirectReceiveDocDate:
                UpdateDirectReceiveDocDate();
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
    private void UpdateDirectReceiveDocDate()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateDirectReceiveDocDate"
                , piList);
    }
    private void SendWarehousingToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendWarehousingToSAP"
                , piList);
    }
    private void DeleteWarehousingItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteWarehousingItem"
                , piList);
    }
    private void InsertWarehousingItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateWarehousingItem"
                , piList);
    }
    private void GetWarehousingProductInfo()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetWarehousingProductInfo"
                , piList);
        updateWarehousingProductInfo();
    }
    private void GetOpenWarehousing()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetOpenWarehousing"
                , piList);
        updateWarehousingHeaderDetail();
    }
    private void StartWarehousing()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "StartWarehousing"
                , piList);
        updateWarehousingHeaderDetail();
    }
    private void CheckForOpenWarehousing()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "CheckForOpenWarehousing"
                , piList);
    }
    private void SendStoreReturnToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendStoreReturnToSAP"
                , piList);
    }
    private void DeleteReturnItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteReturnItem"
                , piList);
    }
    private void GetReturnItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetReturnItem"
                , piList);
        updateReturnStoreProduct();
    }
    private void UpdateReturnItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateReturnItem"
                , piList);
    }
    private void InsertReturnItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertReturnItem"
                , piList);
    }
    private void SendDirectReceiveToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendDirectReceiveToSAP"
                , piList);
    }
    private void DeleteDirectReceiveItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteDirectReceiveItem"
                , piList);
    }
    private void GetDirectReceiveDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetDirectReceiveDetail"
                , piList);
        updateDirectReceiveDetail();
    }
    private void DeleteDirectReceiveHeader()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteDirectReceiveHeader"
                , piList);
    }
    private void GetDirectReceiveHeader()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetDirectReceiveHeader"
                , piList);
        updateDirectReceiveHeader();
    }
    private void UpdateDirectReceiveItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateDirectReceiveItem"
                , piList);
    }
    private void InsertDirectReceiveItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertDirectReceiveItem"
                , piList);
    }
    private void GetDirectProductInfo()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetDirectProductInfo"
                , piList);
        updateMaterialInfoData(common.taskResult);
    }
    private void SendWarehouseOrderToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendWarehouseOrderToSAP"
                , piList);
    }
    private void DeleteWarehouseOrder()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteWarehouseOrder"
                , piList);
    }
    private void GetWarehouseOrder()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetWarehouseOrder"
                , piList);
        updateWarehouseOrder();
    }
    private void UpdateWarehouseOrder()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateWarehouseOrder"
                , piList);
    }
    private void InsertWarehouseOrder()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertWarehouseOrder"
                , piList);
    }
    private void DeletePrintBarcode()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeletePrintBarcode"
                , piList);
    }
    private void UpdatePrintBarcode()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdatePrintBarcode"
                , piList);
    }
    private void GetPrintBarcode()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetPrintBarcode"
                , piList);
        updatePrintBarcode();
    }
    private void InsertPrintBarcode()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertPrintBarcode"
                , piList);
    }
    private void InsertGarbageProductDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertGarbageProductDetail"
                , piList);
    }
    private void Login()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "Login"
                , piList);
    }
    private void UpdateGarbageProductDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateGarbageProductDetail"
                , piList);
    }
    private void GetMaterialInfo()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetMaterialInfo"
                , piList);
        updateMaterialInfoData(common.taskResult);
    }
    private void GetOpenInboundFromSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetOpenInboundFromSAP"
                , piList);
        updateInboundHeader();
    }
    private void GetOpenInboundHeader()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetOpenInboundHeader2"
                , piList);
        updateInboundHeader();
    }
    private void GetInboundSaleItem()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetInboundSaleItem"
                , piList);
        updateInboundSaleItem();
    }
    private void GetInboundSaleItemFromSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetInboundSaleItemFromSAP"
                , piList);
    }
    private void InsertInboundDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "InsertInboundDetail"
                , piList);
    }
    private void GetInboundDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetInboundDetail"
                , piList);
        updateInboundDetail();
    }
    private void DeleteInboundDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteInboundDetail"
                , piList);
    }
    private void UpdateInboundDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "UpdateInboundDetail"
                , piList);
    }
    private void SendInboundToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendInboundToSAP"
                , piList);
    }
    private void GetGarbageProductHeader()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetGarbageProductHeader"
                , piList);
        updateGarbageProductHeaderData();
    }
    private void GetGarbageProductDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "GetGarbageProductDetail"
                , piList);
        updateGarbageProductDetailData();
    }

    private void DeleteGarbageProductDetail()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteGarbageProductDetail"
                , piList);
    }
    private void DeleteGarbageProductHeader()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "DeleteGarbageProductHeader"
                , piList);
    }
    private void SendGarbageProductToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendGarbageProductToSAP"
                , piList);
    }
    private void SendPrintBarcodeToSAP()
    {
        common.GeneralServiceAction("StoreHandheld.svc","IStoreHandheld", "SendPrintBarcodeToSAP"
                , piList);
    }
    //region Update Properties
    private void updateWarehousingProductInfo()
    {
        if(!common.taskResult.isSuccessful) return;
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        SoapObject soapProductInfo = (SoapObject) soapObject.getProperty("warehousingProductInfoData");
        SoapObject soapItemUM = (SoapObject) soapObject.getProperty("warehousingItemUMdataList");

        WarehousingProductInfoData productInfoData = new WarehousingProductInfoData();
        productInfoData.setID(Integer.parseInt( soapProductInfo.getProperty("ID").toString().replace("anyType{}","0")));
        productInfoData.setItemID(Integer.parseInt( soapProductInfo.getProperty("ItemID").toString().replace("anyType{}","0")));
        productInfoData.setItemName( soapProductInfo.getProperty("ItemName").toString().replace("anyType{}",""));
        productInfoData.setCountValue(Integer.parseInt( soapProductInfo.getProperty("CountValue").toString().replace("anyType{}","0")));
        productInfoData.setLocation(Integer.parseInt( soapProductInfo.getProperty("Location").toString().replace("anyType{}","0")));
        productInfoData.setCountingDone(Boolean.parseBoolean( soapProductInfo.getProperty("CountingDone").toString().replace("anyType{}","false")));

        ArrayList<WarehousingItemUMdata> list = new ArrayList<>();
        for(int i= 0; i< soapItemUM.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapItemUM.getProperty(i);
            WarehousingItemUMdata data = new WarehousingItemUMdata();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setUMID(obj.getProperty("UMID").toString().replace("anyType{}",""));
            data.setMultipleConvert(Integer.parseInt( obj.getProperty("MultipleConvert").toString().replace("anyType{}","0")));
            list.add(data);
        }

        WarehousingProductContainer containerData = new WarehousingProductContainer();
        containerData.setWarehousingProductInfoData(productInfoData);
        containerData.setWarehousingItemUMdataList(list);
        common.taskResult.dataStructure = containerData;
    }
    private void updateWarehousingHeaderDetail()
    {
        if(!common.taskResult.isSuccessful) return;
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        SoapObject soapHeader = (SoapObject) soapObject.getProperty("warehousingHeaderList");
        SoapObject soapDetail = (SoapObject) soapObject.getProperty("warehousingDetailList");
        WarehousingHeaderDetailWrapperData wrapperData = new WarehousingHeaderDetailWrapperData();
        ArrayList<WarehousingHeaderData> headerList = getWarehousingHeaderList(soapHeader);
        ArrayList<WarehousingDetailData> detailList = getWarehousingDetailList(soapDetail);
        wrapperData.setWarehousingHeaderList(headerList);
        wrapperData.setWarehousingDetailList(detailList);
        common.taskResult.dataStructure = wrapperData;
    }
    private void updateWarehousingDetail( )
    {
        if(!common.taskResult.isSuccessful) return;
        ArrayList<WarehousingDetailData> list = getWarehousingDetailList((SoapObject) common.taskResult.dataStructure);
        common.taskResult.dataStructure = list;
    }
    private ArrayList<WarehousingDetailData> getWarehousingDetailList(SoapObject soapObject)
    {
        ArrayList<WarehousingDetailData> list = new ArrayList<>();
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            WarehousingDetailData data = new WarehousingDetailData();

            data.setID(Integer.parseInt(obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setHeaderCode(Integer.parseInt( obj.getProperty("HeaderCode").toString().replace("anyType{}","0")));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setCountValue(Integer.parseInt( obj.getProperty("CountValue").toString().replace("anyType{}","0")));
            data.setLocation(Integer.parseInt( obj.getProperty("Location").toString().replace("anyType{}","0")));
            data.setCreateDate(obj.getProperty("CreateDate").toString().replace("anyType{}",""));
            data.setCountingDone(Boolean.parseBoolean( obj.getProperty("CountingDone").toString().replace("anyType{}","false")));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setMultipleConvert(Integer.parseInt( obj.getProperty("MultipleConvert").toString().replace("anyType{}","0")));

            list.add(data);
        }
        return list;
    }
    private void updateWarehousingHeader( )
    {
        if(!common.taskResult.isSuccessful) return;
        ArrayList<WarehousingHeaderData> list = getWarehousingHeaderList((SoapObject) common.taskResult.dataStructure);
        common.taskResult.dataStructure = list;
    }
    private ArrayList<WarehousingHeaderData>  getWarehousingHeaderList(SoapObject soapObject)
    {
        ArrayList<WarehousingHeaderData> list = new ArrayList<>();
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            WarehousingHeaderData data = new WarehousingHeaderData();

            data.setCode(Integer.parseInt( obj.getProperty("Code").toString().replace("anyType{}","0")));
            data.setID(obj.getProperty("ID").toString().replace("anyType{}",""));
            data.setRetailStoreID(Integer.parseInt( obj.getProperty("RetailStoreID").toString().replace("anyType{}","0")));
            data.setCountingStatusID(Integer.parseInt( obj.getProperty("CountingStatusID").toString().replace("anyType{}","0")));
            data.setCountingStatusName( obj.getProperty("CountingStatusName").toString().replace("anyType{}",""));
            data.setCreateDate(obj.getProperty("CreateDate").toString().replace("anyType{}",""));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
            data.setHandheldIP(obj.getProperty("HandheldIP").toString().replace("anyType{}",""));
            data.setHandheldID(obj.getProperty("HandheldID").toString().replace("anyType{}",""));

            list.add(data);
        }
        return list;
    }
    private void updateReturnStoreProduct( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<StoreReturnItemData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            StoreReturnItemData data = new StoreReturnItemData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setStoreID(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setAmount(Integer.parseInt( obj.getProperty("Amount").toString().replace("anyType{}","0")));
            data.setSelectedUnit(obj.getProperty("SelectedUnit").toString().replace("anyType{}",""));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setBoxQuantity(Integer.parseInt( obj.getProperty("BoxQuantity").toString().replace("anyType{}","0")));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
            data.setReturnReasonID(Integer.parseInt( obj.getProperty("ReturnReasonID").toString().replace("anyType{}","0")));
            data.setReturnReasonCode(obj.getProperty("ReturnReasonCode").toString().replace("anyType{}",""));
            data.setReturnReasonName(obj.getProperty("ReturnReasonName").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateDirectReceiveHeader( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<DirectReceiveHeaderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            DirectReceiveHeaderData data = new DirectReceiveHeaderData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setInsertDate(obj.getProperty("InsertDate").toString().replace("anyType{}",""));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
            data.setStoreID(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0")));
            data.setVendorID(Integer.parseInt( obj.getProperty("VendorID").toString().replace("anyType{}","0")));
            data.setVendorName(obj.getProperty("VendorName").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateGarbageProductDetailData( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<GarbageProductDetailData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            GarbageProductDetailData data = new GarbageProductDetailData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setHeaderID(Integer.parseInt( obj.getProperty("HeaderID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setAmount(Double.parseDouble( obj.getProperty("Amount").toString().replace("anyType{}","0")));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setBoxQuantity(Integer.parseInt( obj.getProperty("BoxQuantity").toString().replace("anyType{}","0")));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateGarbageProductHeaderData( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<GarbageProductHeaderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            GarbageProductHeaderData data = new GarbageProductHeaderData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setInsertDate(obj.getProperty("InsertDate").toString().replace("anyType{}",""));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));
            data.setStoreID(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0")));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateInboundSaleItem()
    {
        if(!common.taskResult.isSuccessful) return;
        SoapObject obj = (SoapObject) common.taskResult.dataStructure;
        InboundSaleItemData data = new InboundSaleItemData();

        data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
        data.setRetailStoreId(Integer.parseInt( obj.getProperty("RetailStoreId").toString().replace("anyType{}","0")));
        data.setItemId(Integer.parseInt( obj.getProperty("ItemId").toString().replace("anyType{}","0")));
        data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
        data.setBarcode(obj.getProperty("Barcode").toString().replace("anyType{}",""));
        data.setMeins(obj.getProperty("Meins").toString().replace("anyType{}",""));
        data.setUMREZ(Integer.parseInt( obj.getProperty("UMREZ").toString().replace("anyType{}","0")));
        data.setTranslate_Meinh(obj.getProperty("Translate_Meinh").toString().replace("anyType{}",""));

        common.taskResult.dataStructure = data;
    }
    private void updateInboundHeader( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<InboundHeaderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
//            InboundHeaderData data = new InboundHeaderData();

//            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
//            data.setInboundId(obj.getProperty("InboundId").toString().replace("anyType{}","0"));
//            data.setOrderType(obj.getProperty("OrderType").toString().replace("anyType{}","D"));
//            data.setRetailStoreId(Integer.parseInt( obj.getProperty("RetailStoreId").toString().replace("anyType{}","0")));
//            data.setDATEN_S(obj.getProperty("DATEN_S").toString().replace("anyType{}","0"));
//            data.setDATEN_M(obj.getProperty("DATEN_M").toString().replace("anyType{}","0"));
//            data.setWBSTK(obj.getProperty("WBSTK").toString().replace("anyType{}","A"));
//            data.setLOGGR(obj.getProperty("LOGGR").toString().trim().replace("anyType{}","غذایی"));
//            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateInboundDetail( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<InboundDetailData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            InboundDetailData data = new InboundDetailData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setInboundHeaderID(Integer.parseInt( obj.getProperty("InboundHeaderID").toString().replace("anyType{}","0")));
            data.setInboundItemID(Integer.parseInt( obj.getProperty("InboundItemID").toString().replace("anyType{}","0")));
            data.setItemId(Integer.parseInt( obj.getProperty("ItemId").toString().replace("anyType{}","0")));
            data.setUserCount(Double.parseDouble( obj.getProperty("UserCount").toString().replace("anyType{}","0")));
            data.setUserMeins(obj.getProperty("UserMeins").toString().replace("anyType{}",""));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setUMREZ(Integer.parseInt( obj.getProperty("UMREZ").toString().replace("anyType{}","0")));
            data.setMeins(obj.getProperty("Meins").toString().replace("anyType{}",""));
            data.setTranslate_Meinh(obj.getProperty("Translate_Meinh").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updatePrintBarcode( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<PrintBarcodeData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            PrintBarcodeData data = new PrintBarcodeData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setStoreID(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setAmount(Integer.parseInt( obj.getProperty("Amount").toString().replace("anyType{}","0")));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setBoxQuantity(Integer.parseInt( obj.getProperty("BoxQuantity").toString().replace("anyType{}","0")));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateWarehouseOrder( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<WarehouseOrderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            WarehouseOrderData data = new WarehouseOrderData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setStoreID(Integer.parseInt( obj.getProperty("StoreID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setAmount(Integer.parseInt( obj.getProperty("Amount").toString().replace("anyType{}","0")));
            data.setSelectedUnit(obj.getProperty("SelectedUnit").toString().replace("anyType{}",""));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setBoxQuantity(Integer.parseInt( obj.getProperty("BoxQuantity").toString().replace("anyType{}","0")));
            data.setStatusID(Integer.parseInt( obj.getProperty("StatusID").toString().replace("anyType{}","0")));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateMaterialInfoData(TaskResult taskResult )
    {

        if(!taskResult.isSuccessful) return;
        SoapObject mainObject = (SoapObject)taskResult.dataStructure;

        ProductControlData productControlData = new ProductControlData();
        ProductUnitData productUnitData;
        List<ProductUnitData> productUnitDataList = new ArrayList<>();
        ProductControlContainerData productControlContainerData = new ProductControlContainerData();

        ArrayList<VendorData> vendorDataList = new ArrayList<>();
        VendorData vendorData = new VendorData();

        for(int i= 0; i< mainObject.getPropertyCount(); i++)
        {
            SoapObject arr = (SoapObject)mainObject.getProperty(i);
            if(arr.getName().equals("ProductControlData"))
            {
                SoapObject soapObject = arr;//(SoapObject)arr.getProperty(j);
                productControlData.setProductCode( soapObject.getProperty("ProductCode").toString().replace("anyType{}",""));
                productControlData.setProductName( soapObject.getProperty("ProductName").toString().replace("anyType{}",""));
                productControlData.setLatestSaleDate( soapObject.getProperty("LatestSaleDate").toString().replace("anyType{}",""));
                productControlData.setLatestReceiptDate( soapObject.getProperty("LatestReceiptDate").toString().replace("anyType{}",""));
                productControlData.setPromotion( soapObject.getProperty("Promotion").toString().replace("anyType{}",""));
                productControlData.setProvider( soapObject.getProperty("Provider").toString().replace("anyType{}",""));
                productControlData.setSalePrice( soapObject.getProperty("SalePrice").toString().replace("anyType{}",""));
                productControlData.setPrice( soapObject.getProperty("Price").toString().replace("anyType{}",""));
                productControlData.setSoldCount( Integer.parseInt( soapObject.getProperty("SoldCount").toString().replace("anyType{}","0")));
            }
            else if(arr.getName().equals("ArrayOfProductUnit"))
            {
                for (int j = 0; j < arr.getPropertyCount(); j++)
                {
                    SoapObject soapObject = (SoapObject) arr.getProperty(j);
                    productUnitData = new ProductUnitData();
                    productUnitData.setUnitName(soapObject.getProperty("UnitName").toString().replace("anyType{}", ""));
                    productUnitData.setAmount(soapObject.getProperty("Amount").toString().replace("anyType{}", ""));
                    productUnitDataList.add(productUnitData);
                }
            }
            else if(arr.getName().equals("ArrayOfVendorData"))
            {
                for (int j = 0; j < arr.getPropertyCount(); j++)
                {
                    SoapObject soapObject = (SoapObject) arr.getProperty(j);
                    vendorData = new VendorData();
                    vendorData.setID(soapObject.getProperty("ID").toString().replace("anyType{}", ""));
                    vendorData.setName(soapObject.getProperty("Name").toString().replace("anyType{}", ""));
                    vendorDataList.add(vendorData);
                }
            }
        }
        productControlContainerData.setProductControlData( productControlData);
        productControlContainerData.setProductUnitDataList(productUnitDataList);
        productControlContainerData.setVendorDataList(vendorDataList);

        common.taskResult.dataStructure = productControlContainerData;
    }
    private void updateDirectReceiveDetail( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<DirectReceiveDetailData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            DirectReceiveDetailData data = new DirectReceiveDetailData();

            data.setID(Integer.parseInt( obj.getProperty("ID").toString().replace("anyType{}","0")));
            data.setHeaderID(Integer.parseInt( obj.getProperty("HeaderID").toString().replace("anyType{}","0")));
            data.setItemID(Integer.parseInt( obj.getProperty("ItemID").toString().replace("anyType{}","0")));
            data.setAmount(Integer.parseInt( obj.getProperty("Amount").toString().replace("anyType{}","0")));
            data.setItemName(obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setPartUnit(obj.getProperty("PartUnit").toString().replace("anyType{}",""));
            data.setWholeUnit(obj.getProperty("WholeUnit").toString().replace("anyType{}",""));
            data.setBoxQuantity(Integer.parseInt( obj.getProperty("BoxQuantity").toString().replace("anyType{}","0")));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    //endregion Update Properties
}
