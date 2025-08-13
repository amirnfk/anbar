package com.oshanak.mobilemarket.Activity.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Models.DocsListModel;
import com.oshanak.mobilemarket.Activity.Models.GetDocListModel;
import com.oshanak.mobilemarket.Activity.Models.ItemModel;
import com.oshanak.mobilemarket.Activity.Models.ItemsListModel;
import com.oshanak.mobilemarket.Activity.Models.ItemsListRequestModel;
import com.oshanak.mobilemarket.Activity.Models.metaData;
import com.oshanak.mobilemarket.Activity.RowAdapter.Docs_Adapter;
import com.oshanak.mobilemarket.Activity.RowAdapter.Items_Adapter;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Get_Item_API;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Get_Item_API_PILOT;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductByNameActivity extends AppCompatActivity {

    EditText et_search;
    FloatingActionButton fabSearch;
    ProgressBar progress_bar;

    RecyclerView rc_view_list;

    ArrayList<ItemModel> Items_list;
    Items_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_by_name);
        fabSearch = findViewById(R.id.fab_SearchItem);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        et_search = findViewById(R.id.et_search);
        rc_view_list = findViewById(R.id.rc_view_items_list);
        Items_list = new ArrayList<ItemModel>();
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (et_search.getText().toString().length() > 2) {
                        Items_list.clear();

                        fabSearch.setVisibility(View.GONE);
                        progress_bar.setVisibility(View.VISIBLE);
                        getSearchItems(et_search.getText().toString());
                        return true;
                    }
                }else{
                    Toast.makeText(SearchProductByNameActivity.this, "لطفا برای جستج. حداقل سه کاراکتر تایپ نمایید", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().length() > 2) {
                Items_list.clear();

                fabSearch.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                getSearchItems(et_search.getText().toString());
                }else{
                    Toast.makeText(SearchProductByNameActivity.this, "برای جستجو حداقل سه کاراکتر وارد نمایید", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getSearchItems(String SearchItem) {

        Common c = new Common(this);
        String s = c.URL();

        ApiInterface apiInterface;

        if (s.contains("pilot")) {
            apiInterface = Get_Item_API_PILOT.getAPI().create(ApiInterface.class);
        } else {
            apiInterface = Get_Item_API.getAPI().create(ApiInterface.class);
        }
        Call<ItemsListModel> logCall = apiInterface.getItemsList(
                "getallitemlist", new ItemsListRequestModel(SearchItem, new metaData(GlobalData.getUserName(), GlobalData.getAppVersionCode(), Utility.getDeviceInfo(), Utility.applicationMode + "")));

        logCall.enqueue(new Callback<ItemsListModel>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<ItemsListModel> call, Response<ItemsListModel> response) {
                progress_bar.setVisibility(View.GONE);
                fabSearch.setVisibility(View.VISIBLE);

                if (response.body() != null && response.isSuccessful() == true && response.body().getAllItemDataList() != null) {

                    showItems(response.body());

                } else {

                    Toast.makeText(SearchProductByNameActivity.this, "موردی یافت نشد", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ItemsListModel> call, Throwable t) {

                Toast.makeText(SearchProductByNameActivity.this, "مشکلی در ارتباط با سرور رخ داد", Toast.LENGTH_SHORT).show();
                fabSearch.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }
        });


    }

    private void showItems(ItemsListModel body) {
        for (int i = 0; i < body.getAllItemDataList().size(); i++) {
            Items_list.add(body.getAllItemDataList().get(i));
        }

        rc_view_list.setLayoutManager(new LinearLayoutManager(SearchProductByNameActivity.this));


        mAdapter = new Items_Adapter(SearchProductByNameActivity.this,
                R.layout.items_inner,
                Items_list, SearchProductByNameActivity.this);

        rc_view_list.setAdapter(mAdapter);
    }


}