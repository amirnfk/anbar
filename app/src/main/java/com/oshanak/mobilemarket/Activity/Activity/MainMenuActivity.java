package com.oshanak.mobilemarket.Activity.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.SignInButton;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingControlHeaderActivity;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingDeliverHeaderListActivity;
import com.oshanak.mobilemarket.Activity.PushNotification.Notification;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRestApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRetrofitClient;
import com.oshanak.mobilemarket.Activity.PushNotification.PushNotificationService;
import com.oshanak.mobilemarket.Activity.RowAdapter.NotificationAdapter;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMenuActivity extends BaseActivity {
    List<Notification> notificationList;
    List<Notification> newNotificationList;
    ProgressBar prg;
    ImageView imgRefresh;
    TextView notificationBadge;
    private final String AUTH_HEADER = "Basic bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_men_grid);

        startPushNotificationService();
        imgRefresh = findViewById(R.id.img_refresh_main);
        prg = findViewById(R.id.prg_main);
        notificationBadge = findViewById(R.id.notificationBadge);

        notificationList = new ArrayList<>();
        newNotificationList = new ArrayList<>();
        if (Utility.restartAppIfNeed(this)) return;

        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1));
        fetchNotifications();
        TextView tvTitle = findViewById(R.id.tvTitle);

        this.setTitle("هفت - فروش تلفني");
        tvTitle.setText("فروشگاه هفت" + " (کد فروشگاه: " + GlobalData.getStoreID() +") " );
imgRefresh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        fetchNotifications();
    }
});

    }

    @Override
    protected void onRestart() {
        fetchNotifications();
        super.onRestart();
    }


    private void fetchNotifications() {
        prg.setVisibility(View.VISIBLE);
        notificationList.clear();
        newNotificationList.clear();
        NotificationRestApiService apiService = NotificationRetrofitClient.getClient().create(NotificationRestApiService.class);
        Call<List<Notification>> call = apiService.getAllNotifications(Integer.parseInt(GlobalData.getStoreID()), AUTH_HEADER);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                prg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    notificationList = response.body();
                    for (Notification notification : notificationList
                    ) {
                        if (!notification.isSeen()) {
                            newNotificationList.add(notification);
                        }
                    }

                    notificationBadge.setText(newNotificationList.size() + "");

                } else {
                    Log.d("checkTheError1",response.toString());
                    Utility.showFailureToast(MainMenuActivity.this, "مشکلی در ارتباط شما با سرور رخ داده است");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.d("checkTheError2",t.getMessage());
                Utility.showFailureToast(MainMenuActivity.this, "مشکلی در ارتباط شما با سرور رخ داده است");
                prg.setVisibility(View.GONE);
            }
        });
    }

    private void startPushNotificationService() {
        if (!PushNotificationService.isRunning()) {
            Intent serviceIntent = new Intent(this, PushNotificationService.class);
            serviceIntent.putExtra("Service_key", "Start Notification Service");
            serviceIntent.setAction("Start");
            startService(serviceIntent);
        }

    }


    public void onProductControl(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ProductControlActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onCompetitorList(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, CompetitorListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickWarehousing(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehousingOneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onDeliverOrder(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, DeliverOrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onOrderList(View view) {
        if (Utility.isDoubleClick()) return;
        String url = "https://oportal.oshanak.com/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
    public void onPickingOrder(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PickingDeliverHeaderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickDeliverOrder(View view) {

        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, StoreDeliverInboundHeaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onGarbageProduct(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, GarbageProductHeaderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onPrintBarcode(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PrintBarcodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onDirectProduct(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, DirectReceiveListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    public void onWarehouseOrder(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehouseOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoNotificationsList(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, NotificationListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickReturn(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ReturnFromStoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onDirectRelation(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, StoreDirectRelationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onPresentation(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ColleaguePresentationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onWarehouseCounting(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehouseCountingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onCollectorChecking(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PickingControlHeaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onPromotionListClicked(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PromotionsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void on_Docs_Uploade_Clicked(View view) {
        if (Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, UploadDocsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


}
