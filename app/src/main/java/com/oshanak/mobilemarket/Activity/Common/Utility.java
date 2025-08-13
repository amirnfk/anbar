package com.oshanak.mobilemarket.Activity.Common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.oshanak.mobilemarket.Activity.Activity.FastHelpActivity;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.UnitValue;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.Font;
import com.oshanak.mobilemarket.Activity.Enum.PickingLineItemFilter;
import com.oshanak.mobilemarket.Activity.Enum.StoreReturnItemReason;
import com.oshanak.mobilemarket.Activity.LocalDB.DBHandler;
import com.oshanak.mobilemarket.Activity.LocalDB.Param;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_general_spinner;
import com.oshanak.mobilemarket.Activity.PickingApp.row_picking_line_item_filter_spinner;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_return_store_product_spinner;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;


/**
 * Created by Masoud on 12/16/2016.
 */

public class Utility {
    public static ApplicationMode applicationMode = ApplicationMode.PickingWarehouse;

    public static boolean editTextIsEmpty(View view) {
        return editTextIsEmpty(view, "");
    }

    public static boolean editTextIsEmpty(View view, String message) {
        EditText editText = (EditText) view;
        String txt = editText.getText().toString();
        boolean result = (txt.isEmpty() || txt.length() == 0 || txt.equals("") || txt == null);
        if (result && message != "") {
            editText.setError(message);
            editText.requestFocus();
        }
        return result;
    }

    public static String getLstWeekShamsiDate(){
        PersianCalendar persianCalendar=new PersianCalendar();
        persianCalendar.addPersianDate(PersianCalendar.DATE,-7);

        return persianCalendar.getPersianShortDate();
    }

    public static String getYesterdaysMiladiDate() {
        Calendar calendar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int year = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            year = calendar.get(Calendar.YEAR);
        }
        int month = 0; // Month is 0-based
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        int day = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        // Calculate yesterday's date
        day -= 1;
        if (day == 0) {
            month -= 1;
            if (month == 0) {
                year -= 1;
                month = 12;
            }
            // Calculate the last day of the previous month
            Calendar lastDayOfPreviousMonth = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lastDayOfPreviousMonth = Calendar.getInstance();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lastDayOfPreviousMonth.set(year, month - 1, 1);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                day = lastDayOfPreviousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        }

        // Format the date as "yyyy/MM/dd"
        String yesterdayShamsiDate = String.format("%04d-%02d-%02d", year, month, day);
        return yesterdayShamsiDate;

    }
    public static void showErrorDialog(Context context, String message) {
       final Dialog errorDialog = new Dialog(context);
        errorDialog.setContentView(R.layout.dialog_error);
         TextView title = errorDialog.findViewById(R.id.dialog_title);
        TextView msg = errorDialog.findViewById(R.id.dialog_message);
        msg.setText(message);
       Button button = errorDialog.findViewById(R.id.dialog_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                errorDialog.dismiss(); // Close the dialog }
            }

        });
        errorDialog.show();
    }

        public static void setFont(Context context, ViewGroup parent)
    {
        for (int i = parent.getChildCount() - 1; i >= 0; i--)
        {
            final View child = parent.getChildAt(i);
            String tag = (child.getTag() == null ? "" : child.getTag().toString().trim());
            if( !tag.equals("sfm")) //sfm: Set Font Manual
            {
//                if (child instanceof LineChart)
//                {
//                    setFont(context, child);
//                }
                if (child instanceof ViewGroup)
                {
                    setFont(context, (ViewGroup) child);
                }
                else
                {
                    setFont(context, child);
                }
            }
        }
    }
    public static void showFailureToast(Activity activity,String message) {
        final Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);

        //inflate view
        View custom_view = activity.getLayoutInflater().inflate(R.layout.failure_toast_layout, null);

        TextView txtToast= custom_view.findViewById(R.id.toast_text);
        txtToast.setText(message);
//        (custom_view.findViewById(R.id.toast_text)).setVisibility(View.GONE);
//        (custom_view.findViewById(R.id.separator)).setVisibility(View.GONE);
        toast.setView(custom_view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }
    public static void showSuccessToast(Activity activity,String message) {
        final Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);

        //inflate view
        View custom_view = activity.getLayoutInflater().inflate(R.layout.success_toast_layout, null);

        TextView txtToast= custom_view.findViewById(R.id.toast_text);
        txtToast.setText(message);
//        (custom_view.findViewById(R.id.toast_text)).setVisibility(View.GONE);
//        (custom_view.findViewById(R.id.separator)).setVisibility(View.GONE);
        toast.setView(custom_view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }
    public static void setFont(Context context, View view)
    {
        setFont(context, view, Font.SansIran);
    }
    public static void setFont(Context context, View view, Font font)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float size = 0;

        Typeface typeFace = null;
        if(font == Font.SansIran) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile(FaNum).ttf");
            size = (displayMetrics.widthPixels / displayMetrics.density) / 30;
        }
        else if(font == Font.Nazanin) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/BNazanin.ttf");
            size = (displayMetrics.widthPixels / displayMetrics.density) / 23;
        }

        if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            et.setTypeface(typeFace);
            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof CheckBox)
        {
            CheckBox cb = ((CheckBox) view);
            cb.setTypeface(typeFace);
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof Button)
        {
//            size -= size * 50 /100;
            Button b = ((Button) view);
            b.setTypeface(typeFace);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            tv.setTypeface(typeFace);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
//        else if (view instanceof NumberPicker)
//        {
//            NumberPicker np = ((NumberPicker) view);
//            np.setTypeface(typeFace);
//            np.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
//        }
    }

    public static void increaseTextSize(View view, float percent)
    {
        if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            float size = tv.getTextSize();
            size += size * percent/100;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        else if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            float size = et.getTextSize();
            size += size * percent/100;
            et.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }
    public static void setFontBold(View view)
    {
        if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }
        else if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            et.setPaintFlags(et.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }
    }
    public static void setFontUnderline(View view)
    {
        if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            et.setPaintFlags(et.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public static void simpleAlert(Context context, String message)
    {
        simpleAlert(context, message,  DialogIcon.Unknown);
    }
    public static void simpleAlert(Context context, String message, DialogIcon dialogIcon)
    {
        simpleAlert(context, message, "", dialogIcon );
    }
    public static void simpleAlert(Context context, String message, String title, DialogIcon dialogIcon )
    {
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                //dismiss the dialog
            }
        };
        simpleAlert(context, message, title, dialogIcon, clickListener);
    }
    public static void simpleAlert(Context context, String message, String title, DialogIcon dialogIcon,DialogInterface.OnClickListener clickListener )
    {
        simpleAlert(context, message, title, dialogIcon, clickListener, "قبول");
    }
    public static void simpleAlert(Context context, String message, String title, DialogIcon dialogIcon,DialogInterface.OnClickListener clickListener, String btnCaption )
    {
        if(title == "") title = " ";
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(btnCaption, clickListener);
        switch (dialogIcon)
        {
            case Error:
                dlgAlert.setIcon(R.drawable.error128);
                break;
            case Info:
                dlgAlert.setIcon(R.drawable.info128);
                break;
            case Question:
                dlgAlert.setIcon(R.drawable.question128);
                break;
            case Warning:
                dlgAlert.setIcon(R.drawable.warning128);
                break;
        }

        dlgAlert.create().show();
    }
   // @RequiresApi(api = Build.VERSION_CODES.M)
   public static void enableViews(Context context,boolean enable, View... view)
   {
       for(View v : view)
       {
           if(v instanceof EditText)
           {
               EditText et = (EditText)v;
               et.setEnabled(enable);
               Typeface tf = et.getTypeface();
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
               {
                   et.setTextAppearance( enable ? R.style.EditTextStyleNormal1 : R.style.EditTextStyleDisable1);
               }
               et.setBackgroundResource(enable ? R.drawable.edit_text_style_normal1 : R.drawable.edit_text_style_disable1);
               et.setTypeface(tf);
           }
           else if(v instanceof RadioButton)
           {
               v.setEnabled(enable);
           }
           else if(v instanceof Button)
           {
               Button b = (Button) v;
               b.setEnabled(enable);
           }
           else if(v instanceof Spinner)
           {   
               Spinner s = (Spinner) v;
               s.setEnabled(enable);
               s.setBackgroundResource(enable ? R.drawable.edit_text_style_normal1 : R.drawable.edit_text_style_disable1);
           }
           else if(v instanceof MenuItem || v.getClass().getSimpleName().equals("ActionMenuItemView"))
           {
               v.setEnabled(enable);
               continue;
           }
           else if(v instanceof TextView)
           {
               TextView tv = (TextView) v;
               tv.setEnabled(enable);
           }
           setFont(context, v);
       }
   }
    public static void setError(View view, String message)
    {
        if(view instanceof EditText)
        {
            EditText et = (EditText)view;
            et.setError(message);
            et.requestFocus();
        }
    }

//    public static boolean isNationalCodeValid(String nationalCode)
//    {
//        nationalCode = nationalCode.trim();
//        if(nationalCode.length() != 10) return false;
//
//        String[] chArr = nationalCode.split("");
//        int[] intArr = new int[chArr.length - 1];
//
//        int counter = 0;
//        for(int i = chArr.length - 1; i >= 1; i--)
//        {
//            intArr[counter++] = Integer.parseInt( chArr[i]);
//        }
//
//        int sum = 0;
//        for(int i = 1; i < intArr.length; i++)
//        {
//            sum +=  intArr[i] * (i + 1);
//        }
//        int remain = sum % 11;
//
//        if(remain < 2)
//        {
//            return (remain ==  intArr[0]);
//        }
//        else
//        {
//            return (11 - remain) ==  intArr[0];
//        }
//    }
//    public static boolean isEconomicCodeValid(String economicCode)
//    {
//        economicCode = economicCode.trim();
//        if(economicCode.length() != 11) return false;
//
//        String[] chArr = economicCode.split("");
//        int[] intArr = new int[chArr.length - 1];
//
//        int counter = 0;
//        for(int i = chArr.length - 1; i >= 1; i--)
//        {
//            intArr[counter++] = Integer.parseInt( chArr[i]);
//        }
//
//        int check= intArr[1] + 2;
//        int sum = 0;
//        int factor = 0;
//        for(int i = 1; i < intArr.length; i++)
//        {
//            switch (i)
//            {
//                case 1:
//                    factor = 17;
//                    break;
//                case 2:
//                    factor = 19;
//                    break;
//                case 3:
//                    factor = 23;
//                    break;
//                case 4:
//                    factor = 27;
//                    break;
//                case 5:
//                    factor = 29;
//                    break;
//                case 6:
//                    factor = 17;
//                    break;
//                case 7:
//                    factor = 19;
//                    break;
//                case 8:
//                    factor = 23;
//                    break;
//                case 9:
//                    factor = 27;
//                    break;
//                case 10:
//                    factor = 29;
//                    break;
//            }
//            sum +=  (intArr[i] + check) * factor;
//        }
//        int remain = sum % 11;
//        if(remain == 10) remain = 0;
//
//        return (remain ==  intArr[0]);
//    }
    public static boolean isMobileNoValid(String mobileNo)
    {
        mobileNo = mobileNo.trim();
        if(mobileNo.length() != 11) return false;
        return !(!mobileNo.startsWith("09") && !mobileNo.startsWith("۰۹"));
    }

    public static String getFiveLenTime(int hour, int min)
    {
        return String.format("%02d",hour) + ":" + String.format("%02d",min);
    }


    public static void hideKeyboardbyView(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }



    }
    public static void hideKeyboard (Context context )
    {



        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( ((AppCompatActivity)context).findViewById(android.R.id.content).getWindowToken(), 0);
    }
    public static void showKeyboard(Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
    }

    public static void setGridIntervalColor(int position, int selectedPos, View convertView, Context context, String orderType)
    {
        if (position == selectedPos && orderType.equalsIgnoreCase("S"))
        {
//            convertView.setBackgroundResource( R.drawable.list_item_back_selected);
            convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.light_blue2, null));
        }
        else if (position == selectedPos && !orderType.equalsIgnoreCase("S"))
        {
            convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightOrange2, null));

//            if (position % 2 == 0)
//            {
//                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightYellow, null));
//            } else
//            {
//                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.White, null));
//            }

//            convertView.setBackgroundResource( R.drawable.list_item_back_normal);
        }else {
            if(orderType.equalsIgnoreCase("S")){
                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.light_blue1, null));

            }else {
                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightOrange1, null));
            }
        }
    }

    public static void setGridIntervalColor(int position, int selectedPos, View convertView, Context context)
    {
        if (position == selectedPos)
        {
//            convertView.setBackgroundResource( R.drawable.list_item_back_selected);
            convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightOrange2, null));
        }
        else
        {
//            if (position % 2 == 0)
//            {
//                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightYellow, null));
//            } else
//            {
//                convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.White, null));
//            }

//            convertView.setBackgroundResource( R.drawable.list_item_back_normal);
            convertView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.LightOrange1, null));
        }
    }
    public static void setGridIntervalColor2(int position, int selectedPos, View convertView, Context context)
    {
        if (position == selectedPos)
        {
            convertView.setBackgroundResource( R.drawable.list_item_back_selected);
        }
        else
        {
            convertView.setBackgroundResource( R.drawable.list_item_back_normal);
        }
    }

    public static boolean generalErrorOccurred(TaskResult taskResult, Context context)
    {

        if(CheckMaintenanceMode(taskResult, context)) return true;
        if (new AppVersion(taskResult, context).isNewVersionAvailable()) return true;
        if ( failedToConnect(taskResult, context)) return true;
//        if(controlCharge) return chargeIsExpired(taskResult, context, noChargeMessage);
        return false;
    }
    private static boolean CheckMaintenanceMode(TaskResult taskResult, Context context)
    {
        if(!taskResult.isSuccessful && taskResult.isExceptionOccured("Maintenance_Mode") )
        {
            Utility.simpleAlert(context,"با عرض پوزش امكان استفاده از سيستم در حال حاضر وجود ندارد چون در حال بروز رساني سيستم مرکزی مي باشيم." + "\n" + "لطفاً كمي صبر كنيد، خيلي زود بر مي گرديم."
                    ,"", DialogIcon.Warning);
            return true;
        }
        return false;
    }

    private static boolean failedToConnect(TaskResult taskResult, Context context)
    {
        if(!taskResult.isSuccessful &&
                (taskResult.isExceptionOccured("failed to connect to") ||
                        taskResult.isExceptionOccured("Unable to resolve host"))
                )
        {
            Utility.simpleAlert(context,"خطا در ارتباط با سرور مرکزی." + "\n" + "اطمینان حاصل نمایید که تبلت/موبایل به درستی به اینترنت متصل باشد."
                    ,"خطا", DialogIcon.Error);
            return true;
        }
        return false;
    }


    public static String getDeviceInfo()
    {
        return "Manufacturer:" + Build.MANUFACTURER + ",Model:" + Build.MODEL + ",SDK_Int:" + Build.VERSION.SDK_INT;

    }
    private static boolean checkValidChar(String s)
    {
        String num = "1234567890";
        String lowerChar = "abcdefghijklmnopqrstuvwxyz";
        String upperChar = lowerChar.toUpperCase();
        String validChars = num + lowerChar + upperChar;

        char[] arr = s.toCharArray();
        for(int i = 0; i < arr.length; i ++)
        {
            boolean result = validChars.contains( String.valueOf( arr[i]));
            if(!result) return false;
        }
        return true;
    }
//    public static boolean incorrectCharInPassword(EditText etPassword)
//    {
//        if (!Utility.checkValidChar(etPassword.getText().toString()) )
//        {
//            etPassword.setError("رمز عبور فقط می تواند شامل اعداد و حروف کوچک و بزرگ انگلیسی باشد." +
//                    "\n" + "صفحه کلید خود را در حالت انگلیسی قرار دهید.");
//            etPassword.requestFocus();
//            etPassword.selectAll();
//            return true;
//        }
//        return false;
//    }
    public static void setListCount(int count, TextView tv)
    {
        tv.setText("تعداد: " + count);
    }

//    public static GlobalData getGlobalData(Context context)
//    {
       //return (GlobalData) ((BaseActivity)context).getApplication();
//    }
    public static boolean restartAppIfNeed(final Activity activity)
    {
        if(GlobalData.getUserName() == null || GlobalData.getUserName().equals(""))
        {
            Intent i = activity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            activity.finishAffinity();

            return true;

        }
        return false;
    }


    public static boolean compareTwoDates(Context context,DateFragment dateFrom, DateFragment dateTo)
    {
        String date1 = dateFrom.getShortDate();
        String date2 = dateTo.getShortDate();
        if(date1.compareTo(date2) > 0)
        {
            Utility.simpleAlert(context,"تاريخ اول نمي تواند از تاريخ دوم بزرگتر باشد." , DialogIcon.Warning);
            return false;
        }
        return true;
    }
    public static boolean haveStoragePermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
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
    public static void initPickingLineItemFilterSpinner(Context context, Spinner spinner)
    {
        ArrayList<PickingLineItemFilter> list = new ArrayList<>();
        list.add( PickingLineItemFilter.All);
        list.add( PickingLineItemFilter.NonDetermined);
        list.add( PickingLineItemFilter.NotEnoughInventory);
        list.add( PickingLineItemFilter.DeliverLessThanInventory);
        row_picking_line_item_filter_spinner adapter = new row_picking_line_item_filter_spinner(context, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
    public static void initPickingControlItemFilterSpinner(Context context, Spinner spinner)
    {
        ArrayList<PickingLineItemFilter> list = new ArrayList<>();
        list.add( PickingLineItemFilter.All);
        list.add( PickingLineItemFilter.NotFinalControlled);
        row_picking_line_item_filter_spinner adapter = new row_picking_line_item_filter_spinner(context, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
    public static Typeface getTypeFace(Context context,Font font)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float size = 0;

        Typeface typeFace = null;
        if(font == Font.SansIran) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile(FaNum).ttf");
        }
        else if(font == Font.Nazanin) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/BNazanin.ttf");
        }
        return typeFace;
    }

    @SuppressLint("DefaultLocale")
    public static String getCurrentPersianDate()
    {
        PersianCalendar persianCalendar = new PersianCalendar();
        return getPersianShortDate(persianCalendar);
    }
    @SuppressLint("DefaultLocale")
    public static String getCurrentPersianShortDate()
    {
        PersianCalendar persianCalendar = new PersianCalendar();
        return getCurrentPersianShortDate(persianCalendar);
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
    public static String getCurrentPersianShortDate(PersianCalendar persianCalendar)
    {
        int year = persianCalendar.getPersianYear();
        int month = persianCalendar.getPersianMonth();
        int day = persianCalendar.getPersianDay();

        return String.format("%04d", year) +"/"+ String.format("%02d", month) +"/"+ String.format("%02d", day) ;
    }
    public static boolean isPowerUser()
    {
        return GlobalData.getUserName().toLowerCase().equals("mafarahani") ||
                GlobalData.getUserName().toLowerCase().equals("mabeygifar") ||
                 GlobalData.getUserName().toLowerCase().equals("sotaheri") ||
                 GlobalData.getUserName().toLowerCase().equals("anoorafkan") ||
                GlobalData.getUserName().toLowerCase().equals("apourmand");
    }
    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4)
    {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String getMACAddress(Context context) {
        /* NOT WORK IN API 30+
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
         */

        /* SECOND WAY
        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
         */
        DBHandler db = new DBHandler(context);
        String deviceID = db.getParamValue(Param.DeviceID);
        if(deviceID.equals(""))
        {
            deviceID = UUID.randomUUID().toString().toUpperCase();
            db.setParamValue(Param.DeviceID, deviceID);
        }
        return deviceID;
    }
    private static long lastClickTime = 0;
    public static boolean isDoubleClick()
    {
        return isDoubleClick(1000);
    }
    /**
     * @param interval Interval between clicks(touches) in milliseconds
     * @return
     */
    public static boolean isDoubleClick(int interval)
    {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval){
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
    public static void clearGeneralSpinner(Context context, Spinner spinner)
    {
        List<String> list = new ArrayList<>();
        list.add("");
        row_general_spinner adapter = new row_general_spinner(context, list);
        spinner.setAdapter(adapter);
    }
    public static String removeFakeZeroFromDouble(double value)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }
    public static UnitValue getNewUnitAndValue(String oldUnit, double oldValue
            , String selectedUnit, double selectedValue, int boxUnit)
    {
        UnitValue newUnitValue = new UnitValue();
        if ((oldUnit.equals("CAR") && selectedUnit.equals("CAR")) ||
                (oldUnit.equals("KG") && selectedUnit.equals("KG")))
        {
            newUnitValue.setValue( selectedValue + oldValue);
            newUnitValue.setUnit( selectedUnit);
        }
        else if ((oldUnit.equals("CAR") && selectedUnit.equals("PC")) ||
                (oldUnit.equals("KG") && selectedUnit.equals("G")))
        {
            newUnitValue.setValue( selectedValue + (oldValue * boxUnit));
            newUnitValue.setUnit( selectedUnit);
        }
        else if ((oldUnit.equals("PC") && selectedUnit.equals("CAR")) ||
                (oldUnit.equals("G") && selectedUnit.equals("KG")))
        {
            newUnitValue.setValue( (selectedValue * boxUnit) + oldValue);
            newUnitValue.setUnit( oldUnit);
        }
        else if ((oldUnit.equals("PC") && selectedUnit.equals("PC")) ||
                (oldUnit.equals("G") && selectedUnit.equals("G")))
        {
            newUnitValue.setValue( selectedValue + oldValue);
            newUnitValue.setUnit( selectedUnit);
        }
        return newUnitValue;
    }
    public static void initReturnReasonSpinner(Context context, Spinner sReturnReason)
    {
        ArrayList<StoreReturnItemReason> list = new ArrayList<>();
        list.add(StoreReturnItemReason.Unknown);
        list.add(StoreReturnItemReason.R001);
        list.add(StoreReturnItemReason.R002);
        list.add(StoreReturnItemReason.R003);
        list.add(StoreReturnItemReason.R004);
        list.add(StoreReturnItemReason.R005);
        list.add(StoreReturnItemReason.R006);
        list.add(StoreReturnItemReason.R007);
        list.add(StoreReturnItemReason.R008);
        list.add(StoreReturnItemReason.R009);

        row_return_store_product_spinner adapter = new row_return_store_product_spinner(context, list);
        sReturnReason.setAdapter(adapter);
        sReturnReason.setSelection(0);
    }
    public static void fastHelp(Context context, String title, String comment)
    {
        fastHelp(context, title, comment, R.drawable.question2);
    }
    public static void makeHyperlinkText(Activity context, String text, View view)
    {
        SpannableString ss = new SpannableString(text);
        String[] words = ss.toString().split(" |\n");
        for(final String word : words)
        {
            if(word.contains("www"))
            {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
//                        Utility.openDokkanSite( context);
                    }
                };
                ss.setSpan(clickableSpan, ss.toString().indexOf(word),
                        ss.toString().indexOf(word) + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(word.contains("instagram.com"))
            {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
//                        Utility.openInstagramPage(context);
                    }
                };
                ss.setSpan(clickableSpan, ss.toString().indexOf(word),
                        ss.toString().indexOf(word) + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            et.setText(ss);
            et.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            tv.setText(ss);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    public static void fastHelp(Context context, String title, String comment, int imageID)
    {
        if(isDoubleClick()) return;
        Intent intent = new Intent(context, FastHelpActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("comment", comment);
        intent.putExtra("imageID", imageID);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public static void addParam(ArrayList<PropertyInfo> piList, String name, Object value)
    {
        PropertyInfo pi;
        pi = new PropertyInfo();
        pi.setName(name);
        pi.setValue(value);
        piList.add(pi);
    }
    public static void playBeep()
    {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
    }
    public static void playShortBeep()
    {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP,50);
    }
    public void setKeyboardConfirmAction(EditText et, View.OnClickListener onClickListener)
    {
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT))
                {
                    onClickListener.onClick(v);
                }
                return false;
            }
        };
        et.setOnEditorActionListener(editorActionListener);
    }
}
