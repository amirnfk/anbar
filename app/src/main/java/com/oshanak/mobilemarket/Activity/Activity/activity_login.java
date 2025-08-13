package com.oshanak.mobilemarket.Activity.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getPassword;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getStoreID;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getUserName;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.oshanak.mobilemarket.Activity.Activity.Enum.LoginActivityMode;
import com.oshanak.mobilemarket.Activity.Common.Anim;
import com.oshanak.mobilemarket.Activity.Common.AppVersion;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.LocalDB.DBHandler;
import com.oshanak.mobilemarket.Activity.LocalDB.Param;
import com.oshanak.mobilemarket.Activity.PushNotification.CreateTokenRequest;
import com.oshanak.mobilemarket.Activity.PushNotification.CreateTokenResponse;
import com.oshanak.mobilemarket.Activity.PushNotification.PushNotificationService;
import com.oshanak.mobilemarket.Activity.PushNotification.SignalerNotificationApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.createTokenClient;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Enum.GeneralServiceMode;
import com.oshanak.mobilemarket.Activity.Service.GeneralService;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ErrorResponse;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.LoginRequest;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.LoginResponse;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingApiService;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingLoginRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_login extends BaseActivity implements OnTaskCompleted
{
    public static int timeMs=5000;
    private EditText etUserName;
    private EditText etPassword;
    private LoginActivityMode loginActivityMode = LoginActivityMode.Unknown;
    private int editTextIncPercent = 30;
    private CheckBox cbRememberPassword;
    private LinearLayout lStoreID;
    private EditText etStoreID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    ProgressBar prgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ///////////////////////
        GlobalData.setAppVersionCode(AppVersion.getCurrentVersionCode(activity_login.this));

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        cbRememberPassword = findViewById(R.id.cbRememberPassword);
        lStoreID = findViewById(R.id.lStoreID);
        etStoreID = findViewById(R.id.etStoreID);
        prgLogin=findViewById(R.id.prg_login);
        prgLogin.setVisibility(GONE);
        findViewById(R.id.ivLock).setAnimation(Anim.inFromRightAnimation(1000));

        DBHandler dbHandler = new DBHandler(this);
        etUserName.setText(dbHandler.getParamValue(Param.UserName));
        cbRememberPassword.setChecked(Boolean.valueOf(dbHandler.getParamValue(Param.RememberLoginPassword))
                                        && Utility.applicationMode != ApplicationMode.StoreHandheld);
        if (cbRememberPassword.isChecked())
        {
            etPassword.setText(dbHandler.getParamValue(Param.LoginPassword));
        }
        switch (Utility.applicationMode)
        {
            case PhoneDelivery:
                this.setTitle("هفت - فروش تلفني");
                lStoreID.setVisibility(GONE);
                break;
            case PickingWarehouse:
                this.setTitle("هفت - جمع آوري انبار");
                lStoreID.setVisibility(GONE);
                break;
            case StoreHandheld:
                this.setTitle("هفت - سيستم فروشگاهي");
                etUserName.setInputType(InputType.TYPE_CLASS_NUMBER);
                etPassword.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                etStoreID.setText(dbHandler.getParamValue(Param.StoreID));
                cbRememberPassword.setVisibility(GONE);
                findViewById(R.id.cbShowPassword).setVisibility(GONE);
                etUserName.setHint("نام کاربری صندوق");
                etPassword.setHint("رمز عبور صندوق");
                break;
            case Competitor:
                this.setTitle("هفت - رُقبا");
                lStoreID.setVisibility(GONE);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.mnuConfig:
                intent = new Intent(this, ConfigActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.mnuAbout:
                intent = new Intent(this, AboutActivity.class);
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
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(findViewById(R.id.bLogin),20);
            Utility.increaseTextSize(etUserName, editTextIncPercent);
            Utility.increaseTextSize(etPassword, editTextIncPercent);
            if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                Utility.increaseTextSize(etStoreID, editTextIncPercent);
            }
        }
    }

    public void onClickRememberPassword(View view)
    {
        if (cbRememberPassword.isChecked())
        {
            Utility.simpleAlert(this,
                    "اگر گوشی یا تبلت شما توسط دیگران نیز استفاده می شود توصیه می گردد این گزینه را انتخاب نکنید."
                    , DialogIcon.Warning);
        }
    }
    public void onClickPasswordCheckBox(View view)
    {

        int inputType;
        if (((CheckBox)findViewById(R.id.cbShowPassword)).isChecked())
        {
            if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                inputType = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL;
            }
            else {
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }
        }
        else
        {
            if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                inputType = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD;
            }
            else {
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
        }
        etPassword.setInputType(inputType);
        Utility.setFont(this, etPassword);
        Utility.increaseTextSize(etPassword,editTextIncPercent);
    }
//    public void onClose(View view)
//    {
//        finish();
//    }
    public void onLogin(View view)
    {

        if (Utility.editTextIsEmpty(etUserName, "نام  كاربري را وارد نمایید.")) return;
        if (Utility.editTextIsEmpty(etPassword, "کلمه عبور را وارد کنید.")) return;

        if(Utility.applicationMode == ApplicationMode.PhoneDelivery ||

                Utility.applicationMode == ApplicationMode.Competitor)
        {
            loginActivityMode = LoginActivityMode.BeforeLogin;
            GeneralService service = new GeneralService(GeneralServiceMode.Login, this);

            service.addParam("userName", etUserName.getText().toString().trim());
            service.addParam("password", etPassword.getText().toString().trim());

            service.listener = this;
            service.execute();
            startWait();
        } else if (  Utility.applicationMode == ApplicationMode.PickingWarehouse ) {
            loginActivityMode = LoginActivityMode.BeforeLogin;

            sendPickingLoginRequest(etUserName.getText().toString().trim(),etPassword.getText().toString().trim());

        } else if(Utility.applicationMode == ApplicationMode.StoreHandheld)
        {
            if (PushNotificationService.isRunning()) {
                Intent serviceIntent = new Intent(this, PushNotificationService.class);
                serviceIntent.setAction("Stop"); // Change action to "Stop"
                stopService(serviceIntent);
            }
            sharedPreferences = getSharedPreferences("oNotifyPreferences", MODE_PRIVATE);
            sharedPreferencesEditor = sharedPreferences.edit();
            PushNotificationService.jwt="empty";
            sharedPreferencesEditor.remove("sharedJwt");
            sharedPreferencesEditor.remove("sharedExp");
            sharedPreferencesEditor.apply();


            if(!isNotification_api33_permissited()){
                return;

            }

            if (Utility.editTextIsEmpty(etStoreID, "کد فروشگاه را وارد کنید.")) return;
            loginActivityMode = LoginActivityMode.BeforeLogin;

//            String StoreId= etStoreID.getText().toString().trim();
//            String UserName= etUserName.getText().toString().trim();
//            String UserPass= etPassword.getText().toString().trim();

            performLogin();

//            SendStoreHandHeldLoginRequest(StoreId,UserName,UserPass);

//            StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.Login, this);
//
//            PropertyInfo pi;
//
//            pi = new PropertyInfo();
//            pi.setName("storeId");
//            pi.setValue(etStoreID.getText().toString().trim());
//            service.piList.add(pi);
//
//            pi = new PropertyInfo();
//            pi.setName("userName");
//            pi.setValue(etUserName.getText().toString().trim());
//            service.piList.add(pi);
//
//            pi = new PropertyInfo();
//            pi.setName("password");
//            pi.setValue(etPassword.getText().toString().trim());
//            service.piList.add(pi);
//
//            pi = new PropertyInfo();
//            pi.setName("deviceIP");
//            pi.setValue(Utility.getIPAddress(true));
//            service.piList.add(pi);
//            pi = new PropertyInfo();
//            pi.setName("deviceMAC");
//            pi.setValue(Utility.getMACAddress(this));
//            service.piList.add(pi);
//            service.listener = this;
//            service.execute();
//
//            startWait();
        }
    }

    public void onOrderListEntry(View view){
Intent intent=new Intent(activity_login.this,ItemPriceListActivity.class);
startActivity(intent);
    }


    private void performLogin() {

        prgLogin.setVisibility(VISIBLE);
                    String storeId= etStoreID.getText().toString().trim();
            String staffId= etUserName.getText().toString().trim();
            String password= etPassword.getText().toString().trim();
        
        
   

        // Basic validation
        if (storeId.isEmpty() || staffId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "لطفا اطلاعات ورودی را به طور کامل پر کنید", Toast.LENGTH_SHORT).show();
            prgLogin.setVisibility(GONE);
            return;
        }

        LoginRequest loginRequest = new LoginRequest(storeId, staffId, password);

        SignalerNotificationApiService apiService = createTokenClient.getApiService();
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    prgLogin.setVisibility(GONE);
                    if (loginResponse.isSuccess()) {
                        // Login successful
//                        Toast.makeText(activity_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Log.d("checkLoginx1",call.request().toString());
                        Log.d("checkLoginx2",response.body().toString());
                        DBHandler dbHandler = new DBHandler(activity_login.this);
                        GlobalData.setUserName( storeId);
                        GlobalData.setPassword( password);


                        GlobalData.setStoreID(storeId);
                        GlobalData.setStoreName(storeId);

                        dbHandler.setParamValue(Param.StoreID, etStoreID.getText().toString().trim());

                        dbHandler.setParamValue(Param.UserName, GlobalData.getUserName());
                        dbHandler.setParamValue(Param.RememberLoginPassword, String.valueOf( cbRememberPassword.isChecked()));
                        if(cbRememberPassword.isChecked())
                        {
                            dbHandler.setParamValue(Param.LoginPassword, etPassword.getText().toString().trim());
                        }
                        else
                        {
                            dbHandler.setParamValue(Param.LoginPassword, "");
                        }

                        Intent intent = new Intent(activity_login.this, MainMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        Log.d("checkLoginx11",call.request().toString());
                        Log.d("checkLoginx22",response.body().toString());
                        // Handle API error
                        handleError(loginResponse.getError());
                    }
                } else{
                    prgLogin.setVisibility(GONE);
                    Log.d("checkLoginx111",call.request().toString());
                    Log.d("checkLoginx222",response.toString());
                    // Handle HTTP error
                    Toast.makeText(activity_login.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                prgLogin.setVisibility(GONE);
                Log.d("checkLoginx1111",call.request().toString());
                Log.d("checkLoginx2222",t.getMessage() .toString());
                Toast.makeText(activity_login.this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_SHORT).show();
                Log.e("LoginError", t.getMessage());
            }
        });
    }

    private void handleError(ErrorResponse error) {
        String errorMessage;
        switch (error.getErrorCode()) {
            case 500:
                errorMessage = "خطای داخلی شبکه";
                break;
            case 501:
                errorMessage = "وارد کردن پارامترهای نام کاربری ، شناسه فروشگاه و رمز عبور اجباری است";
                break;
            case 502:
                errorMessage = " وارد کردن شناسه فروشگاه اجباری است";
                break;
            case 503:
                errorMessage = " وارد کردن نام کاربری اجباری است ";
                break;
            case 504:
                errorMessage = " وارد کردن رمز عبور اجباری است";
                break;
            case 505:
                errorMessage = "کد فروشگاه نامعتبر است";
                break;
            case 506:
                errorMessage = "کاربر به فروشگاه دسترسی ندارد";
                break;
            case 507:
                errorMessage = "نام کاربری یا رمز عبور نامعتبر می باشد";
                break;
            default:
                errorMessage = "خطای ارتباط با سرور";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void SendStoreHandHeldLoginRequest(String storeId, String userName, String userPass) {



            SignalerNotificationApiService apiService = createTokenClient.getApiService();

            CreateTokenRequest request = new CreateTokenRequest(storeId, userName, userPass);
            Call<CreateTokenResponse> call = apiService.createToken(request);

            call.enqueue(new Callback<CreateTokenResponse>() {
                @Override
                public void onResponse(Call<CreateTokenResponse> call, retrofit2.Response<CreateTokenResponse> response) {


                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("checkLoginx1",call.request().toString());
                        Log.d("checkLoginx2",response.body().toString());
                        DBHandler dbHandler = new DBHandler(activity_login.this);
                        GlobalData.setUserName( userName);
                        GlobalData.setPassword( userPass);


                                GlobalData.setStoreID(storeId);
                                GlobalData.setStoreName(storeId);



                            dbHandler.setParamValue(Param.StoreID, etStoreID.getText().toString().trim());

                        dbHandler.setParamValue(Param.UserName, GlobalData.getUserName());
                        dbHandler.setParamValue(Param.RememberLoginPassword, String.valueOf( cbRememberPassword.isChecked()));
                        if(cbRememberPassword.isChecked())
                        {
                            dbHandler.setParamValue(Param.LoginPassword, etPassword.getText().toString().trim());
                        }
                        else
                        {
                            dbHandler.setParamValue(Param.LoginPassword, "");
                        }

                        Intent intent = new Intent(activity_login.this, MainMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);


                    } else {
                        Log.d("checkLoginx3",response.toString());
                    }
                }

                @Override
                public void onFailure(Call<CreateTokenResponse> call, Throwable t) {
                    Log.d("checkLoginx4",t.getMessage().toString());
                    Log.d("checkLoginx5",call.request().toString());
                }
            });

    }



    private void sendPickingLoginRequest(String userName, String password) {
        PickingApiService PickingapiService = PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

        MetaData metaData = new MetaData(userName, Utilities.getApkVersionCode(activity_login.this),  password, "Picking", "Info", "");
        PickingLoginRequest loginRequest = new PickingLoginRequest(userName, password, metaData);

        Call<PickingLoginResponse> call = PickingapiService.loginUser(loginRequest);
        call.enqueue(new Callback<PickingLoginResponse>() {
            @Override
            public void onResponse(Call<PickingLoginResponse> call, Response<PickingLoginResponse> response) {
                if (response.isSuccessful()) {
                    PickingLoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.isSuccessful()) {
                        DBHandler dbHandler = new DBHandler(activity_login.this);
                        GlobalData.setUserName( etUserName.getText().toString().trim());
                        GlobalData.setPassword( etPassword.getText().toString().trim());

                        dbHandler.setParamValue(Param.UserName, GlobalData.getUserName());
                        dbHandler.setParamValue(Param.RememberLoginPassword, String.valueOf( cbRememberPassword.isChecked()));
                        if(cbRememberPassword.isChecked())
                        {
                            dbHandler.setParamValue(Param.LoginPassword, etPassword.getText().toString().trim());
                        }
                        else
                        {
                            dbHandler.setParamValue(Param.LoginPassword, "");
                        }


                        Intent intent = new Intent(activity_login.this, MainMenuActivity_Picking.class);

                        intent.putExtra("userType",response.body().getUserType());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
//                        Toast.makeText(activity_login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Utility.showFailureToast(activity_login.this,"اطلاعات وارد شده صحیح نمی باشد");
//                        Toast.makeText(activity_login.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utility.showFailureToast(activity_login.this,"اطلاعات وارد شده صحیح نمی باشد");
                }
            }

            @Override
            public void onFailure(Call<PickingLoginResponse> call, Throwable t) {
                Utility.showFailureToast(activity_login.this,"ارتباط با سرور برقرار نشد.");
            }
        });
    }




    private boolean isNotification_api33_permissited() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 98);
                return false;
            }else {
                return true;
            }
        }
        return true;
    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(loginActivityMode == LoginActivityMode.BeforeLogin  )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("Login failed by"))
            {
                Utility.simpleAlert(this,"نام كاربري یا کلمه عبور اشتباه است!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful &&
                    (taskResult.isExceptionOccured("اشکال در دسترسی به فروشگاه") ||
                            taskResult.isExceptionOccured("The request failed with HTTP status 401: Unauthorized.")))
            {
                Utility.simpleAlert(this,"اشکال در دسترسی به فروشگاه", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("نام کاربري و يا کلمه عبور اشتباه مي باشد"))
            {
                Utility.simpleAlert(this,"نام کاربري و يا کلمه عبور اشتباه مي باشد", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("does not have access store"))
            {
                Utility.simpleAlert(this,"شما به فروشگاه مورد نظر دسترسی ندارید.", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("not find staff user"))
            {
                Utility.simpleAlert(this,"نام کاربری وارد شده صحیح نیست.", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("password is not valid "))
            {
                Utility.simpleAlert(this,"کلمه عبور اشتباه است.", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful)
            {

                Utility.simpleAlert(this,getString(R.string.general_error),DialogIcon.Error);
                return;
            }
            DBHandler dbHandler = new DBHandler(this);
            GlobalData.setUserName( etUserName.getText().toString().trim());
            GlobalData.setPassword( etPassword.getText().toString().trim());
            if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                String[] param = ((String)taskResult.dataStructure).split(",");
                if(param.length > 1)
                {
                    GlobalData.setStoreID(param[0]);
                    GlobalData.setStoreName(param[1]);
                    String TimeMs=param[2];
                    timeMs=Integer.parseInt(TimeMs);

                }
                dbHandler.setParamValue(Param.StoreID, etStoreID.getText().toString().trim());
            }
            dbHandler.setParamValue(Param.UserName, GlobalData.getUserName());
            dbHandler.setParamValue(Param.RememberLoginPassword, String.valueOf( cbRememberPassword.isChecked()));
            if(cbRememberPassword.isChecked())
            {
                dbHandler.setParamValue(Param.LoginPassword, etPassword.getText().toString().trim());
            }
            else
            {
                dbHandler.setParamValue(Param.LoginPassword, "");
            }
            if(Utility.applicationMode == ApplicationMode.StoreHandheld){
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            } else if (Utility.applicationMode == ApplicationMode.PickingWarehouse) {
                Intent intent = new Intent(this, MainMenuActivity_Picking.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED && requestCode != 98)
        {
            new Common(this).GetAPK(activity_login.this);
        }
        if (requestCode == 98) {

             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

              } else {
                 AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                 dlgAlert.setMessage("لطفا اجازه نمایش نوتیفیکیشن را به اپلیکیشن فروشگاهی اعطا کنید");
                  dlgAlert.setCancelable(false);
                 dlgAlert.setPositiveButton("اوکی",
                         new DialogInterface.OnClickListener()
                         {
                             public void onClick(DialogInterface dialog, int which)
                             {
                                 openAppDetailsSettings();
                             }
                         });
                 dlgAlert.setIcon(R.drawable.question128);
                 dlgAlert.create().show();

            }
        }
    }


    private void openAppDetailsSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo (API 26) and above, navigate to notification settings
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else {
            // For Android versions prior to Oreo, navigate to app details settings
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }

        startActivity(intent);

    }



}
