package com.oshanak.mobilemarket.Activity.Service;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.ServiceUrlType;
import com.oshanak.mobilemarket.Activity.LocalDB.DBHandler;
import com.oshanak.mobilemarket.Activity.LocalDB.Param;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common
{
    private Context context;
    private String SOAP_ACTION = "";
    private String METHOD_NAME = "";
    private final int timeOut = 120000;//120 seconds
    private String NAMESPACE = "http://MobileService.org/";
    private static String apk = "haft.apk";

    public String URL()
    {
        DBHandler dbHandler = new DBHandler(context);
        String serviceUrlCode = dbHandler.getParamValue(Param.ServiceUrlType);
        if(Utility.applicationMode == ApplicationMode.PhoneDelivery)
        {
            return serviceUrlCode.equals("") || serviceUrlCode.equals("0") || serviceUrlCode.equals(ServiceUrlType.PhoneDelivery_Operational.getCode()) ?
                    ServiceUrlType.PhoneDelivery_Operational.getURL() : ServiceUrlType.PhoneDelivery_Pilot.getURL();
        }
        else if(Utility.applicationMode == ApplicationMode.PickingWarehouse)
        {
            return serviceUrlCode.equals("") || serviceUrlCode.equals("0") || serviceUrlCode.equals(ServiceUrlType.Picking_Operational.getCode()) ?
                    ServiceUrlType.Picking_Operational.getURL() : ServiceUrlType.Picking_Pilot.getURL();
        }
        else if(Utility.applicationMode == ApplicationMode.StoreHandheld)
        {
            return serviceUrlCode.equals("") || serviceUrlCode.equals("0") || serviceUrlCode.equals(ServiceUrlType.StoreHandheld_Operational.getCode()) ?
                    ServiceUrlType.StoreHandheld_Operational.getURL() : ServiceUrlType.StoreHandheld_Pilot.getURL();
        }
        else if(Utility.applicationMode == ApplicationMode.Competitor)
        {
            return serviceUrlCode.equals("") || serviceUrlCode.equals("0") || serviceUrlCode.equals(ServiceUrlType.Competitor_Operational.getCode()) ?
                    ServiceUrlType.Competitor_Operational.getURL() : ServiceUrlType.Competitor_Pilot.getURL();
//            return serviceUrlCode.equals("") || serviceUrlCode.equals("0") || serviceUrlCode.equals(ServiceUrlType.Picking_Operational.getCode()) ?
//                    ServiceUrlType.Picking_Operational.getURL() : ServiceUrlType.Picking_Pilot.getURL();
        }
        return "";
    }
    private String apkUrl()
    {

        return URL() + "App/haft.apk";
    }



    TaskResult taskResult = new TaskResult();



    public Common(Context context)
    {
        this.context = context;
    }

    public void GeneralServiceAction(String ServiceName, String interfaceName, String methodName, Object dataStructure, String variableName)
    {
        try
        {
            SOAP_ACTION = NAMESPACE + interfaceName + "/" + methodName;
            METHOD_NAME = methodName;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo pi = new PropertyInfo();
            pi.setNamespace(NAMESPACE);
            pi.setName(variableName);
            pi.setValue(dataStructure);
            pi.setType(dataStructure.getClass());
            Request.addProperty(pi);

            //
            MetaData metaData = new MetaData();
            metaData.UserName = (GlobalData.getUserName());
            metaData.AppVersionCode = GlobalData.getAppVersionCode();
            metaData.Password = GlobalData.getPassword();
            metaData.AppMode = Utility.applicationMode.toString();
            metaData.DeviceInfo = Utility.getDeviceInfo();
            metaData.StoreID = GlobalData.getStoreID();
//            metaData.IdentityKey = (GlobalData.getShopData() == null ? "" : GlobalData.getShopData().getIdentityKey());

            pi = new PropertyInfo();
            pi.setNamespace(NAMESPACE);
            pi.setName("metaData");
            pi.setValue(metaData);
            pi.setType(metaData.getClass());
            Request.addProperty(pi);
            //

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            soapEnvelope.addMapping(NAMESPACE, dataStructure.getClass().getSimpleName(),dataStructure.getClass());
            soapEnvelope.addMapping(NAMESPACE, metaData.getClass().getSimpleName(),metaData.getClass());
            //////////
            MarshalDouble md = new MarshalDouble();
            md.register(soapEnvelope);
            //////////

            HttpTransportSE transport = new HttpTransportSE(URL() + ServiceName + "/",timeOut);
            transport.setReadTimeout(timeOut);
//                        transport.debug = true;//

            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " +
                    org.kobjects.base64.Base64.encode(GlobalData.getBasicAuthorization().getBytes())));

//            transport.call(SOAP_ACTION, soapEnvelope);
            transport.call(SOAP_ACTION, soapEnvelope, headerList);
//                        String req = transport.requestDump;//
//                        String res = transport.responseDump;//

//            SoapPrimitive soapPrimitive = (SoapPrimitive)soapEnvelope.getResponse();
            SoapObject soapObject = (SoapObject)soapEnvelope.getResponse();

            taskResult.isSuccessful = Boolean.parseBoolean( soapObject.getProperty("isSuccessful").toString());
            taskResult.message = soapObject.getProperty("message").toString().replace("anyType{}","");
            taskResult.dataStructure = soapObject.getProperty("dataStructure");
            taskResult.tag = soapObject.getProperty("tag").toString().replace("anyType{}","");

//            envelope.dotNet = true;
//            envelope.implicitTypes = true;
//            envelope.encodingStyle = SoapSerializationEnvelope.XSD;
//            MarshalDouble md = new MarshalDouble();
//            md.register(envelope);

        } catch (Exception ex)
        {
            taskResult.isSuccessful = false;
            taskResult.message = ex.getMessage();
            taskResult.dataStructure = dataStructure;
        }
    }

    public void GeneralServiceAction(String ServiceName, String interfaceName, String methodName, ArrayList<PropertyInfo> piList)
    {
        try
        {
            SOAP_ACTION = NAMESPACE + interfaceName + "/" + methodName;
            METHOD_NAME = methodName;
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            //ترتیب ارسال پارامترها مهم است
            if(piList != null)
            {
                for (int i = 0; i < piList.size(); i++)
                {
                    Request.addProperty(piList.get(i));
                }
            }

            //
            MetaData metaData = new MetaData();
            metaData.UserName = (GlobalData.getUserName());
            metaData.AppVersionCode = GlobalData.getAppVersionCode();
            metaData.Password = GlobalData.getPassword();
            metaData.AppMode = Utility.applicationMode.toString();
            metaData.DeviceInfo = Utility.getDeviceInfo();
            metaData.StoreID = GlobalData.getStoreID();
//            metaData.IdentityKey = (GlobalData.getShopData() == null ? "" : GlobalData.getShopData().getIdentityKey());

            PropertyInfo pi = new PropertyInfo();
            pi.setNamespace(NAMESPACE);
            pi.setName("metaData");
            pi.setValue(metaData);
            pi.setType(metaData.getClass());
            Request.addProperty(pi);
            //


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            soapEnvelope.addMapping(NAMESPACE, metaData.getClass().getSimpleName(),metaData.getClass());
            //////////
            MarshalDouble md = new MarshalDouble();
            md.register(soapEnvelope);
            //////////
            HttpTransportSE transport = new HttpTransportSE(URL() + ServiceName + "/",timeOut);
            transport.setReadTimeout(timeOut);
//                        transport.debug = true;//

            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " +
                    org.kobjects.base64.Base64.encode(GlobalData.getBasicAuthorization().getBytes())));

//            transport.call(SOAP_ACTION, soapEnvelope);
            transport.call(SOAP_ACTION, soapEnvelope, headerList);
//                        String req = transport.requestDump;//
//                        String res = transport.responseDump;//

            //SoapPrimitive soapPrimitive = (SoapPrimitive)soapEnvelope.getResponse();
            SoapObject soapObject = (SoapObject)soapEnvelope.getResponse();

            taskResult.isSuccessful = Boolean.parseBoolean( soapObject.getProperty("isSuccessful").toString());
            taskResult.message = soapObject.getProperty("message").toString().replace("anyType{}","");
            taskResult.dataStructure = soapObject.getProperty("dataStructure");
            taskResult.tag = soapObject.getProperty("tag").toString().replace("anyType{}","");

        } catch (Exception ex)
        {
            taskResult.isSuccessful = false;
            taskResult.message = ex.getMessage();
            taskResult.dataStructure = null;
        }
    }




    // Todo این قسمت باید بعد از آپدیت موفق حذف شود.


    public void GetAPK(final Context context)
    {

        ((BaseActivity)context).startWait("لطفاً صبر نمایید." +"\n"+ "در حال دریافت نسخه جدید...");

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        destination += apk;
        final Uri uri = Uri.parse("file://" + destination);
        final File file = new File(destination);

        if (file.exists())
        {
            file.delete();
        }

        //get url of app on server
        String url = apkUrl();//Main.this.getString(R.string.update_app_url);

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("دانلود نسخه جدید...");
        request.setTitle("HAFT");

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//        try
//        {
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver()
        {
            public void onReceive(Context ctxt, Intent intent)
            {

                    try {

                        ((BaseActivity) ctxt).stopWait();
                        Uri uri;
                        String authority;
                        String pathName;
                        if (Build.VERSION.SDK_INT >= 24)
                        {
                            authority = BuildConfig.APPLICATION_ID + ".provider";
                            pathName = Environment.getExternalStorageDirectory() + "/download/" + apk;

                            uri = FileProvider.getUriForFile(ctxt, authority, new File(pathName));
                        }
                        else
                        {
                            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                            destination += apk;
                            uri = Uri.parse("file://" + destination);
                        }

                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        install.setDataAndType(uri,/* manager.getMimeTypeForDownloadedFile(downloadId)*/ "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        ////////////
                        File file = new File(URI.create(uri.getPath()).getPath());
                        if (file.exists()) {
                            //Do something
                            String s = file.getPath();
                        }
                        ////////////

                        ctxt.startActivity(install);

                        ctxt.unregisterReceiver(this);
//                        ((Activity) ctxt).finish();
                    }
                    catch(Exception e)
                    {
                        String S=e.getMessage();
                        Utility.simpleAlert(ctxt, S);
                    }

            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));//

    }


    // Todo این قسمت باید بعد از آپدیت موفق حذف شود.




    public static boolean haveStoragePermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else
        {
            return true;
        }
    }

}
