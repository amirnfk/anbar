package com.oshanak.mobilemarket.Activity.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingControlHeaderActivity;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingDeliverHeaderListActivity;
import com.oshanak.mobilemarket.Activity.PushNotification.PushNotificationService;
import com.oshanak.mobilemarket.R;


public class MainMenuActivity_Picking extends AppCompatActivity {




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


                setContentView(R.layout.activity_main_menu_redesign);


        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1));


        if (Utility.restartAppIfNeed(this)) return;






        //Starting Push Notification Service for Delivering Snap Orders

        //region Animation
//        findViewById(R.id.bWarehouse).setAnimation(Anim.inFromRightAnimation(500));
//        findViewById(R.id.bReturn).setAnimation(Anim.inFromRightAnimation(550));
//        findViewById(R.id.bDirectOrder).setAnimation(Anim.inFromRightAnimation(600));
//        findViewById(R.id.bGarbage).setAnimation(Anim.inFromRightAnimation(650));
//        findViewById(R.id.bInfo).setAnimation(Anim.inFromRightAnimation(700));
//        findViewById(R.id.bDeliverProduct).setAnimation(Anim.inFromRightAnimation(750));
//
//        findViewById(R.id.bProductControl).setAnimation(Anim.inFromLeftAnimation(500));
//        findViewById(R.id.bGetProductByOrder).setAnimation(Anim.inFromLeftAnimation(550));
//        findViewById(R.id.bGetProductDirect).setAnimation(Anim.inFromLeftAnimation(600));
//        findViewById(R.id.bPrintBarcode).setAnimation(Anim.inFromLeftAnimation(650));
//        findViewById(R.id.bWarehouseOrder).setAnimation(Anim.inFromLeftAnimation(700));
//        findViewById(R.id.bCompetitor).setAnimation(Anim.inFromLeftAnimation(750));
        //endregion Animation

        TextView tvTitle = findViewById(R.id.tvTitle);
        switch (Utility.applicationMode)
        {
            case PhoneDelivery:
                this.setTitle("هفت - فروش تلفني");
                tvTitle.setText("همكار محترم" + " " + GlobalData.getUserName() + "، " + "خوش آمديد");
                break;
            case PickingWarehouse:
                this.setTitle("هفت - جمع آوري انبار");
                tvTitle.setText("همكار محترم" + " " + GlobalData.getUserName() + "، " + "خوش آمديد");
                 break;
            case StoreHandheld:
                startPushNotificationService();
                this.setTitle("هفت - سيستم فروشگاهي");
                tvTitle.setText("فروشگاه محترم" + " " + GlobalData.getStoreName() + " (" + GlobalData.getStoreID() + ")، " + "خوش آمديد");
                break;
            case Competitor:
                this.setTitle("هفت - رُقبا");
                tvTitle.setText("همكار محترم" + " " + GlobalData.getUserName() + "، " + "خوش آمديد");
                break;
        }

        if(Utility.applicationMode == ApplicationMode.PhoneDelivery)
        {
            findViewById(R.id.lPickingOrder).setVisibility(View.GONE);
            findViewById(R.id.lProductControl).setVisibility(View.GONE);
            findViewById(R.id.lWarehousing).setVisibility(View.GONE);
            findViewById(R.id.lDeliverOrder).setVisibility(View.GONE);
            findViewById(R.id.lGarbageProduct).setVisibility(View.GONE);
            findViewById(R.id.lPrintBarcode).setVisibility(View.GONE);
            findViewById(R.id.lDirectProduct).setVisibility(View.GONE);
            findViewById(R.id.lWarehouseOrder).setVisibility(View.GONE);
            findViewById(R.id.lReturn).setVisibility(View.GONE);
            findViewById(R.id.lStoreDirectRelation).setVisibility(View.GONE);
            findViewById(R.id.lPresentation).setVisibility(View.GONE);
            findViewById(R.id.lWarehouseCounting).setVisibility(View.GONE);
            findViewById(R.id.lCollectorChecking).setVisibility(View.GONE);
            findViewById(R.id.lCompetitorList).setVisibility(View.GONE);
            findViewById(R.id.promotions_list).setVisibility(View.GONE);
            findViewById(R.id.Docs_Upload).setVisibility(View.GONE);
        }
        else if(Utility.applicationMode == ApplicationMode.PickingWarehouse)
        {

            String userType=getIntent().getStringExtra("userType");

            assert userType != null;
            if(userType.equals("X")) {

                findViewById(R.id.lPickingOrder).setVisibility(View.GONE);
                findViewById(R.id.lProductControl).setVisibility(View.GONE);
                findViewById(R.id.lPhoneDelivery).setVisibility(View.GONE);
                findViewById(R.id.lProductControl).setVisibility(View.GONE);
                findViewById(R.id.lWarehousing).setVisibility(View.GONE);
                findViewById(R.id.lDeliverOrder).setVisibility(View.GONE);
                findViewById(R.id.lGarbageProduct).setVisibility(View.GONE);
                findViewById(R.id.lPrintBarcode).setVisibility(View.GONE);
                findViewById(R.id.lDirectProduct).setVisibility(View.GONE);
                findViewById(R.id.lWarehouseOrder).setVisibility(View.GONE);
                findViewById(R.id.lReturn).setVisibility(View.GONE);
                findViewById(R.id.lStoreDirectRelation).setVisibility(View.GONE);
                findViewById(R.id.lPresentation).setVisibility(View.GONE);
                findViewById(R.id.lCompetitorList).setVisibility(View.GONE);
                findViewById(R.id.promotions_list).setVisibility(View.GONE);
                findViewById(R.id.Docs_Upload).setVisibility(View.GONE);
            } else if (userType.equals("C")) {

                findViewById(R.id.lCollectorChecking).setVisibility(View.GONE);

                findViewById(R.id.lPhoneDelivery).setVisibility(View.GONE);
                findViewById(R.id.lProductControl).setVisibility(View.GONE);
                findViewById(R.id.lWarehousing).setVisibility(View.GONE);
                findViewById(R.id.lDeliverOrder).setVisibility(View.GONE);
                findViewById(R.id.lGarbageProduct).setVisibility(View.GONE);
                findViewById(R.id.lPrintBarcode).setVisibility(View.GONE);
                findViewById(R.id.lDirectProduct).setVisibility(View.GONE);
                findViewById(R.id.lWarehouseOrder).setVisibility(View.GONE);
                findViewById(R.id.lReturn).setVisibility(View.GONE);
                findViewById(R.id.lStoreDirectRelation).setVisibility(View.GONE);
                findViewById(R.id.lPresentation).setVisibility(View.GONE);
                findViewById(R.id.lCompetitorList).setVisibility(View.GONE);
                findViewById(R.id.promotions_list).setVisibility(View.GONE);
                findViewById(R.id.Docs_Upload).setVisibility(View.GONE);
            }

        }
        else if(Utility.applicationMode == ApplicationMode.StoreHandheld)
        {
            findViewById(R.id.lPickingOrder).setVisibility(View.GONE);
            findViewById(R.id.lPhoneDelivery).setVisibility(View.GONE);
            findViewById(R.id.lStoreDirectRelation).setVisibility(View.GONE);
            findViewById(R.id.lPresentation).setVisibility(View.GONE);
            findViewById(R.id.lWarehouseCounting).setVisibility(View.GONE);
            findViewById(R.id.lCollectorChecking).setVisibility(View.GONE);
            findViewById(R.id.lCompetitorList).setVisibility(View.GONE);
        }
        else if(Utility.applicationMode == ApplicationMode.Competitor)
        {
            findViewById(R.id.lPhoneDelivery).setVisibility(View.GONE);
            findViewById(R.id.lProductControl).setVisibility(View.GONE);
            findViewById(R.id.lWarehousing).setVisibility(View.GONE);
            findViewById(R.id.lDeliverOrder).setVisibility(View.GONE);
            findViewById(R.id.lGarbageProduct).setVisibility(View.GONE);
            findViewById(R.id.lPrintBarcode).setVisibility(View.GONE);
            findViewById(R.id.lDirectProduct).setVisibility(View.GONE);
            findViewById(R.id.lWarehouseOrder).setVisibility(View.GONE);
            findViewById(R.id.lReturn).setVisibility(View.GONE);
            findViewById(R.id.lStoreDirectRelation).setVisibility(View.GONE);
            findViewById(R.id.lPresentation).setVisibility(View.GONE);
            findViewById(R.id.lPickingOrder).setVisibility(View.GONE);
            findViewById(R.id.lWarehouseCounting).setVisibility(View.GONE);
            findViewById(R.id.lCollectorChecking).setVisibility(View.GONE);
            findViewById(R.id.promotions_list).setVisibility(View.GONE);
            findViewById(R.id.Docs_Upload).setVisibility(View.GONE);
        }
    }



    private void startPushNotificationService() {
        if(!PushNotificationService.isRunning()) {
            Intent serviceIntent=new Intent(this, PushNotificationService.class);
            serviceIntent.putExtra("Service_key", "Start Notification Service");
            serviceIntent.setAction("Start");
            startService(serviceIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuAbout:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
//            case R.id.mnuExit:
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onProductControl(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ProductControlActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onCompetitorList(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, CompetitorListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onClickWarehousing(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehousingOneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onDeliverOrder(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, DeliverOrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onPickingOrder(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PickingDeliverHeaderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onClickDeliverOrder(View view)
    {

        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, StoreDeliverInboundHeaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onGarbageProduct(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, GarbageProductHeaderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onPrintBarcode(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PrintBarcodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onDirectProduct(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, DirectReceiveListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onWarehouseOrder(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehouseOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onClickReturn(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ReturnFromStoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onDirectRelation(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, StoreDirectRelationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onPresentation(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, ColleaguePresentationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onWarehouseCounting(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, WarehouseCountingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onCollectorChecking(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PickingControlHeaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onPromotionListClicked(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, PromotionsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void on_Docs_Uploade_Clicked(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, UploadDocsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }





}
