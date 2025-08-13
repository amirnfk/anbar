package com.oshanak.mobilemarket.Activity.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.oshanak.mobilemarket.Activity.Activity.Enum.DeliverOrderLocationActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CustomerAddress;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GeneralConfigData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.DeliverOrderService;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

public class DeliverOrderLocationActivity extends BaseActivity implements OnTaskCompleted
{

    private TextView tvCustomerLon;
    private TextView tvCustomerLat;
    private TextView tvCurrentLon;
    private TextView tvCurrentLat;
    private TextView tvMaxDistance;
    private TextView tvMessage;
    private Button bConfirm;
    private DeliverOrderData deliverOrderData;
    int REQUEST_CHECK_SETTINGS = 1;
    private DeliverOrderLocationActivityMode deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.Unknown;
    private boolean controlDeliverLocation;
    private int maxDistanceToCustomer;
    TextView tvNoLocationCustomer;
    Button bConfirmCustomerLocation;
    View lCustomerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_location);
        ///////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCustomerLon = findViewById(R.id.tvCustomerLon);
        tvCustomerLat = findViewById(R.id.tvCustomerLat);
        tvCurrentLon = findViewById(R.id.tvCurrentLon);
        tvCurrentLat = findViewById(R.id.tvCurrentLat);
        bConfirm = findViewById(R.id.bConfirm);
        tvMessage = findViewById(R.id.tvMessage);
        tvMaxDistance = findViewById(R.id.tvMaxDistance);
        tvNoLocationCustomer = findViewById(R.id.tvNoLocationCustomer);
        bConfirmCustomerLocation = findViewById(R.id.bConfirmCustomerLocation);
        lCustomerLocation = findViewById(R.id.lCustomerLocation);
        tvMaxDistance.setVisibility(View.GONE);

        Intent intent = getIntent();
        deliverOrderData = (DeliverOrderData) intent.getSerializableExtra("deliverOrderData");
        tvCustomerLon.setText(String.valueOf( deliverOrderData.getCustomerLon()));
        tvCustomerLat.setText(String.valueOf( deliverOrderData.getCustomerLat()));

        if(customerHasLocation())
        {
            tvNoLocationCustomer.setVisibility(View.GONE);
            bConfirmCustomerLocation.setVisibility(View.GONE);
            lCustomerLocation.setVisibility(View.GONE);
        }

        getConfigs();
    }
    private void getConfigs()
    {
//        reset();
//        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
//        {
//            return;
//        }

        deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.BeforeGetConfigs;
        DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.GetDeliverOrderConfigs, this);
//        PropertyInfo pi;

//        pi = new PropertyInfo();
//        pi.setName("UserName");
//        pi.setValue(GlobalData.getUserName());
//        task.piList.add(pi);

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(deliverOrderLocationActivityMode == DeliverOrderLocationActivityMode.BeforeGetConfigs)
        {
//            reset();
            if(taskResult == null) return;

            if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data),"", DialogIcon.Error, onFinishClick);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data),"", DialogIcon.Error, onFinishClick);
                return;
            }
            ArrayList<GeneralConfigData> list = (ArrayList<GeneralConfigData>) taskResult.dataStructure;

            for(int i = 0; i < list.size(); i++)
            {
                if(list.get(i).getName().equalsIgnoreCase("Phone_Delivery_Control_Deliver_Location"))
                {
                    controlDeliverLocation = list.get(i).getValue().equals("1");
                }
                else if(list.get(i).getName().equalsIgnoreCase("Phone_Delivery_Max_Distance_To_Customer"))
                {
                    maxDistanceToCustomer = Integer.parseInt( list.get(i).getValue());
                }
            }
            if(!customerHasLocation())
            {
                Utility.enableViews(this, false, bConfirm);
                Utility.enableViews(this, false, bConfirmCustomerLocation);
            }
            else {
                Utility.enableViews(this, !controlDeliverLocation, bConfirm);
            }

            if(controlDeliverLocation)
            {
                tvMaxDistance.setVisibility(View.VISIBLE);
                tvMaxDistance.setText("حداکثر فاصله مجاز تا مشتری " + ThousandSeparatorWatcher.addSeparator(maxDistanceToCustomer) + " متر می باشد.");
                setStatusFalse();
            }
            else
            {
                tvMaxDistance.setVisibility(View.GONE);
            }

//            createLocationRequest();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.AfterGetConfigs;
        }
        else if(deliverOrderLocationActivityMode == DeliverOrderLocationActivityMode.BeforeGetCustomerAddress)
        {
//            reset();
//            if(taskResult == null) return;
//

            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data),"", DialogIcon.Error, onFinishClick);
                return;
            }
            ArrayList<CustomerAddress> list = (ArrayList<CustomerAddress>) taskResult.dataStructure;
//
            deliverOrderData.setCustomerLat(list.get(0).getLatitude());
            deliverOrderData.setCustomerLon(list.get(0).getLongitude());
            deliverOrderData.setCustomerAddress(list.get(0).getAddress());
            tvCustomerLon.setText(String.valueOf( deliverOrderData.getCustomerLon()));
            tvCustomerLat.setText(String.valueOf( deliverOrderData.getCustomerLat()));
//            for(int i = 0; i < list.size(); i++)
//            {
//                if(list.get(i).getName().equalsIgnoreCase("Phone_Delivery_Control_Deliver_Location"))
//                {
//                    controlDeliverLocation = list.get(i).getValue().equals("1");
//                }
//                else if(list.get(i).getName().equalsIgnoreCase("Phone_Delivery_Max_Distance_To_Customer"))
//                {
//                    maxDistanceToCustomer = Integer.parseInt( list.get(i).getValue());
//                }
//            }
            if(!customerHasLocation())
            {
                Utility.enableViews(this, false, bConfirm);
                Utility.enableViews(this, false, bConfirmCustomerLocation);
                tvNoLocationCustomer.setVisibility(View.VISIBLE);
                bConfirmCustomerLocation.setVisibility(View.VISIBLE);
                lCustomerLocation.setVisibility(View.VISIBLE);
            }
            else {
                Utility.enableViews(this, !controlDeliverLocation, bConfirm);
                tvNoLocationCustomer.setVisibility(View.GONE);
                bConfirmCustomerLocation.setVisibility(View.GONE);
                lCustomerLocation.setVisibility(View.GONE);
            }
            dataChanged = true;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.AfterGetCustomerAddress;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    createLocationRequest();
                } else
                {
                    Utility.simpleAlert(this,"مجوز دسترسی به GPS برنامه داده نشده است. به تنظیمات برنامه در سیستم اندروید مراجعه نموده و مجوز لازم را عطا نمایید."
                            ,"", DialogIcon.Warning, onFinishClick);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    DialogInterface.OnClickListener onFinishClick= new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            onBackPressed();
        }
    };
    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());



        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                Toast.makeText(DeliverOrderLocationActivity.this, "Gps already open",
                        Toast.LENGTH_SHORT).show();

                getCurrentLocation();


            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(DeliverOrderLocationActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CHECK_SETTINGS){

            if(resultCode==RESULT_OK){

                Toast.makeText(this, "جی پی اس فعال شد", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                //if user allows to open gps


            }else if(resultCode==RESULT_CANCELED){

                Toast.makeText(this, "جی پی اس فعال نشد",
                        Toast.LENGTH_SHORT).show();
                setStatusFalse();
                // in case user back press or refuses to open gps

            }
        }
    }

    private static double longitude;
    private static double latitude;
    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        try {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException ex)
        {
            if(ex.getMessage().toLowerCase().contains("ACCESS_FINE_LOCATION".toLowerCase()) ||
                    ex.getMessage().toLowerCase().contains("ACCESS_COARSE_LOCATION".toLowerCase()))
            {
                Utility.simpleAlert(this,"مجوز دسترسی به GPS برنامه داده نشده است. به تنظیمات برنامه در سیستم اندروید مراجعه نموده و مجوز لازم را عطا نمایید."
                        ,"", DialogIcon.Warning, onFinishClick);
            }
            return;
        }

        final double[] longitude = {0};
        final double[] latitude = {0};
        if (location == null) {
            setStatusFalse();
        } else {
            longitude[0] = location.getLongitude();
            latitude[0] = location.getLatitude();
        }

        final LocationListener locationListener = new LocationListener()
        {

            public void onLocationChanged(Location location)
            {
                try
                {   longitude[0] = location.getLongitude();
                    latitude[0] = location.getLatitude();
                    DeliverOrderLocationActivity.longitude = longitude[0];
                    DeliverOrderLocationActivity.latitude = latitude[0];

                    tvCurrentLat.setText(String.valueOf(latitude[0]));
                    tvCurrentLon.setText(String.valueOf(longitude[0]));
                    double distance = distance(deliverOrderData.getCustomerLat(), deliverOrderData.getCustomerLon(),
                            latitude[0], longitude[0]);
                    distance = (int) distance;

                    tvMessage.setTextColor(Color.BLUE);
                    String message = "فاصله شما تا محل مشتری " + ThousandSeparatorWatcher.addSeparator(distance) + " متر می باشد.";
                    tvMessage.setText(message);
                    if(!customerHasLocation())
                    {
                        Utility.enableViews(DeliverOrderLocationActivity.this, false, bConfirm);
                        Utility.enableViews(DeliverOrderLocationActivity.this, true, bConfirmCustomerLocation);
                    }
                    else if(controlDeliverLocation && distance > maxDistanceToCustomer)
                    {
                        Utility.enableViews(DeliverOrderLocationActivity.this, false, bConfirm);
                    }
                    else
                    {
                        Utility.enableViews(DeliverOrderLocationActivity.this, true, bConfirm);
                    }
                }catch (Exception e)
                {
                    String s = e.getMessage();
                    setStatusFalse();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Called when the provider status changes.
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                setStatusFalse();
            }

        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    private void setStatusFalse()
    {
        latitude = 0;
        longitude = 0;
        tvCurrentLat.setText(String.valueOf(latitude));
        tvCurrentLon.setText(String.valueOf(longitude));
        tvMessage.setTextColor(Color.GRAY);
        tvMessage.setText("مطمئن شوید GPS گوشی روشن است و در مکانی سرباز قرار بگیرید.");
        if(!customerHasLocation()) {
            Utility.enableViews(this, false, bConfirm);
        }
        else if(controlDeliverLocation) {
            Utility.enableViews(this, false, bConfirm);
        }
        Utility.enableViews(this, false, bConfirmCustomerLocation);
    }
    private boolean customerHasLocation()
    {
        return deliverOrderData.getCustomerLon() != 0 && deliverOrderData.getCustomerLat() != 0;
    }
    private double distance(double lat_From, double long_From, double lat_To, double long_To)
    {
        if(!customerHasLocation())
        {
            return 0;
        }
        double degred;
        double a;
        double c;
        double r;
        double dlon;
        double dlat;

        degred = Math.PI / 180;
        lat_From = lat_From * degred;
        long_From = long_From * degred;
        lat_To = lat_To * degred;
        long_To = long_To * degred;
        dlat = lat_To - lat_From;
        dlon = long_To - long_From;
        a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(lat_From) * Math.cos(lat_To) * Math.pow(Math.sin(dlon/2), 2);
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        r = 6367.0;
        return (r * c) * 1000; // KM to Meter
    }

    public void onExit(View view)
    {
        onBackPressed();
    }
    public void onConfirm(View view)
    {
        deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.AfterConfirmLocation;
        onBackPressed();
    }
    private boolean updateCustomerLocation = false;
    public void onConfirmCustomerLocation(View view)
    {
        deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.AfterConfirmLocation;
        updateCustomerLocation = true;
        onBackPressed();
    }
    private boolean dataChanged = false;
    @Override
    public void onBackPressed()
    {
        if(deliverOrderLocationActivityMode == DeliverOrderLocationActivityMode.AfterConfirmLocation)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("latitude", latitude);
            returnIntent.putExtra("longitude", longitude);
            returnIntent.putExtra("updateCustomerLocation", updateCustomerLocation);
            if(updateCustomerLocation)
            {
                deliverOrderData.setCustomerLat(latitude);
                deliverOrderData.setCustomerLon(longitude);
                dataChanged = true;
            }
            returnIntent.putExtra("deliverOrderData", deliverOrderData);
            returnIntent.putExtra("dataChanged", dataChanged);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        else if(dataChanged)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("deliverOrderData", deliverOrderData);
            returnIntent.putExtra("dataChanged", dataChanged);
            returnIntent.putExtra("onlyDataChanged", true);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void onRefreshLocation(View view)
    {
//        reset();

        deliverOrderLocationActivityMode = DeliverOrderLocationActivityMode.BeforeGetCustomerAddress;
        DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.GetCustomerAddress, this);

        task.addParam("Id", deliverOrderData.getAddressId());

        task.listener = this;
        task.execute();
        startWait();
    }
}