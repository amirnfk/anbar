package com.oshanak.mobilemarket.Activity.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Models.DocsListModel;
import com.oshanak.mobilemarket.Activity.Models.GetDocListModel;
import com.oshanak.mobilemarket.Activity.Models.metaData;
import com.oshanak.mobilemarket.Activity.RowAdapter.Docs_Adapter;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.R;

import net.time4j.PlainDate;
import net.time4j.android.ApplicationStarter;
import net.time4j.calendar.PersianCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.Iso8601Format;
import net.time4j.format.expert.PatternType;

import org.jetbrains.annotations.NotNull;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;



import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UploadDocsActivity extends AppCompatActivity {
    RecyclerView rc_view_uploaded_docs_list;
    ArrayList<Uploaded_Docs_Model> uploaded_documents_list;
    ArrayList<Uploaded_Docs_Model> filtered_uploaded_documents_list;
    Docs_Adapter mAdapter;
    ProgressBar prg_docs_list;
    ExtendedFloatingActionButton fab_doc_send;
    ImageView imgMenue;
Toolbar toolbar;
    LinearLayout lyt_from_date;
    LinearLayout lyt_sardarb_help_icon;
    LinearLayout lyt_to_date;
    private DrawerLayout drawer;
    TextView txt_from_date;
    TextView txt_to_date;
    static long time_stamp_from;
    static long time_stamp_to;
    TextView btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_docs);
        ApplicationStarter.initialize(this, true); // with prefetch on background thread

        setUp();

        try {
            getMyDocs();
        } catch (Exception e) {
            showErrorToast("لیست اسناد دریافت نشد");
        }

        fab_doc_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadDocsActivity.this, New_Document_Upload_Activity.class);
                startActivity(intent);
            }
        });
        imgMenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);

            }
        });
        lyt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(UploadDocsActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(UploadDocsActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                                txt_from_date.setText(persianPickerDate.getPersianYear() + "/" + persianPickerDate.getPersianMonth() + "/" + persianPickerDate.getPersianDay());
                                time_stamp_from = persianPickerDate.getTimestamp();

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        lyt_sardarb_help_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadDocsActivity.this, Activity_Tablo_Sardarb_help.class);
                startActivity(intent);
            }
        });
        lyt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(UploadDocsActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(UploadDocsActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                                txt_to_date.setText(persianPickerDate.getPersianYear() + "/" + persianPickerDate.getPersianMonth() + "/" + persianPickerDate.getPersianDay());
                                time_stamp_to = persianPickerDate.getTimestamp();

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                filterDocsByDate(uploaded_documents_list);
                } catch ( Exception e) {
                    showErrorToast("اطلاعات به درستی وارد نشده است");
                }
            }
        });
    }
    public static String convertDateStringToTimestamp(String dateString) {
        try {
            // Parse the input date string
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputDateFormat.parse(dateString);

            // Convert the Date object to a timestamp in milliseconds
            long timestamp = date.getTime();

            // Format the timestamp as a string
            return String.valueOf(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String convertTimestampToStartOfDay(String timestampString) {
        try {
            // Convert timestamp string to long
            long timestamp = Long.parseLong(timestampString);

            // Create Date object from timestamp
            Date date = new Date(timestamp);

            // Set time to midnight (00:00:00)
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);

            // Get timestamp for the start of the day
            long startOfDayTimestamp = date.getTime();

            // Return the start of the day timestamp as a string
            return String.valueOf(startOfDayTimestamp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String convertTimestampToEndOfDay(String timestampString) {
        try {
            // Convert timestamp string to long
            long timestamp = Long.parseLong(timestampString);

            // Create Date object from timestamp
            Date date = new Date(timestamp);

            // Set time to midnight (00:00:00)
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);

            // Get timestamp for the start of the day
            long startOfDayTimestamp = date.getTime();

            // Return the start of the day timestamp as a string
            return String.valueOf(startOfDayTimestamp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String convertJalaliToGregorian(String jalaliDate) throws ParseException {

        PersianCalendar jalali =
                ChronoFormatter
                        .ofPattern("yyyy/MM/dd", PatternType.CLDR, Locale.ROOT, PersianCalendar.axis())
                        .parse(jalaliDate );
        PlainDate gregorian = jalali.transform(PlainDate.axis().getChronoType());
        String iso8601 = Iso8601Format.EXTENDED_DATE.format(gregorian);
        System.out.println(iso8601); // 2018-04-03
        return iso8601;
    }
    private void filterDocsByDate(ArrayList<Uploaded_Docs_Model> _uploaded_documents_list) {
        time_stamp_from= Long.parseLong(convertTimestampToStartOfDay(String.valueOf(time_stamp_from)));
        time_stamp_to= Long.parseLong(convertTimestampToEndOfDay(String.valueOf(time_stamp_to)));


        filtered_uploaded_documents_list = new ArrayList<>();
        SimpleDateFormat dateFormatEnd = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        for (Uploaded_Docs_Model uploaded_docs_model : _uploaded_documents_list) {
            try {
//                Date date = dateFormatEnd.parse(uploaded_docs_model.getUploaded_Doc_Date());

                // Convert the Gregorian date to a Shamsi (Jalali) date


                // Convert the Shamsi date to a Unix timestamp
long docTimeInTimeStampFormat=Long.parseLong(convertDateStringToTimestamp(convertJalaliToGregorian( uploaded_docs_model.getUploaded_Doc_Date().substring(0,10) )));



                if ((time_stamp_from != 0 && time_stamp_to != 0) && (time_stamp_from > time_stamp_to)) {
                    showErrorToast("تاریخ شروع بعد از تاریخ پایان انتخاب شده");
                } else if ((docTimeInTimeStampFormat >= time_stamp_from && docTimeInTimeStampFormat < time_stamp_to)
                        || (docTimeInTimeStampFormat > time_stamp_from && time_stamp_to == 0)
                        || (time_stamp_from == 0 && docTimeInTimeStampFormat < time_stamp_to)
                        || (time_stamp_from == 0 && time_stamp_to == 0)) {
                    filtered_uploaded_documents_list.add(uploaded_docs_model);
                }
            } catch (Exception e) {

            }
        }

        rc_view_uploaded_docs_list.setLayoutManager(new LinearLayoutManager(UploadDocsActivity.this));

        mAdapter = new Docs_Adapter(UploadDocsActivity.this,
                R.layout.uploaded_docs_inner,
                filtered_uploaded_documents_list);

        prg_docs_list.setVisibility(View.GONE);
        rc_view_uploaded_docs_list.setAdapter(mAdapter);
        drawer.closeDrawers();
    }
    public static boolean isDocumentWithinLastTwoDays(long uploadDateTimeStamp) {
        // Convert upload date string to Date object

        // Get the current date
        Date currentDate = new Date();

        // Calculate the difference in milliseconds between current date and upload date
        long timeDifference = currentDate.getTime() - uploadDateTimeStamp;

        // Calculate the difference in days
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);

        // Check if the document was uploaded within the last two days
        return daysDifference <= 2;
    }
    private void filterDocsOfTwoLastDays(ArrayList<Uploaded_Docs_Model> _uploaded_documents_list) {



        filtered_uploaded_documents_list = new ArrayList<>();

        for (Uploaded_Docs_Model uploaded_docs_model : _uploaded_documents_list) {
            try {
//                Date date = dateFormatEnd.parse(uploaded_docs_model.getUploaded_Doc_Date());

                // Convert the Gregorian date to a Shamsi (Jalali) date


                // Convert the Shamsi date to a Unix timestamp
                long docTimeInTimeStampFormat=Long.parseLong(convertDateStringToTimestamp(convertJalaliToGregorian( uploaded_docs_model.getUploaded_Doc_Date().substring(0,10) )));


                 if ((isDocumentWithinLastTwoDays(docTimeInTimeStampFormat))) {
                    filtered_uploaded_documents_list.add(uploaded_docs_model);
                }
            } catch (Exception e) {

            }
        }

        rc_view_uploaded_docs_list.setLayoutManager(new LinearLayoutManager(UploadDocsActivity.this));

        mAdapter = new Docs_Adapter(UploadDocsActivity.this,
                R.layout.uploaded_docs_inner,
                filtered_uploaded_documents_list);

        prg_docs_list.setVisibility(View.GONE);
        rc_view_uploaded_docs_list.setAdapter(mAdapter);
        drawer.closeDrawers();
    }
    private void showSuccessToast(String message) {
        View view = getLayoutInflater().inflate(R.layout.ctoast_view,null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    private void showErrorToast(String message) {
        View view = getLayoutInflater().inflate(R.layout.ctoast_view_error,null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    private void getMyDocs() {

        Common c = new Common(this);
        String s = c.URL();


        ApiInterface apiInterface;

        if (s.contains("pilot")) {
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }
        Call<DocsListModel> logCall = apiInterface.getDocsList(
                "GetDocslist", new GetDocListModel("", "0", GlobalData.getStoreID(), " ", " ", new metaData(GlobalData.getUserName(), GlobalData.getAppVersionCode(), Utility.getDeviceInfo(), Utility.applicationMode + "")));

        logCall.enqueue(new Callback<DocsListModel>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<DocsListModel> call, Response<DocsListModel> response) {


                if (response.body() != null && response.isSuccessful() == true && response.body().getList() != null) {
                    showMyDocs(response.body());


                } else {

                    prg_docs_list.setVisibility(View.GONE);

                 showSuccessToast("شما هنوز هیچ سندی اضافه نکرده اید");

                }

            }

            @Override
            public void onFailure(Call<DocsListModel> call, Throwable t) {
                showErrorToast("مشکلی در ارتباط با سرور رخ داد.");

                prg_docs_list.setVisibility(View.GONE);

            }
        });


    }

    private void showMyDocs(DocsListModel body) {
        String doc_url_1 = "";
        String doc_url_2 = "";
        String doc_url_3 = "";
        String doc_url_4 = "";
        String doc_url_5 = "";
        String doc_url_6 = "";


        for (int i = 0; i < body.getList().size(); i++) {
            Uploaded_Docs_Model uploaded_docs_model = new Uploaded_Docs_Model(
                    body.getList().get(i).getHeaderID(),
                    body.getList().get(i).getTitle(),
                    body.getList().get(i).getDocumentTypeTitle(),
                    body.getList().get(i).getDocumentTypeCode(),
                    body.getList().get(i).getComment(),
                    body.getList().get(i).getInsertDate());

            try {
                uploaded_docs_model.setUploaded_Doc_img1_ID(body.getList().get(i).getImageList().get(0).getDetailID());
                uploaded_docs_model.setUploaded_Doc_img2_ID(body.getList().get(i).getImageList().get(1).getDetailID());
                uploaded_docs_model.setUploaded_Doc_img3_ID(body.getList().get(i).getImageList().get(2).getDetailID());
                uploaded_docs_model.setUploaded_Doc_img4_ID(body.getList().get(i).getImageList().get(3).getDetailID());
                uploaded_docs_model.setUploaded_Doc_img5_ID(body.getList().get(i).getImageList().get(4).getDetailID());


            } catch (Exception e) {

            }
            try {
                uploaded_docs_model.setUploaded_Doc_img1(body.getList().get(i).getImageList().get(0).getImageURL());
                uploaded_docs_model.setUploaded_Doc_img2(body.getList().get(i).getImageList().get(1).getImageURL());
                uploaded_docs_model.setUploaded_Doc_img3(body.getList().get(i).getImageList().get(2).getImageURL());
                uploaded_docs_model.setUploaded_Doc_img4(body.getList().get(i).getImageList().get(3).getImageURL());
                uploaded_docs_model.setUploaded_Doc_img5(body.getList().get(i).getImageList().get(4).getImageURL());
            } catch (Exception e) {

            }
            uploaded_documents_list.add(uploaded_docs_model);


        }
       filterDocsOfTwoLastDays(uploaded_documents_list);
    }

    private void setUp() {
        rc_view_uploaded_docs_list = findViewById(R.id.rc_docs);
        fab_doc_send = findViewById(R.id.fab_send_docs);
        uploaded_documents_list = new ArrayList<>();
        prg_docs_list = findViewById(R.id.prg_docs_list);
        prg_docs_list.setVisibility(View.VISIBLE);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        imgMenue=findViewById(R.id.imgmenue);
        drawer = findViewById(R.id.drawer_layout);
        lyt_from_date = findViewById(R.id.lyt_from_date);
        txt_from_date = findViewById(R.id.txt_from_date);
        txt_to_date = findViewById(R.id.txt_to_date);
        lyt_to_date = findViewById(R.id.lyt_to_date);
        btnApply = findViewById(R.id.btn_apply_promotion_list);
        lyt_sardarb_help_icon=findViewById(R.id.lyt_sardarb_guide_icon);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        drawer.openDrawer(GravityCompat.END);

        return false;

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UploadDocsActivity.this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}