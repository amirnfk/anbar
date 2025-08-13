package com.oshanak.mobilemarket.Activity.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.Service.Enum.GeneralServiceMode;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class GeneralService extends AsyncTask<Void, Void, Void>
{
    public OnTaskCompleted listener;
    private Common common;
    private GeneralServiceMode generalServiceMode;

    public ArrayList<PropertyInfo> piList = new ArrayList<>();

    public GeneralService(GeneralServiceMode generalServiceMode, Context context)
    {
        this.generalServiceMode = generalServiceMode;
        common = new Common(context);
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected Void doInBackground(Void... params)
    {
        switch (generalServiceMode)
        {
            case Login:
                Login();
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
    private void Login()
    {

        common.GeneralServiceAction("General.svc","IGeneral", "Login"
                , piList);

    }
}
