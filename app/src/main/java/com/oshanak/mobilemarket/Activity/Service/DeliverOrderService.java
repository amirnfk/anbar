package com.oshanak.mobilemarket.Activity.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CustomerAddress;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GeneralConfigData;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class DeliverOrderService extends AsyncTask<Void, Void, Void>
{
    public OnTaskCompleted listener;
    private Common common;
    private DeliverOrderServiceMode deliverOrderServiceMode;

    public DeliverOrderData deliverOrderData;
    public DeliverItemData deliverItemData;

    public ArrayList<PropertyInfo> piList = new ArrayList<>();

    public DeliverOrderService(DeliverOrderServiceMode deliverOrderServiceMode, Context context)
    {
        this.deliverOrderServiceMode = deliverOrderServiceMode;

        common = new Common(context);
        switch (deliverOrderServiceMode)
        {
            case UpdateDeliverItem:
                this.deliverItemData = new DeliverItemData();
                break;
        }
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected Void doInBackground(Void... params)
    {
        switch (deliverOrderServiceMode)
        {
            case GetDeliverOrder:
                GetDeliverOrder();
                break;
            case InsertOrderStatus:
                InsertOrderStatus();
                break;
            case GetDeliverItem:
                GetDeliverItem();
                break;
            case UpdateDeliverItem:
                UpdateDeliverItem();
                break;
            case GetDeliverOrderConfigs:
                GetDeliverOrderConfigs();
                break;
            case GetCustomerAddress:
                GetCustomerAddress();
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

    private void GetDeliverOrder()
    {
        common.GeneralServiceAction("DeliverOrder.svc","IDeliverOrder", "GetDeliverOrder"
                , piList);
        updateDeliverOrder();
    }
    private void GetCustomerAddress()
    {
        common.GeneralServiceAction("DeliverOrder.svc","IDeliverOrder", "GetCustomerAddress"
                , piList);
        updateCustomerAddress();
    }
    private void GetDeliverOrderConfigs()
    {
        common.GeneralServiceAction("DeliverOrder.svc","IDeliverOrder", "GetDeliverOrderConfigs"
                , piList);
        updateConfigs();
    }
    private void InsertOrderStatus()
    {
        common.GeneralServiceAction("DeliverOrder.svc","IDeliverOrder", "InsertOrderStatus"
                , piList);
    }
    private void GetDeliverItem()
    {
        common.GeneralServiceAction("DeliverOrder.svc","IDeliverOrder", "GetDeliverItem"
                , piList);
        updateDeliverItem();
    }
    private void UpdateDeliverItem()
    {
        common.GeneralServiceAction("DeliverOrder.svc", "IDeliverOrder", "UpdateDeliverItem"
                , deliverItemData, "deliverItemData");
    }

    private void updateDeliverItem( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<DeliverItemData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            DeliverItemData data = new DeliverItemData();

            data.setOrderId( obj.getProperty("OrderId").toString().replace("anyType{}",""));
            data.setItemId( Integer.parseInt( obj.getProperty("ItemId").toString()));
            data.setItemName( obj.getProperty("ItemName").toString().replace("anyType{}",""));
            data.setQuantity( Integer.parseInt( obj.getProperty("Quantity").toString()));
            data.setUnitPrice( Integer.parseInt( obj.getProperty("UnitPrice").toString()));
            data.setDeliverQuantity( Integer.parseInt( obj.getProperty("DeliverQuantity").toString()));
            data.setComment( obj.getProperty("Comment").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateConfigs( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<GeneralConfigData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            GeneralConfigData data = new GeneralConfigData();

            data.setCode(Integer.parseInt( obj.getProperty("Code").toString()));
            data.setName( obj.getProperty("Name").toString().replace("anyType{}",""));
            data.setDescription( obj.getProperty("Description").toString().replace("anyType{}",""));
            data.setValue( obj.getProperty("Value").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateDeliverOrder( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<DeliverOrderData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            DeliverOrderData data = new DeliverOrderData();

            data.setCustomerId( obj.getProperty("CustomerId").toString().replace("anyType{}",""));
            data.setCustomerName( obj.getProperty("CustomerName").toString().replace("anyType{}",""));
            data.setMobile( obj.getProperty("Mobile").toString().replace("anyType{}",""));
            data.setRetailStoreId( Integer.parseInt( obj.getProperty("RetailStoreId").toString()));
            data.setCustomerAddress( obj.getProperty("CustomerAddress").toString().replace("anyType{}",""));
            data.setOrderId( obj.getProperty("OrderId").toString().replace("anyType{}",""));
            data.setTotalPrice( Integer.parseInt( obj.getProperty("TotalPrice").toString()));
            data.setOrderDate( obj.getProperty("OrderDate").toString().replace("anyType{}",""));
            data.setOrderStatusId( Integer.parseInt( obj.getProperty("OrderStatusId").toString()));
            data.setOrderStatusName( obj.getProperty("OrderStatusName").toString().replace("anyType{}",""));
            data.setAxFactorNo( obj.getProperty("AxFactorNo").toString().replace("anyType{}",""));
            data.setNo( obj.getProperty("No").toString().replace("anyType{}",""));
            data.setUnit( obj.getProperty("Unit").toString().replace("anyType{}",""));
            data.setReceiveDateTime( obj.getProperty("ReceiveDateTime").toString().replace("anyType{}",""));
            data.setPosPrice( Integer.parseInt( obj.getProperty("PosPrice").toString()));
            data.setCashPrice( Integer.parseInt( obj.getProperty("CashPrice").toString()));
            data.setBonPrice( Integer.parseInt( obj.getProperty("BonPrice").toString()));
            data.setOrderNo( Long.parseLong( obj.getProperty("OrderNo").toString()));
            data.setCodeConfirm( Integer.parseInt( obj.getProperty("CodeConfirm").toString()));
            data.setCustomerLat( Double.parseDouble( obj.getProperty("CustomerLat").toString()));
            data.setCustomerLon( Double.parseDouble( obj.getProperty("CustomerLon").toString()));
            data.setAddressId( Long.parseLong( obj.getProperty("AddressId").toString()));
            data.setOrderTypeCode( Integer.parseInt( obj.getProperty("OrderTypeCode").toString()));
            data.setOrderTypeName( obj.getProperty("OrderTypeName").toString());

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateCustomerAddress( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<CustomerAddress> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            CustomerAddress data = new CustomerAddress();

            data.setId( Long.parseLong( obj.getProperty("Id").toString()));
            data.setAddress( obj.getProperty("Address").toString().replace("anyType{}",""));
            data.setLatitude( Double.parseDouble( obj.getProperty("Latitude").toString()));
            data.setLongitude( Double.parseDouble( obj.getProperty("Longitude").toString()));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
}
