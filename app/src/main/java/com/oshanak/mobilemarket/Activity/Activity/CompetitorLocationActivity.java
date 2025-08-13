package com.oshanak.mobilemarket.Activity.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oshanak.mobilemarket.Activity.Activity.Enum.CompetitorLocationActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.R;

public class CompetitorLocationActivity extends BaseActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener
{

    private GoogleMap mMap;
    private TextView tvLocation;
    private TextView tvMessage;
    private Button bConfirm;
    private boolean cameraFixed = false;
    private Location currentLocation = null;
    private Location competitorLocation = null;
    private String competitorName = "";
    private String companyName = "";
    private CompetitorLocationActivityMode competitorLocationActivityMode = CompetitorLocationActivityMode.Unknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ///////////////
        if (Utility.restartAppIfNeed(this)) return;
        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);

        tvLocation = findViewById(R.id.tvLocation);
        tvMessage = findViewById(R.id.tvMessage);
        bConfirm = findViewById(R.id.bConfirm);

        tvLocation.setVisibility(View.GONE);
        tvMessage.setText("در مكاني سرباز مستقر شده و كمي صبر كنيد");
        tvMessage.setTextColor(Color.GRAY);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        competitorName = intent.getStringExtra("competitorName");
        companyName = intent.getStringExtra("companyName");
        if(latitude > 0 || longitude > 0)
        {
            competitorLocation = new Location("");
            competitorLocation.setLatitude(latitude);
            competitorLocation.setLongitude(longitude);
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {


        //seattle coordinates
//        LatLng seattle = new LatLng(47.6062095, -122.3320708);
//        mMap.addMarker(new MarkerOptions().position(seattle).title("Seattle"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));

        if(haveLocationPermission(this))
        {
            googleMap.setMyLocationEnabled(true);
            initGPS();
        }
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        addCompetitorToMap();
    }
    @SuppressLint("MissingPermission")
    private void initGPS()
    {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                setLocationOnMap(location);
                bConfirm.setEnabled(true);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider)
            {
                tvMessage.setText("در مكاني سرباز مستقر شده و كمي صبر كنيد");
                tvMessage.setTextColor(Color.GRAY);
                cameraFixed = false;
            }

            public void onProviderDisabled(String provider) {
                bConfirm.setEnabled(false);
                tvMessage.setText("جي.پي.اس گوشي خود را روشن كنيد");
                tvMessage.setTextColor(Color.RED);
                cameraFixed = false;
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    private void setLocationOnMap(Location location)
    {
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());

//        if(markerYourLocation != null) markerYourLocation.remove();
//        markerYourLocation = mMap.addMarker(new MarkerOptions().position(sydney).title("شما اینجا هستید"));

//        if(markerYourLocation == null)
//        {
//            markerYourLocation = mMap.addMarker(new MarkerOptions().position(coordinate).title("شما اینجا هستید"));
//            markerYourLocation.showInfoWindow();
        if(!cameraFixed) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16.0f));
            cameraFixed = true;
        }
//        tvLocation.setText("عرض جغرافيايي: " +location.getLatitude() + " , " +"طول جغرافيايي: "+ location.getLongitude());
        currentLocation = location;
        tvMessage.setText("مي توانيد موقعيت فعلي را ثبت نماييد");
        tvMessage.setTextColor(Color.BLUE);

//            startWait();
//            ProductBO productBO = new ProductBO(ProductBoMode.GetShopList);
//            productBO.nearestShopParam.Product_Code = productData.Product_Code;
//            productBO.listener = this;
//            productBO.execute();
//        }
//        else
//        {
//            markerYourLocation.setPosition(coordinate);
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
//        }
    }
    public static boolean haveLocationPermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return false;
            }
        }
        else
        {
            return true;
        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker)
    {
//        marker.showInfoWindow();
//        TextView tvLocation = (TextView)findViewById(R.id.tvLocation);
//
//        //////
//        LatLng origin = new LatLng( markerYourLocation.getPosition().latitude, markerYourLocation.getPosition().longitude);
//        LatLng dest = new LatLng( marker.getPosition().latitude, marker.getPosition().longitude);
//
//        String url = getMapsApiDirectionsUrl(origin, dest);
//        ReadTask downloadTask = new ReadTask(mMap);
//        downloadTask.execute(url);
//
//        float[] results = new float[1];
//        Location.distanceBetween(origin.latitude, origin.longitude,
//                dest.latitude, dest.longitude,
//                results);
//
//        int meter = (int)results[0];
//        tvLocation.setText( "فاصله شما تا فروشگاه" + " " + marker.getTitle() + " "
//                + String.valueOf(ThousandSeparatorWatcher.addSeparator( meter) + " " + "متر می باشد."));
        //////

        return true;
    }
    public void onClickExit(View view)
    {
        onBackPressed();
    }
    public void onClickConfirm(View view)
    {
        competitorLocationActivityMode = CompetitorLocationActivityMode.AfterConfirmLocation;
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        if(competitorLocationActivityMode == CompetitorLocationActivityMode.AfterConfirmLocation)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("latitude", currentLocation.getLatitude());
            returnIntent.putExtra("longitude", currentLocation.getLongitude());

            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void addCompetitorToMap()
    {
        if(competitorLocation != null)
        {
//            ShopData shopData = shopList.get(i);
            LatLng coordinate = new LatLng( competitorLocation.getLatitude(), competitorLocation.getLongitude());
            Marker shopMarker = mMap.addMarker(new MarkerOptions().position(coordinate).title(companyName + ", " + competitorName));
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.shop);
            shopMarker.setIcon(bd);
            shopMarker.showInfoWindow();
        }
    }

}
