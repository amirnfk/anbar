package com.oshanak.mobilemarket.Activity.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.oshanak.mobilemarket.Activity.Activity.CheckNewVersionActivity;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

/**
 * Created by mfarahani on 2017/10/31.
 */

public class AppVersion
{
    private TaskResult taskResult;
    private Context context;

    public AppVersion(TaskResult taskResult, Context context)
    {
        this.taskResult = taskResult;
        this.context = context;
    }


    public boolean isNewVersionAvailable()
    {
        if(!taskResult.isSuccessful && taskResult.isExceptionOccured("APK version is out of date."))
        {
//            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
//            dlgAlert.setMessage("نسخه ای جدید از برنامه موجود می باشد و لازم است دریافت گردد.");
//            dlgAlert.setTitle(" ");
//            dlgAlert.setCancelable(true);
//            dlgAlert.setPositiveButton("دریافت نسخه", getNewVersion);
//            dlgAlert.setNegativeButton("انصراف",null);
//            dlgAlert.setIcon(R.drawable.info128);
//            dlgAlert.create().show();
//            return true;

            Intent intent = new Intent(context, CheckNewVersionActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return false;
    }

    private DialogInterface.OnClickListener getNewVersion = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            if(Common.haveStoragePermission(context))
            {
                new Common(context).GetAPK(context);
            }
        }
    };
    public static String getCurrentVersionName(Context context)
    {
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "0";
        }
    }
    public static int getCurrentVersionCode(Context context)
    {
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return 0;
        }
    }
}
