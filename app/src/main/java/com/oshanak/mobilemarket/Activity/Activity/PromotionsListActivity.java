package com.oshanak.mobilemarket.Activity.Activity;

import static com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity.EXTRA_IMAGE_URL;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getAppVersionCode;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getPassword;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getStoreID;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getUserName;
import static com.oshanak.mobilemarket.Activity.Enum.ApplicationMode.StoreHandheld;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.DiscountComparator;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.DiscountComparator_High_to_Low;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.Post_Price_Comparator_High_to_Low;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.Post_Price_Comparator_Low_to_High;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.Pre_Price_Comparator_High_to_Low;
import com.oshanak.mobilemarket.Activity.Activity.Comparator.Pre_Price_Comparator_Low_to_High;
import com.oshanak.mobilemarket.Activity.Common.DelayedOnQueryTextListener;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.Promotion_Info_Model;
import com.oshanak.mobilemarket.Activity.Models.DataStructure;
import com.oshanak.mobilemarket.Activity.RowAdapter.Promotions_Adapter;
import com.oshanak.mobilemarket.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class PromotionsListActivity extends AppCompatActivity {
    Toolbar toolbar1;
    NestedScrollView nestedScrollView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    ImageView img;
    ImageView img_top_header;
    ImageView img_from_date_clear;
    ImageView img_to_date_clear;
    TextView txt_promotion_title;
    TextView txt_promotion_date;
    TextView txt_promotion_count;
    RadioGroup rdBtnGroup_sort;
    RadioGroup rdBtnGroup_filter;
    RadioButton rdBtnStart;
    RadioButton rdBtnFilterAll;
    RadioButton rdBtnEnd;
    RadioButton rdBtnStartedToday;
    RadioButton rdBtnExpiredToday;
    RadioButton rdBtnExpiredYesterday;
    TextView btnApply;
    TextView txt_from_date;
    TextView txt_to_date;
    LinearLayout lyt_to_date;
    private DrawerLayout drawer;
    LinearLayout lyt_from_date;
    RecyclerView rc_view_promotion_list;
    ArrayList<Promotion_Info_Model> promotion_list;
    ArrayList<Promotion_Info_Model> sorted_promotion_list;
    ArrayList<Promotion_Info_Model> filtered_promotion_list;
    Promotions_Adapter mAdapter;
    RadioButton rdBtn_sort_highest_price_before;
    RadioButton rdBtn_sort_lowest_price_before;
    RadioButton rdBtn_sort_highest_price_after;
    RadioButton rdBtn_sort_lowest_price_after;
    RadioButton rdBtn_sort_highest_discount;
    RadioButton rdBtn_sort_lowest_discount;
    ProgressBar prg_promotion_list;
    SearchView searchIcon;
    static long time_stamp_to;
    static long time_stamp_from;
    ImageButton imgHeaderInlarger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_promotions_list);
        setUp();
        txt_promotion_title.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        txt_promotion_date.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        txt_promotion_count.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        prg_promotion_list.setVisibility(View.VISIBLE);
        txt_promotion_date.setText(Utility.getCurrentPersianDate().substring(0, 10));
        String  backHeaderImgUrl="https://storehandheld.ows.gbgnetwork.net/background/header.jpg";

        Glide.with(this)
                .load(backHeaderImgUrl)
                .skipMemoryCache(true) // Prevent caching in memory
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.back_header) // Replace with your placeholder image resource
                .error(R.drawable.back_header)
                .into(img_top_header)             ;
img_top_header.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PromotionsListActivity.this, ImageEnlargedActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, backHeaderImgUrl);
        startActivity(intent);
    }
});
imgHeaderInlarger.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PromotionsListActivity.this, ImageEnlargedActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, backHeaderImgUrl);
        startActivity(intent);
    }
});

        promotion_list = new ArrayList<Promotion_Info_Model>();
        sorted_promotion_list = new ArrayList<Promotion_Info_Model>();
        filtered_promotion_list = new ArrayList<Promotion_Info_Model>();
//        rdBtnFilterAll.setChecked(true);
        lyt_from_date.setVisibility(View.GONE);
        lyt_to_date.setVisibility(View.GONE);
        try {
            getPromotionList();
        } catch (JSONException e) {

        }
        img_from_date_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_from_date.setText("");
                time_stamp_from = 0;
            }
        });
        img_to_date_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_to_date.setText("");
                time_stamp_to = 0;
            }
        });
        lyt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(PromotionsListActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(PromotionsListActivity.this)
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
        txt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(PromotionsListActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(PromotionsListActivity.this)
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
        txt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(PromotionsListActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(PromotionsListActivity.this)
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
        lyt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(PromotionsListActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(PromotionsListActivity.this)
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
//        promotion_list.add(new Promotion_Info_Model("کنسرو ذرت لوبیا", "https://cdn.modiseh.com/unsafe/medium/mt0559_1.jpg", 12000, 10000, 16));
//        promotion_list.add(new Promotion_Info_Model("نخود 500 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0679/255855_main_1.jpg", 18000, 13000, 27));
//        promotion_list.add(new Promotion_Info_Model("نمک دریایی دلفین", "https://cdn.modiseh.com/unsafe/medium/mt0724_1.jpg", 32000, 30000, 6));
//        promotion_list.add(new Promotion_Info_Model("پسته 100 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0417_3.jpg", 11000, 10000, 9));
//        promotion_list.add(new Promotion_Info_Model("قند شکسته 500 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0728_1.jpg", 45000, 40000, 11));
//        promotion_list.add(new Promotion_Info_Model("فلفل سیاه 100 گرمی گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0805_1.jpg", 18000, 15000, 16));
//        promotion_list.add(new Promotion_Info_Model("سس هالوپینو گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0887_2.jpg", 10000, 8000, 20));
//        promotion_list.add(new Promotion_Info_Model("کنسرو ذزت لوبیا", "https://cdn.modiseh.com/unsafe/medium/mt0559_1.jpg", "12000", "10000", 16));
//        promotion_list.add(new Promotion_Info_Model("نخود 500 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0679/255855_main_1.jpg", "18000", "13000", 27));
//        promotion_list.add(new Promotion_Info_Model("نمک دریایی دلفین", "https://cdn.modiseh.com/unsafe/medium/mt0724_1.jpg", "32000", "30000", 6));
//        promotion_list.add(new Promotion_Info_Model("پسته 100 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0417_3.jpg", "11000", "10000", 9));
//        promotion_list.add(new Promotion_Info_Model("قند شکسته 500 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0728_1.jpg", "45000", "40000", 11));
//        promotion_list.add(new Promotion_Info_Model("فلفل سیاه 100 گرمی گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0805_1.jpg", "18000", "15000", 16));
//        promotion_list.add(new Promotion_Info_Model("سس هالوپینو گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0887_2.jpg", "10000", "8000", 20));
//        promotion_list.add(new Promotion_Info_Model(" زرد چوبه 100 گرم گلستان", "https://cdn.modiseh.com/unsafe/medium/mt0972_1.jpg", 15000, 14000, 6));


//Todo
        //comented recentle for delay problem
//        rc_view_promotion_list.setLayoutManager(new GridLayoutManager(PromotionsListActivity.this, 2));
////        rc_view_promotion_list.setLayoutManager(new LinearLayoutManager(PromotionsListActivity.this));
//
//
//        mAdapter = new Promotions_Adapter(PromotionsListActivity.this,
//                R.layout.promotions_inner_layout,
//                promotion_list,prg_promotion_list);
//        rc_view_promotion_list.setAdapter(mAdapter);

        rdBtnGroup_filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if ((checkedId == R.id.rd_btn_expired_yesterday) ||
                        (checkedId == R.id.rd_btn_started_today) ||
                        (checkedId == R.id.rd_btn_expired_today) ||
                        (checkedId == R.id.rd_btn_all)) {
                    lyt_from_date.setVisibility(View.GONE);
                    lyt_to_date.setVisibility(View.GONE);
                } else {
                    lyt_from_date.setVisibility(View.VISIBLE);
                    lyt_to_date.setVisibility(View.VISIBLE);
                }
                time_stamp_from = 0;
                time_stamp_to = 0;
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    applyFilter(promotion_list);
                } catch ( Exception e) {
                    showErrorToast("اطلاعات به درستی وارد نشده است");
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    img.setVisibility(View.VISIBLE);
                } else if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    img.setVisibility(View.GONE);
                } else {

//                    img.animate().alpha(1-percentage);
                }
            }
        });

//        lyt_sort_highest_price_before.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//                refreshRecyclerview(promotion_list, new Pre_Price_Comparator_High_to_Low());
//            }
//        });
//        lyt_sort_highest_price_after.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//                refreshRecyclerview(promotion_list, new Post_Price_Comparator_High_to_Low());
//            }
//        });
//        lyt_sort_lowest_price_after.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//                refreshRecyclerview(promotion_list, new Post_Price_Comparator_Low_to_High());
//            }
//        });
//        lyt_sort_lowest_price_before.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//                refreshRecyclerview(promotion_list, new Pre_Price_Comparator_Low_to_High());
//            }
//        });
//        lyt_sort_lowest_discount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//
//
//                refreshRecyclerview(promotion_list, new DiscountComparator());
//            }
//        });
//        lyt_sort_highest_discount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prg_promotion_list.setVisibility(View.VISIBLE);
//
//                setBackgroundColorFromClick(v);
//                drawer.closeDrawers();
//
//
//                refreshRecyclerview(promotion_list, new DiscountComparator_High_to_Low());
//            }
//        });
    }

    private void applyFilter(ArrayList<Promotion_Info_Model> __promotion_list)  {
        Comparator comparator = new DiscountComparator();

        switch (rdBtnGroup_sort.getCheckedRadioButtonId()) {

            case R.id.rd_btn_highest_discount:
                comparator = new DiscountComparator_High_to_Low();
                break;
            case R.id.rd_btn_lowest_discount:
                comparator = new DiscountComparator();
                break;
            case R.id.rd_btn_highest_price_after_discount:
                comparator = new Post_Price_Comparator_High_to_Low();
                break;
            case R.id.rd_btn_lowest_price_after_discount:
                comparator = new Post_Price_Comparator_Low_to_High();
                break;
            case R.id.rd_btn_lowest_price_before_discount:
                comparator = new Pre_Price_Comparator_Low_to_High();
                break;
            case R.id.rd_btn_highest_price_before_discount:
                comparator = new Pre_Price_Comparator_High_to_Low();
                break;
            default:
                comparator = new DiscountComparator_High_to_Low();
                rdBtn_sort_highest_discount.setChecked(true);
                break;
        }



        filtered_promotion_list = new ArrayList<>();
        filtered_promotion_list.clear();
        switch (rdBtnGroup_filter.getCheckedRadioButtonId()) {

            case R.id.rd_btn_started_today:

                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {


                    if (promotion_info_model.getPromotion_Product_From_Date_S().equals(Utility.getCurrentPersianShortDate())) {


                        filtered_promotion_list.add(promotion_info_model);

                    }
                }
                break;

            case R.id.rd_btn_expired_today:

                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {


                    if (promotion_info_model.getPromotion_Product_To_Date_S().equals(Utility.getCurrentPersianShortDate())) {
                        filtered_promotion_list.add(promotion_info_model);

                    }
                }
                break;
            case R.id.rd_btn_expired_yesterday:


                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {


                    if (promotion_info_model.getPromotion_Product_To_Date_M().equals(Utility.getYesterdaysMiladiDate())) {
                        filtered_promotion_list.add(promotion_info_model);

                    }

                }
                break;
            case R.id.rd_btn_start:
                SimpleDateFormat dateFormat = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                }

                // Parse the date string into a Date object


                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {
                    try {
                        Date date = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            date = dateFormat.parse(promotion_info_model.getPromotion_Product_From_Date_M());
                        }

                        // Convert the Date object to a Unix timestamp
                        long promotion_info_model_timestamp = date.getTime();

                        if ((time_stamp_from != 0 && time_stamp_to != 0) && (time_stamp_from > time_stamp_to)) {
                            showErrorToast("تاریخ شروع بعد از تاریخ پایان انتخاب شده");
                        } else if ((promotion_info_model_timestamp > time_stamp_from && promotion_info_model_timestamp < time_stamp_to)
                                || (promotion_info_model_timestamp > time_stamp_from && time_stamp_to == 0)
                                || (time_stamp_from == 0 && promotion_info_model_timestamp < time_stamp_to)
                                || (time_stamp_from == 0 && time_stamp_to == 0)) {
                            filtered_promotion_list.add(promotion_info_model);


                        }
                    } catch (Exception e) {

                    }


                }


                break;
            case R.id.rd_btn_end:
                SimpleDateFormat dateFormatEnd = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateFormatEnd = new SimpleDateFormat("yyyy-MM-dd");
                }

                // Parse the date string into a Date object


                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {
                    try {
                        Date date = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            date = dateFormatEnd.parse(promotion_info_model.getPromotion_Product_To_Date_M());
                        }

                        // Convert the Date object to a Unix timestamp
                        long promotion_info_model_timestamp = date.getTime();

                        if ((time_stamp_from != 0 && time_stamp_to != 0) && (time_stamp_from > time_stamp_to)) {
                            showErrorToast("تاریخ شروع بعد از تاریخ پایان انتخاب شده");
                        }else if ((promotion_info_model_timestamp > time_stamp_from && promotion_info_model_timestamp < time_stamp_to)
                                || (promotion_info_model_timestamp > time_stamp_from && time_stamp_to == 0)
                                || (time_stamp_from == 0 && promotion_info_model_timestamp < time_stamp_to)
                                || (time_stamp_from == 0 && time_stamp_to == 0)) {
                            filtered_promotion_list.add(promotion_info_model);


                        }
                    } catch (Exception e) {

                    }


                }


                break;
            case R.id.rd_btn_all:
                for (Promotion_Info_Model promotion_info_model : __promotion_list
                ) {
                    if (!(promotion_info_model.getPromotion_Product_To_Date_M().equals(Utility.getYesterdaysMiladiDate()))) {
                        filtered_promotion_list.add(promotion_info_model);

                    }

                }
                break;

            default:
                filtered_promotion_list.addAll(__promotion_list);
        }

        refreshRecyclerview(filtered_promotion_list, comparator);
        txt_promotion_count.setText(filtered_promotion_list.size() + " کالای تخفیفی ");
        drawer.closeDrawers();
    }


    private void getPromotionList() throws JSONException {
        RequestQueue requestQueue = null;
        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        Gson gson = new Gson();
        JSONObject jsonobject2 = new JSONObject();
        jsonobject2.put("UserName", getUserName());
        jsonobject2.put("AppVersionCode", getAppVersionCode());
        jsonobject2.put("Password", getPassword());
        jsonobject2.put("AppMode", StoreHandheld);
        jsonobject2.put("DeviceInfo", Utility.getDeviceInfo());
        jsonobject2.put("StoreID", getStoreID());
        jsonObject.put("metaData", jsonobject2);


        com.oshanak.mobilemarket.Activity.Service.Common common = new com.oshanak.mobilemarket.Activity.Service.Common(this);
        String url = common.URL() + "Store_REST.svc/GetPromotionList";//"https://storehandheldpilot.ows.gbgnetwork.net/Store_REST.svc/GetPromotionList";


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getJSONObject("GetPromotionListResult").get("message").equals("Successful!")) {


                                ArrayList<String> listdata = new ArrayList<>();
                                JSONArray jArray = (JSONArray) response.getJSONObject("GetPromotionListResult").get("dataStructure");
                                if (jArray != null) {
                                    for (int i = 0; i < jArray.length(); i++) {
                                        listdata.add(jArray.getString(i));
                                    }
                                }
                                //
                                txt_promotion_count.setText(listdata.size() + " کالای تخفیفی ");
                                prg_promotion_list.setVisibility(View.GONE);

                                Gson gson = new Gson();
                                Type objectType = DataStructure.class;
                                ArrayList<DataStructure> myList = new ArrayList<>();

                                for (int i = 0; i < listdata.size(); i++) {
                                    myList.add(gson.fromJson(String.valueOf(listdata.get(i)), objectType));
                                }


                                for (int i = 0; i < myList.size(); i++) {
                                    int price = myList.get(i).getSalesPrice();
                                    int off_price = myList.get(i).getPromotionPrice();
                                    int off_percent = 0;
                                    try {
                                        off_percent = Math.round((price - off_price) * 100 / price);
                                    } catch (Exception e) {

                                    }
                                    promotion_list.add(new Promotion_Info_Model((myList.get(i).getItemId()),
                                            myList.get(i).getItemName(),
                                            "https://oshanakportal.oshanak.com/Sale/Item/ItemImage/" + myList.get(i).getImageURL() + myList.get(i).getItemId() + "",
                                            myList.get(i).getSalesPrice(),
                                            myList.get(i).getPromotionPrice(),
                                            off_percent,
                                            myList.get(i).getPromotionToDate_S(),
                                            myList.get(i).getPromotionToDate_M(),
                                            myList.get(i).getPromotionFromDate_S(),
                                            myList.get(i).getPromotionFromDate_M()

                                    ));

                                }
//                                promotion_list=myList;

//                                for (int i = 0; i < getNewOrderNotificationResult.getDataStructure().size(); i++) {
////                            notifications_db.insertNotificationToDatabase(new Notification_Model((getNewOrderNotificationResult.getDataStructure().get(i)));
//                                }
//                                notifications_db.insertNotificationToDatabase(new Notification_Model());
////                                showNotification();
                            } else {

                            }
                        } catch (JSONException e) {

                        }
//                        rc_view_promotion_list.setLayoutManager(new GridLayoutManager(PromotionsListActivity.this, 2));
                        rc_view_promotion_list.setLayoutManager(new LinearLayoutManager(PromotionsListActivity.this));


                        mAdapter = new Promotions_Adapter(PromotionsListActivity.this,
                                R.layout.promotions_inner_layout_linear,
                                promotion_list, prg_promotion_list, txt_promotion_count);


                        rc_view_promotion_list.setAdapter(mAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }


    private void refreshRecyclerview(ArrayList<Promotion_Info_Model> _promotion_list, Comparator comparator) {
        Collections.sort(_promotion_list, comparator);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new Promotions_Adapter(PromotionsListActivity.this,
                        R.layout.promotions_inner_layout_linear,
                        _promotion_list, prg_promotion_list, txt_promotion_count);

                rc_view_promotion_list.setAdapter(mAdapter);

                prg_promotion_list.setVisibility(View.GONE);
            }
        }, 300);


    }

//    private void setBackgroundColorFromClick(View v) {
//
//        lyt_sort_lowest_discount.setBackgroundColor(getResources().getColor(R.color.White));
//        lyt_sort_highest_discount.setBackgroundColor(getResources().getColor(R.color.White));
//        lyt_sort_lowest_price_before.setBackgroundColor(getResources().getColor(R.color.White));
//        lyt_sort_lowest_price_after.setBackgroundColor(getResources().getColor(R.color.White));
//        lyt_sort_highest_price_after.setBackgroundColor(getResources().getColor(R.color.White));
//        lyt_sort_highest_price_before.setBackgroundColor(getResources().getColor(R.color.White));
//        v.setBackgroundColor(getResources().getColor(R.color.light_orange));
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.promotion_toolbar_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//             searchView= (SearchView) searchIcon.getActionView();
        searchIcon.setOnQueryTextListener(new DelayedOnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public void onDelayerQueryTextChange(String query) {
                if (query.length() == 0) {
                    refreshRecyclerview(promotion_list, new Post_Price_Comparator_High_to_Low());
                }
                prg_promotion_list.setVisibility(View.VISIBLE);
                mAdapter.getFilter().filter(query.toString());

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        drawer.openDrawer(GravityCompat.END);

        return false;

    }

    //
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu){
//        menu.findItem(R.id.mnutoolbar).setEnabled(false);
//
//        return super.onPrepareOptionsMenu(menu);
//    }
    private void showErrorToast(String message) {
        View view = getLayoutInflater().inflate(R.layout.ctoast_view_error, null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void setUp() {
        imgHeaderInlarger=findViewById(R.id.img_header_inlarger);
        btnApply = findViewById(R.id.btn_apply_promotion_list);
        lyt_from_date = findViewById(R.id.lyt_from_date);
        txt_from_date = findViewById(R.id.txt_from_date);
        txt_to_date = findViewById(R.id.txt_to_date);
        lyt_to_date = findViewById(R.id.lyt_to_date);
        searchIcon = findViewById(R.id.search);
        nestedScrollView = findViewById(R.id.nestedscrrol);
        prg_promotion_list = findViewById(R.id.prg_promotion_list);
        toolbar1 = findViewById(R.id.toolbar1);
        appBarLayout = findViewById(R.id.appBar);
        img = findViewById(R.id.img);
        img_top_header=findViewById(R.id.img_top_header);
        setSupportActionBar(toolbar1);
        rc_view_promotion_list = findViewById(R.id.rc_promotion_list);
//      getSupportActionBar().setHomeButtonEnabled(true);
//      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      getSupportActionBar().setIcon(R.drawable.ic_baseline_filter_list_24);
        drawer = findViewById(R.id.drawer_layout);
        txt_promotion_date = findViewById(R.id.txt_promotion_date);
        txt_promotion_count = findViewById(R.id.txt_promotion_list_count);
        txt_promotion_title = findViewById(R.id.txt_promotion_title);
        collapsingToolbarLayout = findViewById(R.id.collaps1);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        rdBtn_sort_highest_discount = findViewById(R.id.rd_btn_highest_discount);
        rdBtn_sort_lowest_discount = findViewById(R.id.rd_btn_lowest_discount);
        rdBtn_sort_highest_price_after = findViewById(R.id.rd_btn_highest_price_after_discount);
        rdBtn_sort_lowest_price_after = findViewById(R.id.rd_btn_lowest_price_after_discount);
        rdBtn_sort_highest_price_before = findViewById(R.id.rd_btn_highest_price_before_discount);
        rdBtn_sort_lowest_price_before = findViewById(R.id.rd_btn_lowest_price_before_discount);
        img_from_date_clear = findViewById(R.id.img_from_date_clear);
        img_to_date_clear = findViewById(R.id.img_to_date_clear);
        rdBtnGroup_filter = findViewById(R.id.rd_group_filter);
        rdBtnGroup_sort = findViewById(R.id.rd_group_sort);
        rdBtnStart = findViewById(R.id.rd_btn_start);
        rdBtnFilterAll = findViewById(R.id.rd_btn_all);
        rdBtnEnd = findViewById(R.id.rd_btn_end);
        rdBtnExpiredYesterday = findViewById(R.id.rd_btn_expired_yesterday);
        rdBtnExpiredToday = findViewById(R.id.rd_btn_expired_today);
        rdBtnStartedToday = findViewById(R.id.rd_btn_started_today);
    }
}