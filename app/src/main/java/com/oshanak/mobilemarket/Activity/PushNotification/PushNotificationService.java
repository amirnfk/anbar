


package com.oshanak.mobilemarket.Activity.PushNotification;


import static com.oshanak.mobilemarket.Activity.Activity.activity_login.timeMs;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getAppVersionCode;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getPassword;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getStoreID;
import static com.oshanak.mobilemarket.Activity.DataStructure.GlobalData.getUserName;
import static com.oshanak.mobilemarket.Activity.Enum.ApplicationMode.StoreHandheld;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.oshanak.mobilemarket.Activity.Activity.NotificationListActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.LocalDB.Notifications_DB;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class PushNotificationService extends Service {

    private HubConnection hubConnection;


    public static long expirationDateTime = 0;
    public static String jwt = "empty";

    private String serverUrl = "https://onotify.oshanak.com/notifications?token=";
    private static final String AUDIO_URL = "https://onotify.oshanak.com:8443/Home/Api/Notify/Voice?fileName=";
    private final String AUTH_HEADER = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";
    private MediaPlayer mediaPlayer;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    Timer timer;
    private String CHANNEL_ID = "channelId";
    private NotificationManager notifyManager;
    private static boolean isRunning;
    Notifications_DB notifications_db;
    RequestQueue requestQueue = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        timer.cancel();
        stopSelf();
        super.onDestroy();


    }

    @Override
    public void onCreate() {


        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }



        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();
        requestQueue = Volley.newRequestQueue(this);
        notifications_db = new Notifications_DB(this);


        setUpSharePreferences();



        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Acquire wake lock
                wakeLock.acquire(3000); // 3 seconds
                // Schedule the next execution
                handler.postDelayed(this, 10000); // 10 seconds
            }
        };

// Start the periodic task
        handler.post(runnable);

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning) {

                            Common common = new Common(getApplicationContext());

                            if (common.URL().contains("storehandheld")) {
                                sendSignalarRequest();

                            }


                        }else{
                            Intent restartServiceIntent = new Intent(getApplicationContext(), PushNotificationService.class);
                            restartServiceIntent.setPackage(getPackageName());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(restartServiceIntent);
                            }

                        }
                    }
                });
                thread.start();


            }
//            }

        }, 5000, timeMs);

        isRunning = true;
        super.onCreate();
    }

    private void setUpSharePreferences() {
        sharedPreferences = getSharedPreferences("oNotifyPreferences", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        jwt = sharedPreferences.getString("sharedJwt", "empty");
        expirationDateTime = sharedPreferences.getLong("sharedExp", 0);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            if ("Start".equals(action)) {
                // Start service operations
                isRunning = true;

                // Create a notification channel if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String offerChannelName = "Service Channel";
                    String offerChannelDescription = "Delivery Service Channel";
                    int offerChannelImportance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notifyChannel = new NotificationChannel(CHANNEL_ID, offerChannelName, offerChannelImportance);
                    notifyChannel.setDescription(offerChannelDescription);
                    notifyManager = getSystemService(NotificationManager.class);
                    notifyManager.createNotificationChannel(notifyChannel);
                }

                // Create a pending intent to stop the service
                Intent stopIntent = new Intent(getApplicationContext(), PushNotificationService.class);
                stopIntent.setAction("Stop"); // Set action for stopping
                PendingIntent pStopSelf = PendingIntent.getService(getApplicationContext(), 0, stopIntent, PendingIntent.FLAG_MUTABLE);

                // Set up the notification layout
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.service_layout);
                contentView.setOnClickPendingIntent(R.id.img_turn_off_service, pStopSelf);

                // Build the notification
                NotificationCompat.Builder sNotifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification)
                        .setColor(getResources().getColor(R.color.colorBlueFont))
                        .setContent(contentView);
                Notification notification = sNotifBuilder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

                // Start the service in the foreground
                startForeground(1, notification);

            } else if ("Stop".equals(action)) {
                // Stop the service operations
                isRunning = false;

                // Perform any cleanup if needed

                stopSelf(); // Stop the service
                return START_NOT_STICKY; // Prevents the service from restarting

            }
        }

        return START_STICKY; // Keep running the service until explicitly stopped
    }



    public void showCustomNotification(Context context, String title, String body, String voiceUrl, String imageUrl, int type) {
               NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a NotificationChannel (required for API 26+)
        String channelId = "custom_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Custom Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Create RemoteViews for custom notification layout
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        customLayout.setImageViewResource(R.id.notification_image, R.drawable.haft_farsi);


        customLayout.setTextViewText(R.id.notification_title, title);
        customLayout.setTextViewText(R.id.notification_text, body);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.haft_farsi) // set small icon
                .setAutoCancel(true) // dismiss on click
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle()) // use the custom layout
                .setContent(customLayout); // set the custom layout





    Intent notificationIntent = new Intent(this, NotificationListActivity.class);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    PendingIntent   pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    builder.setContentIntent(pendingIntent);
    long currentTimeInSeconds = System.currentTimeMillis() / 1000;
    notifyManager.notify((int) currentTimeInSeconds, builder.build());

    }



    private void sendSignalarRequest(){


        long currentTimeInSeconds = System.currentTimeMillis() / 1000;


        if (expirationDateTime == 0 || expirationDateTime < currentTimeInSeconds || jwt.equals("empty")) {
            createToken();
        } else {
            tryToCennect(jwt);
        }



    }

    private void createToken() {

        SignalerNotificationApiService apiService = createTokenClient.getApiService();

        CreateTokenRequest request = new CreateTokenRequest(getStoreID(), getUserName(), getPassword());
        Call<CreateTokenResponse> call = apiService.createToken(request);

        call.enqueue(new Callback<CreateTokenResponse>() {
            @Override
            public void onResponse(Call<CreateTokenResponse> call, retrofit2.Response<CreateTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    jwt = response.body().getJwtToken();
                    expirationDateTime = response.body().getExpirationDateTime();

                    tryToCennect(jwt);


                    sharedPreferencesEditor.putString("sharedJwt", jwt); // Example for String
                    sharedPreferencesEditor.putLong("sharedExp", expirationDateTime);         // Example for boolean
                    sharedPreferencesEditor.apply();


                    long currentTimeInSeconds = System.currentTimeMillis() / 1000;



                } else {

                }
            }

            @Override
            public void onFailure(Call<CreateTokenResponse> call, Throwable t) {

            }
        });
    }

    private void tryToCennect(String __jwt) {

        hubConnection = HubConnectionBuilder.create(serverUrl + __jwt)
                .build();

        hubConnection.start().subscribe(() -> {


        }, throwable -> {
            Log.d("checkTheError3",throwable.getMessage());
        });


        hubConnection.on("ReceiveMessage", (List<Map<String, Object>> notifications) -> {
            // Handle the received list of notifications
            List<NotifHeaderDs> notifList = new ArrayList<>();
            Gson gson = new Gson();

            for (Map<String, Object> notifData : notifications) {


                // Convert the map to a JSON string
                String json = gson.toJson(notifData);

                // Deserialize the JSON to a NotificationDto object
                NotificationDto notificationDto = gson.fromJson(json, NotificationDto.class);

                // Now you can access all fields through NotificationDto


                NotifHeaderDs notif = new NotifHeaderDs(
                        notificationDto.getTitle(),
                        notificationDto.getBody(),
                        notificationDto.getImageUrl(),
                        notificationDto.getVoiceUrl(),
                        notificationDto.getType()
                );

                notifList.add(notif);

            }

            if (notifList.size()==1){
                NotifHeaderDs _notif=notifList.get(0);
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                try {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
                } catch (Exception e) {
                    Toast.makeText(this, "گوشی را از حالت سایلنت خارج نمایید", Toast.LENGTH_SHORT).show();
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
                }


                if (_notif.getType() == 1 ) {

                    Uri mp3Uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.public_alarm);
                    try {
                        Uri notification = mp3Uri;
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        // Todo

                        r.play();
                    } catch (Exception e) {

                    }
                }else if (_notif.getType() == 2 ) {

                    Uri mp3Uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.seven_kala);
                    try {
                        Uri notification = mp3Uri;
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        // Todo

                        r.play();
                    } catch (Exception e) {

                    }
                }else if (_notif.getType() == 3) {
                    Uri mp3Uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.critical);
                    try {
                        Uri notification = mp3Uri;
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        // Todo

                        r.play();
                    } catch (Exception e) {


                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            downloadAndPlayAudio(AUDIO_URL + _notif.getVoiceUrl(), AUTH_HEADER);
                        }
                    }, 7000);
                }
                showCustomNotification(getApplicationContext(), _notif.getTitle(),
                        _notif.getBody(),
                        _notif.getVoiceUrl(),
                        _notif.getImageUrl(),
                        _notif.getType());
            }else if(notifList.size()>1){

                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                try {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
                } catch (Exception e) {
                    Toast.makeText(this, "گوشی را از حالت سایلنت خارج نمایید", Toast.LENGTH_SHORT).show();
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
                }
                Uri mp3Uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif_list);
                try {
                    Uri notification = mp3Uri;
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    // Todo

                    r.play();
                } catch (Exception e) {

                }

                for (NotifHeaderDs notif:notifList
                     ) {

                    showCustomNotification(getApplicationContext(), notif.getTitle(),
                            notif.getBody(),
                            notif.getVoiceUrl(),
                            notif.getImageUrl(),
                            notif.getType());

                    if( notif.getType()==3) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                downloadAndPlayAudio(AUDIO_URL + notif.getVoiceUrl(), AUTH_HEADER);
                            }
                        }, 7000);
                    }
                }


            }

        }, List.class);





    }

    private void downloadAndPlayAudio(String url, String auth) {
        SignalerNotificationApiService apiService = SignalerNotificationApiClient.getClient(auth).create(SignalerNotificationApiService.class);
        Call<ResponseBody> call = apiService.downloadAudio(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle the response
                    saveFileAndPlay(response.body());
                } else {

//                    Toast.makeText( getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText( getApplicationContext(), "Download failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFileAndPlay(ResponseBody body) {
        try {
            // Create a temp file to save the downloaded audio
            File tempFile = File.createTempFile("audio", ".mp3", getCacheDir());
            tempFile.deleteOnExit(); // Delete file on exit

            // Write the file to storage
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();

                // Play the audio
                playAudio(tempFile);
            } finally {
                // Close streams
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {

            Toast.makeText( this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio(File audioFile) {
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        try {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
        } catch (Exception e) {
            Toast.makeText(this, "گوشی را از حالت سایلنت خارج نمایید", Toast.LENGTH_SHORT).show();
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare(); // prepare the media player
            mediaPlayer.start(); // start playback
        } catch (IOException e) {

//            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), PushNotificationService.class);
        restartServiceIntent.setPackage(getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartServiceIntent);
        }
        super.onTaskRemoved(rootIntent);
    }

}











