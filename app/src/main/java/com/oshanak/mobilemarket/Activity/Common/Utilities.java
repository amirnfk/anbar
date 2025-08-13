package com.oshanak.mobilemarket.Activity.Common;



import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.oshanak.mobilemarket.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class Utilities {
    public static int getApkVersionCode(Context cntx){
        PackageManager manager = cntx.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(
                    cntx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return info.versionCode;
    }
    public static String checkerviceType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("User_Data", MODE_PRIVATE);
        String serviceType=prefs.getString("ServiceType","Operational");
        return  serviceType;
    }
    public static String getApkVersionName(Context cntx){
        PackageManager manager = cntx.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(
                    cntx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return info.versionName;
    }
//    public static void showSuccessToast(Activity activity,String message) {
//        final Toast toast = new Toast(activity);
//        toast.setDuration(Toast.LENGTH_LONG);
//
//        //inflate view
//        View custom_view = activity.getLayoutInflater().inflate(R.layout.snackbar_toast_delivered_order, null);
//
//     TextView txtToast= custom_view.findViewById(R.id.toast_text);
//     txtToast.setText(message);
////        (custom_view.findViewById(R.id.toast_text)).setVisibility(View.GONE);
////        (custom_view.findViewById(R.id.separator)).setVisibility(View.GONE);
//        toast.setView(custom_view);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();
//
//    }

//    public static void showFailureToast(Activity activity,String message) {
//        final Toast toast = new Toast(activity);
//        toast.setDuration(Toast.LENGTH_LONG);
//
//        //inflate view
//        View custom_view = activity.getLayoutInflater().inflate(R.layout.snackbar_toast_canceled_order, null);
//
//        TextView txtToast= custom_view.findViewById(R.id.toast_text);
//        txtToast.setText(message);
////        (custom_view.findViewById(R.id.toast_text)).setVisibility(View.GONE);
////        (custom_view.findViewById(R.id.separator)).setVisibility(View.GONE);
//        toast.setView(custom_view);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();
//
//    }
//    public static void showServerErrorToast(Activity activity) {
//        final Toast toast = new Toast(activity.getApplicationContext());
//        toast.setDuration(Toast.LENGTH_LONG);
//
//        //inflate view
//        View custom_view = activity.getLayoutInflater().inflate(R.layout.snackbar_toast_server_error, null);
//        (custom_view.findViewById(R.id.tv_undo)).setVisibility(View.GONE);
//        (custom_view.findViewById(R.id.separator)).setVisibility(View.GONE);
//        toast.setView(custom_view);
//        toast.show();
//    }

    private class SolarCalendar {

        public String strWeekDay = "";
        public String strMonth = "";

        int date;
        int month;
        int year;

        public SolarCalendar()
        {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }

        public SolarCalendar(Date MiladiDate)
        {
            calcSolarCalendar(MiladiDate);
        }

        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                }

                else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = "فروردين";
                    break;
                case 2:
                    strMonth = "ارديبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تير";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهريور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دي";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = "يکشنبه";
                    break;
                case 1:
                    strWeekDay = "دوشنبه";
                    break;
                case 2:
                    strWeekDay = "سه شنبه";
                    break;
                case 3:
                    strWeekDay = "چهارشنبه";
                    break;
                case 4:
                    strWeekDay = "پنج شنبه";
                    break;
                case 5:
                    strWeekDay = "جمعه";
                    break;
                case 6:
                    strWeekDay = "شنبه";
                    break;
            }

        }

    }
    public static String getDeviceInfo()
    {
        return "Manufacturer:" + Build.MANUFACTURER + ",Model:" + Build.MODEL + ",SDK_Int:" + Build.VERSION.SDK_INT;

    }
    public static long[] getTimestamps7DaysAgoFromLastWeek() {
        // Get Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the current date
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Get the current day of the week (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of days to subtract to get to 7 days ago from the start of the last week
        int daysToSubtract = currentDayOfWeek + 6 + 7; // 6 days to go back to the start of the week (Sunday) + 7 days ago

        // Subtract the days to get to 7 days ago from the start of the last week
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract);

        // Set the time to the start of 7 days ago from last week (midnight)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the timestamp of the start of 7 days ago from last week
        long startTimestamp = calendar.getTimeInMillis();

        // Set the time to the end of 7 days ago from last week (23:59:59.999)
        calendar.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to get the end of the week
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        // Get the timestamp of the end of 7 days ago from last week
        long endTimestamp = calendar.getTimeInMillis();

        // Return an array containing the timestamps
        return new long[]{startTimestamp, endTimestamp};
    }
    public static long getTimestamp7DaysAgo() {
        // Get Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Subtract 7 days from the current time
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        // Return the timestamp of 7 days ago
        return calendar.getTimeInMillis();
    }
    public   static long[] getLastWeekTimestamps() {
        // Get Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the current date
        calendar.setTime(new Date());

        // Get the current day of the week (0 = Sunday, 1 = Monday, ..., 6 = Saturday)
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of days to subtract to get to the start of the last week
        int daysToSubtract = currentDayOfWeek + 6; // 6 days to go back to the start of the week (Sunday)

        // Subtract the days to get to the start of the last week
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract);

        // Set the time to the start of the last week (midnight)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the timestamp of the start of the last week
        long startOfLastWeekTimestamp = calendar.getTimeInMillis();

        // Add 6 days to get to the end of the last week (Saturday)
        calendar.add(Calendar.DAY_OF_YEAR, 6);

        // Set the time to the end of the last week (23:59:59.999)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        // Get the timestamp of the end of the last week
        long endOfLastWeekTimestamp = calendar.getTimeInMillis();

        // Return an array containing the timestamps
        return new long[]{startOfLastWeekTimestamp, endOfLastWeekTimestamp};
    }

    public static String getCurrentShamsidate() {
        Locale loc = new Locale("en_US");
        Utilities util = new Utilities();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.date+" "+sc.strMonth+" "+sc.year;
//                String.valueOf(sc.year) + "/" + String.format(loc, "%05d",
//                sc.strMonth) + "/" + String.format(loc, "%02d", sc.date);
    }
    public static String getCurrentShamsidateformat2() {
        Locale loc = new Locale("en_US");
        Utilities util = new Utilities();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.year+"/"+sc.month+"/"+sc.date;
//                String.valueOf(sc.year) + "/" + String.format(loc, "%05d",
//                sc.strMonth) + "/" + String.format(loc, "%02d", sc.date);
    }
    public static String getCurrentShamsiMonth() {
        Locale loc = new Locale("en_US");
        Utilities util = new Utilities();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.month+"";
//                String.valueOf(sc.year) + "/" + String.format(loc, "%05d",
//                sc.strMonth) + "/" + String.format(loc, "%02d", sc.date);
    }
    public static String getCurrentShamsiYear() {
        Locale loc = new Locale("en_US");
        Utilities util = new Utilities();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.year+"";
//                String.valueOf(sc.year) + "/" + String.format(loc, "%05d",
//                sc.strMonth) + "/" + String.format(loc, "%02d", sc.date);
    }
    public static String getCurrentShamsiDay() {
        Locale loc = new Locale("en_US");
        Utilities util = new Utilities();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.date+"";
//                String.valueOf(sc.year) + "/" + String.format(loc, "%05d",
//                sc.strMonth) + "/" + String.format(loc, "%02d", sc.date);
    }
    public static String getPersianShortDate(PersianCalendar persianCalendar)
    {
        int year = persianCalendar.getPersianYear();
        int month = persianCalendar.getPersianMonth();
        int day = persianCalendar.getPersianDay();
        int hour = persianCalendar.get(PersianCalendar.HOUR_OF_DAY);
        int minute = persianCalendar.get(PersianCalendar.MINUTE);
        int second = persianCalendar.get(PersianCalendar.SECOND);
        int milisecond = persianCalendar.get(PersianCalendar.MILLISECOND);
        return String.format("%04d", year) +"/"+ String.format("%02d", month) +"/"+ String.format("%02d", day) + " " +
                String.format("%02d", hour) +":"+ String.format("%02d", minute) +":"+ String.format("%02d", second) +":"+ String.format("%03d", milisecond);
    }
}