package com.oshanak.mobilemarket.Activity.Service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oshanak.mobilemarket.Activity.Activity.MainMenuActivity;
import com.oshanak.mobilemarket.Activity.Activity.activity_splash;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if (!isAppOnForeground(context)){

try {
    Intent myIntent = new Intent(context, activity_splash.class);
    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(myIntent);
}catch (Exception e){

}
        }
    }
        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

}
