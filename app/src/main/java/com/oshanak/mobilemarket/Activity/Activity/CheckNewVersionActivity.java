package com.oshanak.mobilemarket.Activity.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.core.content.FileProvider;


import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Operation_Updater_API;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Pilot_Updater_API;
import com.oshanak.mobilemarket.BuildConfig;
import com.oshanak.mobilemarket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNewVersionActivity extends BaseActivity {

//    private RadioButton rDokkanUpdate;
//    private RadioButton rDokkanDownload;
//    private RadioButton rBazaar;
//    private RadioGroup rgSource;
    Call call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_version);
        ///////////////////
//        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

//        rDokkanUpdate = findViewById(R.id.rDokkanUpdate);
//        rDokkanDownload = findViewById(R.id.rDokkanDownload);
//        rBazaar = findViewById(R.id.rBazaar);
//
//        rgSource = findViewById(R.id.rgSource);
//        rgSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                LinearLayout lProgress = findViewById(R.id.lProgress);
//                if(checkedId == rDokkanUpdate.getId())
//                {
//                    lProgress.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    lProgress.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        rDokkanUpdate.setChecked(true);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            TextView tvPercent = findViewById(R.id.tvPercent);
            Utility.increaseTextSize(tvPercent,30);
            TextView tvDownloadAlarm = findViewById(R.id.tvDownloadAlarm);
            Utility.increaseTextSize(tvDownloadAlarm,-10);
        }
    }
    private boolean canceled = false;
    public void onCancelClick(View view)
    {
        onBackPressed();
    }
    public void onConfirmClick(View view)
    {

//        if(GlobalData.getAppMarket() == AppMarket.Bazaar || rBazaar.isChecked())
//        {
//            getVersionFromBazaar();
//        }
//        else if(rDokkanUpdate.isChecked())
//        {
            if(Utility.haveStoragePermission(this))
            {
//                rBazaar.setEnabled(false);
//                rDokkanUpdate.setEnabled(false);
//                rDokkanDownload.setEnabled(false);
                Button bConfirm = findViewById(R.id.bConfirm);
                Utility.enableViews(this,false, bConfirm);
                TextView tvDownloadAlarm = findViewById(R.id.tvDownloadAlarm);
                tvDownloadAlarm.setVisibility(View.VISIBLE);
                GetAPK(this);
            }
//        }
//        else if(rDokkanDownload.isChecked())
//        {
//            Utility.openDokkanSite(this);
//        }

    }


    @Override
    public void onBackPressed()
    {
        if(manager != null) {

            canceled = true;
            manager.remove(downloadId);
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

//    private void getVersionFromBazaar()
//    {
//        //String packageName = "com.parsasoftsystem.dokkan";
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("bazaar://details?id=" + "com.parsasoftsystem.dokkan"));
//            intent.setPackage("com.farsitel.bazaar");
//            startActivity(intent);
//        }catch (ActivityNotFoundException e)
//        {
//            Utility.simpleAlert(this,"دریافت نسخه جدید انجام نشد." + "\n" +
//                            "اطمینان حاصل کنید که آخرين نسخه نرم افزار کافه بازار بر گوشی شما نصب است و به اینترنت متصل می باشید."
//                    ,"خطا", DialogIcon.Error);
//        }
//        catch (Exception e)
//        {
//            Utility.simpleAlert(this,"دریافت نسخه جدید انجام نشد." +
//                            "خطای نامشخص."
//                    ,"خطا", DialogIcon.Error);
//        }
//    };
    private static String apkUrl(Context context)
    {
        return new Common(context).URL() + "App/haft.apk";
    }
    private static String apk = "haft.apk";

    private long downloadId = -1;
    private DownloadManager manager;



    //Todo این قسمت بعد از اصلاح آپدیتر باید حذف شود


//    public void GetAPK(final Context context)
//    {
//
//        try {
//            String destination = "";
//            if (Build.VERSION.SDK_INT >= 24)
//            {
//                destination = context.getExternalFilesDir(null).getAbsolutePath() + "/version/";
//            }
//            else {
//                destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//            }
//            destination += apk;
//            final Uri uri = Uri.parse("file://" + destination);
//            final File file = new File(destination);
//
//            if (file.exists()) {
//                file.delete();
//            }
//
//            //get url of app on server
//            String url = apkUrl(context);//Main.this.getString(R.string.update_app_url);
//
//            //set downloadmanager
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//            request.setDescription("دانلود نسخه جدید...");
//            request.setTitle("دکان");
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
////            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
////                    | DownloadManager.Request.NETWORK_MOBILE);
//
//            //set destination
//            request.setDestinationUri(uri);
//
//            if(Utility.applicationMode == ApplicationMode.Competitor)
//            {
//                request.addRequestHeader("Authorization", "Basic " +
//                        org.kobjects.base64.Base64.encode(GlobalData.getBasicAuthorization().getBytes()));
//            }
//
//            // get download service and enqueue file
//            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//            //        try
//            //        {
//            downloadId = manager.enqueue(request);
//
//        }
//        catch (Exception e)
//        {
//            throw e;
//        }
//        //region progress
//        final int UPDATE_PROGRESS = 5020;
//        final boolean[] progressDone = {false};
//
//        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg)
//            {
//                try {
//                    if (msg.what == UPDATE_PROGRESS && !progressDone[0]) {
//                        String downloaded = String.format("%.2f MB", (double) ((msg.arg1) / 1024) / 1024);
//                        String total = String.format("%.2f MB", (double) ((msg.arg2) / 1024) / 1024);
//                        String status = downloaded + " / " + total;
////                    Toast.makeText(context, downloaded + " / " + total,Toast.LENGTH_SHORT).show();
//                        TextView tvPercent = ((Activity) context).findViewById(R.id.tvPercent);
//                        ProgressBar progressBar = ((Activity) context).findViewById(R.id.progressBar);
//
//                        double percent = ((double) ((msg.arg1) / 1024) / 1024) * 100 / ((double) ((msg.arg2) / 1024) / 1024);
//                        if (percent >= 100) progressDone[0] = true;
//                        progressBar.setProgress((int) percent);
//                        tvPercent.setText((int) percent + " %");
//                    }
//                    super.handleMessage(msg);
//                }
//                catch (Exception e)
//                {
//                    throw e;
//                }
//            }
//        };
//        new Thread(new Runnable() {
//            @Override
//            public void run()
//            {
//                try {
//
//                    if (progressDone[0]) return;
//                    boolean downloading = true;
//                    while (downloading && !progressDone[0] && manager != null) {
//                        DownloadManager.Query q = new DownloadManager.Query();
//                        q.setFilterById(downloadId);
//                        Cursor cursor = manager.query(q);
//                        if(cursor.getCount() <= 0)
//                        {
//                            return;
//                        }
//                        cursor.moveToFirst();
//                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
//                            downloading = false;
//                        }
//                        //Post message to UI Thread
//                        Message msg = handler.obtainMessage();
//                        msg.what = UPDATE_PROGRESS;
//                        //msg.obj = statusMessage(cursor);
//                        msg.arg1 = bytes_downloaded;
//                        msg.arg2 = bytes_total;
//                        handler.sendMessage(msg);
//                        cursor.close();
//                    }
//                }
//                catch (CursorIndexOutOfBoundsException e)
//                {
//                    throw e;
//                }
//                catch (Exception e)
//                {
//                    throw e;
//                }
//            }
//        }).start();
//        //endregion progress
//
//        //set BroadcastReceiver to install app when .apk is downloaded
//        BroadcastReceiver onComplete = new BroadcastReceiver()
//        {
//            public void onReceive(Context ctxt, Intent intent)
//            {
//
//                try {
//
////                    ((BaseActivity) ctxt).stopWait();
//                    Uri uri;
//                    String authority;
//                    String pathName;
//                    if (Build.VERSION.SDK_INT >= 24)
//                    {
//                        authority = BuildConfig.APPLICATION_ID + ".provider";
////                        pathName = Environment.getExternalStorageDirectory() + "/download/" + apk;
//                        pathName = context.getExternalFilesDir(null).getAbsolutePath() + "/version/" + apk;
//
//                        uri = FileProvider.getUriForFile(ctxt, authority, new File(pathName));
//                    }
//                    else
//                    {
//                        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//                        destination += apk;
//                        uri = Uri.parse("file://" + destination);
//                    }
//
//                    if(canceled) return;
//                    Intent install = new Intent(Intent.ACTION_VIEW);
//                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    install.setDataAndType(uri,/* manager.getMimeTypeForDownloadedFile(downloadId)*/ "application/vnd.android.package-archive");
//
//                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    ctxt.startActivity(install);
//
//                    ctxt.unregisterReceiver(this);
////                    ((Activity) ctxt).onBackPressed();
//
//                }
//                catch(Exception e)
//                {
//                    String S=e.getMessage();
//                    Utility.simpleAlert(ctxt, S);
//                }
//
//            }
//        };
//        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));//
//
//    }
//    public void onMarketHelpClick(View view)
//    {
//        String comment =
//                "در صورتي كه نسخه جديد اپليكيشن را از سايت دُكان دريافت نماييد (يكي از دو روش اول)، به ازاي هر يك ماه خريد اعتبار 5 روز اعتبار هديه مطابق زير به شما تعلق مي گيرد." + "\n" +
//                        "1. خريد 1 ماهه: 5 روز هديه" + "\n" +
//                        "2. خريد 3 ماهه: 15 روز هديه" + "\n" +
//                        "3. خريد 6 ماهه: 30 روز هديه" + "\n" +
//                        "4. خريد يك ساله: 60 روز هديه" + "\n" +
//                        "به عبارتي اگر اپليكيشن را از سايت اختصاصي دُكان درياف كرده باشيد، و قصد خريد اعتبار يكساله داريد، اعتبار شما بجاي 12 ماه 14 ماه شارژ خواهد شد.";
//
//        Utility.fastHelp(this, "محل دريافت نسخه", comment);
//    }

    //Todo این قسمت بعد از اصلاح آپدیتر باید حذف شود


    public void GetAPK(final Context context)
    {


        try {
            String destination = "";
            if (Build.VERSION.SDK_INT >= 24) {
                destination = context.getExternalFilesDir(null).getAbsolutePath() + "/version/";
            } else {
                destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
            }
            destination += apk;

            final Uri uri = Uri.parse("file://" + destination);
            final File file = new File(destination);

//            if (file.exists()) {
//                file.delete();
//            }

            //get url of app on server
//            String url = apkUrl(context);//Main.this.getString(R.string.update_app_url);
//            String url = "https://dl.apktops.ir/games/2022/09/Save_the_Masters_v1.1.2_Apktops.ir.apk";
            String url = apkUrl(CheckNewVersionActivity.this);//Main.this.getString(R.string.update_app_url);

            //set downloadmanager
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//            request.setDescription("دانلود نسخه جدید...");
//            request.setTitle("شرکت اوشانک");
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
//                    | DownloadManager.Request.NETWORK_MOBILE);


            /////////////download with retrofit started here/////////////////////
            /////////////download with retrofit/////////////////////
            Common c = new Common(this);
            String s = c.URL();
            ApiInterface request;




            if (s.contains("pilot")) {
                request = Pilot_Updater_API.getAPI().create(ApiInterface.class);
            } else {
                request = Operation_Updater_API.getAPI().create(ApiInterface.class);
            }



            call = request.downlload(url);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    TextView tvPercent = ((Activity) context).findViewById(R.id.tvPercent);
                    ProgressBar progressBar = ((Activity) context).findViewById(R.id.progressBar);
//                    if (response.code()==200) {
//                        progressBar.setProgress(50);
//                        tvPercent.setText("downloading");
//                        try {
//                            Log.d("testurldownload00", response.body().byteStream().toString());
////
//                            String destination = "";
//                            if (Build.VERSION.SDK_INT >= 24) {
//                                destination = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/version/";
//                            } else {
//                                destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//                            }
//                            destination += apk;
//                            Log.d("checkproblem22", destination + "");
//                            final Uri uri = Uri.parse("file://" + destination);
//                            final File file = new File(destination);
//
//                            if (file.exists()) {
//                                file.delete();
//                            }
//
//
//                            File path = Environment.getExternalStorageDirectory();
////                    File file = new File(path, "vysor.apk");
//                            FileOutputStream fileOutputStream = new FileOutputStream(destination);
//                            try {
////                        fileOutputStream.write(response.body().bytes());
//                                Log.d("checkproblem111", file.getAbsolutePath() + "");
////                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//
//                                fileOutputStream.write(response.body().bytes());
//
//
//                                Log.d("checkproblem330", (int) file.getAbsoluteFile().length() + "");
//
//                                Uri uri2;
//                                String authority;
//                                String pathName;
//
//                                if (Build.VERSION.SDK_INT >= 24) {
//                                    authority = BuildConfig.APPLICATION_ID + ".provider";
////                        pathName = Environment.getExternalStorageDirectory() + "/download/" + apk;
//                                    pathName = context.getExternalFilesDir(null).getAbsolutePath() + "/version/" + apk;
//
//                                    uri2 = FileProvider.getUriForFile(context, authority, new File(pathName));
//                                } else {
////                                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//                                    destination += apk;
//
//                                    uri2 = Uri.parse("file://" + destination);
//                                }
//
//
//                                try {
//                                    Intent install = new Intent(Intent.ACTION_VIEW);
//                                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    install.setDataAndType(uri2,/* manager.getMimeTypeForDownloadedFile(downloadId)*/ "application/vnd.android.package-archive");
//
//                                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    AppDownloadActivity.this.startActivity(install);
//
//                                } catch (Exception e) {
//                                    Log.d("checkproblem333", e.getMessage());
//                                }
//
//
//                                ////////////progress shows here /////////
//
//                                final int UPDATE_PROGRESS = 5020;
//                                final boolean[] progressDone = {false};
//
//                                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
//                                    @Override
//                                    public void handleMessage(Message msg) {
//                                        try {
////                                        if (msg.what == UPDATE_PROGRESS && !progressDone[0]) {
//                                            String downloaded = String.format("%.2f MB", (double) ((msg.arg1) / 1024) / 1024);
//                                            String total = String.format("%.2f MB", (double) ((msg.arg2) / 1024) / 1024);
//                                            String status = downloaded + " / " + total;
////                    Toast.makeText(context, downloaded + " / " + total,Toast.LENGTH_SHORT).show();
//                                            TextView tvPercent = ((Activity) context).findViewById(R.id.tvPercent);
//                                            ProgressBar progressBar = ((Activity) context).findViewById(R.id.progressBar);
//
//                                            double percent = ((double) ((msg.arg1) / 1024) / 1024) * 100 / ((double) ((msg.arg2) / 1024) / 1024);
//                                            if (percent >= 100) progressDone[0] = true;
//                                            progressBar.setProgress((int) percent);
//                                            tvPercent.setText((int) percent + " %");
////                                        }
//                                            super.handleMessage(msg);
//                                        } catch (Exception e) {
//                                            Toast.makeText(context, e.getMessage().toString() + "", Toast.LENGTH_SHORT).show();
//
//                                            throw e;
//                                        }
//                                    }
//                                };
//                                new Thread(new Runnable() {
//                                    @SuppressLint("Range")
//                                    @Override
//                                    public void run() {
//                                        try {
//
//                                            if (progressDone[0]) return;
//                                            boolean downloading = true;
//                                            while (downloading && !progressDone[0] && manager != null) {
//                                                DownloadManager.Query q = new DownloadManager.Query();
//                                                q.setFilterById(downloadId);
//                                                Cursor cursor = manager.query(q);
//                                                if (cursor.getCount() <= 0) {
//                                                    return;
//                                                }
//                                                cursor.moveToFirst();
////                                            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                                                int bytes_downloaded = (int) file.length();
////                                            @SuppressLint("Range") int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                                                @SuppressLint("Range") int bytes_total = response.body().bytes().length;
////                                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
////                                                downloading = false;
////                                            }
//                                                //Post message to UI Thread
//                                                Message msg = handler.obtainMessage();
//                                                msg.what = UPDATE_PROGRESS;
//                                                //msg.obj = statusMessage(cursor);
//                                                msg.arg1 = bytes_downloaded;
//                                                msg.arg2 = bytes_total;
//                                                handler.sendMessage(msg);
//                                                cursor.close();
//                                            }
//                                        } catch (CursorIndexOutOfBoundsException e) {
//                                            Toast.makeText(context, e.getMessage().toString() + "", Toast.LENGTH_SHORT).show();
//
//                                            throw e;
//                                        } catch (Exception e) {
//                                            Toast.makeText(context, e.getMessage().toString() + "", Toast.LENGTH_SHORT).show();
//
//                                            try {
//                                                throw e;
//                                            } catch (IOException ex) {
//                                                throw new RuntimeException(ex);
//                                            }
//                                        }
//                                    }
//                                }).start();
//                                //endregion progress
//
//                                //set BroadcastReceiver to install app when .apk is downloaded
//                                BroadcastReceiver onComplete = new BroadcastReceiver() {
//                                    public void onReceive(Context ctxt, Intent intent) {
//
//                                        try {
//
////                    ((BaseActivity) ctxt).stopWait();
//                                            Uri uri;
//                                            String authority;
//                                            String pathName;
//                                            if (Build.VERSION.SDK_INT >= 24) {
//                                                authority = BuildConfig.APPLICATION_ID + ".provider";
////                        pathName = Environment.getExternalStorageDirectory() + "/download/" + apk;
//                                                pathName = context.getExternalFilesDir(null).getAbsolutePath() + "/version/" + apk;
//
//                                                uri = FileProvider.getUriForFile(ctxt, authority, new File(pathName));
//                                            } else {
//                                                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//                                                destination += apk;
//
//                                                uri = Uri.parse("file://" + destination);
//                                            }
//
//                                            if (canceled) return;
//                                            Intent install = new Intent(Intent.ACTION_VIEW);
//                                            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                            install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            install.setDataAndType(uri,/* manager.getMimeTypeForDownloadedFile(downloadId)*/ "application/vnd.android.package-archive");
//
//                                            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                            ctxt.startActivity(install);
//
//                                            ctxt.unregisterReceiver(this);
////                    ((Activity) ctxt).onBackPressed();
//
//                                        } catch (Exception e) {
//                                            String S = e.getMessage();
//
//
//                                            Toast.makeText(ctxt, S, Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//                                };
//                                context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));//
//
//
//                                ////////////progress ends here /////////
//                                fileOutputStream.close();
//
//                            } catch (Exception e) {
//                                Log.d("checkproblem222", e.getMessage() + "");
//                            }
////                    IOUtils.write(response.body().bytes(), fileOutputStream);
//                        } catch (Exception ex) {
//                            Log.d("testurldownload0", ex.getMessage());
//                        }
//                    }else{
//                        progressBar.setProgress(0);
//                        tvPercent.setText("Failed");
//                    }


                    if (response.isSuccessful()) {
                        new AsyncTask<Void, Long, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                try {
                                    ResponseBody body = response.body();
                                    long fileSize = body.contentLength();
                                    long fileSizeDownloaded = 0;
                                    String destination = "";
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        destination = context.getExternalFilesDir(null).getAbsolutePath() + "/version/";
                                        File file1=new File(destination);
                                        file1.mkdir();
                                    } else {
                                        destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                                    }
                                    InputStream inputStream = body.byteStream();
                                    FileOutputStream outputStream = new FileOutputStream(destination + "/" + apk);

                                    byte[] buffer = new byte[4096];
                                    int read;

                                    while ((read = inputStream.read(buffer)) != -1) {
                                        fileSizeDownloaded += read;
                                        outputStream.write(buffer, 0, read);
                                        publishProgress(fileSizeDownloaded, fileSize);
                                    }

                                    outputStream.flush();
                                    outputStream.close();
                                    inputStream.close();

                                    return true;
                                } catch (IOException e) {

                                    return false;
                                }
                            }

                            @Override
                            protected void onProgressUpdate(Long... values) {
                                super.onProgressUpdate(values);
                                long fileSizeDownloaded = values[0];
                                long fileSize = values[1];
                                int progress = (int) ((fileSizeDownloaded * 100) / fileSize);

                                progressBar.setProgress(progress );
                                tvPercent.setText(progress+"%");
                            }

                            @Override
                            protected void onPostExecute(Boolean success) {
                                super.onPostExecute(success);
                                if (success) {


                                    Uri uri2;
                                    String authority;
                                    String pathName;

                                    if (Build.VERSION.SDK_INT >= 24) {
                                        authority = BuildConfig.APPLICATION_ID + ".provider";
//                        pathName = Environment.getExternalStorageDirectory() + "/download/" + apk;
                                        pathName = context.getExternalFilesDir(null).getAbsolutePath() + "/version/" + apk;

                                        uri2 = FileProvider.getUriForFile(context, authority, new File(pathName));
                                    } else {
                                        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                                        destination += apk;

                                        uri2 = Uri.parse("file://" + destination);
                                    }


                                    try {
                                        Intent install = new Intent(Intent.ACTION_VIEW);
                                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        install.setDataAndType(uri2,/* manager.getMimeTypeForDownloadedFile(downloadId)*/ "application/vnd.android.package-archive");

                                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        CheckNewVersionActivity.this.startActivity(install);

                                    } catch (Exception e) {

                                    }



                                } else {

                                }
                            }
                        }.execute();
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }

            });


            /////////////download with retrofit finished here/////////////////////
            /////////////download with retrofit/////////////////////


            //set destination
//            request.setDestinationUri(uri);
//
//            Log.d("checkproblem3",uri+"\n"+url);
//
//            // get download service and enqueue file
//            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//          /*          try
//                    {*/
//            final String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", "MobileService", "Aa123456").getBytes(), Base64.DEFAULT);
//
//            request.addRequestHeader("Authorization", basicAuth);
//
//            Log.d("checkproblem4",basicAuth);
//
//
//            downloadId = manager.enqueue(request);


            //region progress


//    public void onMarketHelpClick(View view)
//    {
//        String comment =
//                "در صورتي كه نسخه جديد اپليكيشن را از سايت دُكان دريافت نماييد (يكي از دو روش اول)، به ازاي هر يك ماه خريد اعتبار 5 روز اعتبار هديه مطابق زير به شما تعلق مي گيرد." + "\n" +
//                        "1. خريد 1 ماهه: 5 روز هديه" + "\n" +
//                        "2. خريد 3 ماهه: 15 روز هديه" + "\n" +
//                        "3. خريد 6 ماهه: 30 روز هديه" + "\n" +
//                        "4. خريد يك ساله: 60 روز هديه" + "\n" +
//                        "به عبارتي اگر اپليكيشن را از سايت اختصاصي دُكان درياف كرده باشيد، و قصد خريد اعتبار يكساله داريد، اعتبار شما بجاي 12 ماه 14 ماه شارژ خواهد شد.";
//
//        Utility.fastHelp(this, "محل دريافت نسخه", comment);
//    }


            //How To Call
//    public void download(String __url){
//
//    }


        } finally {

        }
    }

}