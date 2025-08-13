package com.oshanak.mobilemarket.Activity.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorCompanyData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorProductData;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class CompetitorService extends AsyncTask<Void, Void, Void>
{
    public OnTaskCompleted listener;
    private Common common;
    private CompetitorServiceMode competitorServiceMode;

    public CompetitorCompanyData competitorCompanyData;
    public CompetitorData competitorData;
    public CompetitorProductData competitorProductData;

    public ArrayList<PropertyInfo> piList = new ArrayList<>();

    public CompetitorService(CompetitorServiceMode competitorServiceMode, Context context)
    {
        this.competitorServiceMode = competitorServiceMode;

        common = new Common(context);
        switch (competitorServiceMode)
        {
            case GetCompetitorCompany:
                this.competitorCompanyData = new CompetitorCompanyData();
                break;
            case InsertCompetitor:
            case GetCompetitor:
            case UpdateCompetitor:
            case DeleteCompetitor:
                this.competitorData = new CompetitorData();
                break;
            case InsertCompetitorProduct:
            case GetCompetitorProduct:
            case UpdateCompetitorProduct:
            case DeleteCompetitorProduct:
                this.competitorProductData = new CompetitorProductData();
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
        switch (competitorServiceMode)
        {
            case GetCompetitorCompany:
                GetCompetitorCompany();
                break;
            case InsertCompetitor:
                InsertCompetitor();
                break;
            case GetCompetitor:
                GetCompetitor();
                break;
            case UpdateCompetitor:
                UpdateCompetitor();
                break;
            case InsertCompetitorProduct:
                InsertCompetitorProduct();
                break;
            case GetCompetitorProduct:
                GetCompetitorProduct();
                break;
            case UpdateCompetitorProduct:
                UpdateCompetitorProduct();
                break;
            case DeleteCompetitorProduct:
                DeleteCompetitorProduct();
                break;
            case DeleteCompetitor:
                DeleteCompetitor();
                break;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result)
    {
        listener.onTaskCompleted(common.taskResult);
    }

    private void GetCompetitorCompany()
    {
        common.GeneralServiceAction("Competitor.svc","ICompetitor", "GetCompetitorCompany"
                , piList);
        updateCompetitorCompanyData();
    }
    private void GetCompetitor()
    {

        common.GeneralServiceAction("Competitor.svc","ICompetitor", "GetCompetitor"
                , competitorData, "competitorData");
        updateCompetitorData();
    }
    private void GetCompetitorProduct()
    {

        common.GeneralServiceAction("Competitor.svc","ICompetitor",
                "GetCompetitorProduct", piList);
        updateCompetitorProductData();
    }
    private void InsertCompetitor()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "InsertCompetitor"
                , competitorData, "competitorData");
    }
    private void InsertCompetitorProduct()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "InsertCompetitorProduct"
                , competitorProductData, "competitorProductData");

//        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "InsertCompetitorProduct"
//                , piList);
    }
    private void UpdateCompetitor()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "UpdateCompetitor"
                , competitorData, "competitorData");
    }
    private void UpdateCompetitorProduct()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "UpdateCompetitorProduct"
                , competitorProductData, "competitorProductData");
    }
    private void DeleteCompetitorProduct()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "DeleteCompetitorProduct"
                , competitorProductData, "competitorProductData");
    }
    private void DeleteCompetitor()
    {
        common.GeneralServiceAction("Competitor.svc", "ICompetitor", "DeleteCompetitor"
                , competitorData, "competitorData");
    }

    private void updateCompetitorCompanyData( )
    {
        if(!common.taskResult.isSuccessful) return;
        List<CompetitorCompanyData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            CompetitorCompanyData data = new CompetitorCompanyData();

            data.setCode( Integer.parseInt( obj.getProperty("Code").toString()));
            data.setName( obj.getProperty("Name").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateCompetitorData( )
    {

        if(!common.taskResult.isSuccessful) return;
        List<CompetitorData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            CompetitorData data = new CompetitorData();

            data.setCode( Integer.parseInt( obj.getProperty("Code").toString()));
            data.setCompanyName( obj.getProperty("CompanyName").toString().replace("anyType{}",""));
            data.setName( obj.getProperty("Name").toString().replace("anyType{}",""));
            data.setInsertDate( obj.getProperty("InsertDate").toString().replace("anyType{}",""));
            data.setRegisterUserName( obj.getProperty("RegisterUserName").toString().replace("anyType{}",""));
            data.setCompanyCode( Integer.parseInt( obj.getProperty("CompanyCode").toString()));
            data.setLatitude( Double.parseDouble( obj.getProperty("Latitude").toString()));
            data.setLongitude( Double.parseDouble( obj.getProperty("Longitude").toString()));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }
    private void updateCompetitorProductData( )
    {

        if(!common.taskResult.isSuccessful) return;
        List<CompetitorProductData> list = new ArrayList<>();
        SoapObject soapObject = (SoapObject) common.taskResult.dataStructure;
        for(int i= 0; i< soapObject.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)soapObject.getProperty(i);
            CompetitorProductData data = new CompetitorProductData();

            data.setCode( Integer.parseInt( obj.getProperty("Code").toString()));
            data.setCompetitorCode( Integer.parseInt( obj.getProperty("CompetitorCode").toString()));
            data.setName( obj.getProperty("Name").toString().replace("anyType{}",""));
            data.setPrice( Integer.parseInt( obj.getProperty("Price").toString()));
            data.setIsPromotion( Integer.parseInt( obj.getProperty("IsPromotion").toString()));
            data.setBarcode( obj.getProperty("Barcode").toString().replace("anyType{}",""));
            data.setInsertDate( obj.getProperty("InsertDate").toString().replace("anyType{}",""));
            data.setRegisterUserName( obj.getProperty("RegisterUserName").toString().replace("anyType{}",""));

            list.add(data);
        }
        common.taskResult.dataStructure = list;
    }

}
