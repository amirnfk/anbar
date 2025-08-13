package com.oshanak.mobilemarket.Activity.Activity;




import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Models.NotificationPreviewRequest;
import com.oshanak.mobilemarket.Activity.PushNotification.Notification;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRestApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRetrofitClient;
import com.oshanak.mobilemarket.Activity.PushNotification.SignalerNotificationApiClient;
import com.oshanak.mobilemarket.Activity.PushNotification.SignalerNotificationApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.previewResponse;
import com.oshanak.mobilemarket.Activity.RowAdapter.NotificationAdapter;
import com.oshanak.mobilemarket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity implements NotificationAdapter.OnAudioClickListener ,  NotificationAdapter.OnNotificationClickListener{

    private final String AUTH_HEADER = "Basic bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";
    private MediaPlayer mediaPlayer;
    private String currentPlayingAudioUrl;
    private int currentPlayingPosition = -1; // Store the position of the currently playing item

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    ImageView imgRefresh;
    List<Notification> notificationList;

    List<Notification> SalesNotificationList;
    List<Notification> AnnouncementNotificationList;
    ProgressBar prg;
    SearchView searchView ;
    TextView txtFilterAll;
    TextView txtFilterSalesNotif;
    TextView txtFilterPubclicNotifs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color
        imgRefresh=findViewById(R.id.img_notif_refresh);
        prg=findViewById(R.id.prg_notif_list);
        prg.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.notifsRecyclerView);
        notificationList=new ArrayList<>();

        SalesNotificationList=new ArrayList<>();
        AnnouncementNotificationList=new ArrayList<>();
        txtFilterAll=findViewById(R.id.txt_filter_all);
        txtFilterSalesNotif=findViewById(R.id.txt_filter_sales);
        txtFilterPubclicNotifs=findViewById(R.id.txt_filter_etelaresani_zaruri);
txtFilterAll.setSelected(true);

        txtFilterSalesNotif.setSelected(false);
        txtFilterSalesNotif.setSelected(false);
        txtFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.setVisibility(View.VISIBLE);
                txtFilterSalesNotif.setSelected(false);
                txtFilterAll.setSelected(true);
                txtFilterPubclicNotifs.setSelected(false);

                showNotificationList( notificationList);
            }
        });
        txtFilterSalesNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.setVisibility(View.VISIBLE);
                txtFilterSalesNotif.setSelected(true);
                txtFilterAll.setSelected(false);
                txtFilterPubclicNotifs.setSelected(false);

                showNotificationList(SalesNotificationList);
            }
        });
        txtFilterPubclicNotifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.setVisibility(View.VISIBLE);
                txtFilterSalesNotif.setSelected(false);
                txtFilterAll.setSelected(false);
                txtFilterPubclicNotifs.setSelected(true);

                showNotificationList(AnnouncementNotificationList);
            }
        });



        fetchNotifications( );
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFilterSalesNotif.setSelected(false);
                txtFilterAll.setSelected(true);
                txtFilterPubclicNotifs.setSelected(false);
                fetchNotifications( );
            }
        });
         searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                notificationAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notificationAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void downloadAndPlayAudio(String url, String auth, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
        SignalerNotificationApiService apiService = SignalerNotificationApiClient.getClient(auth).create(SignalerNotificationApiService.class);
        Call<ResponseBody> call = apiService.downloadAudio(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle the response
                    saveFileAndPlay(response.body(), url, position,   seekBar,   txt_current_time,   txt_total_time,imgPlay); // Pass position here
                } else {

//                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Download failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFileAndPlay(ResponseBody body, String url, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {

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
                playAudio(tempFile.getAbsolutePath(), url, position,   seekBar,   txt_current_time,   txt_total_time,imgPlay); // Pass the position to track which one is playing
            } finally {
                // Close streams
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {

//            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void playAudio(String audioFilePath, String url, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {



        // If the same audio is clicked, toggle play/pause
        if (mediaPlayer != null && currentPlayingAudioUrl != null && currentPlayingAudioUrl.equals(url)) {
            if (mediaPlayer.isPlaying()) {
                imgPlay.setImageResource(R.drawable.play);
                mediaPlayer.pause();
                return; // Exit if we paused the current audio
            } else {
                imgPlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();

                updateSeekBar(seekBar, txt_current_time); // Start updating the SeekBar and current time
                return; // Exit if we resumed the current audio
            }
        }

        // Release any previously initialized MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Initialize and play the new audio
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare(); // prepare the media player
            mediaPlayer.start(); // start playback

            // Update the UI with the total duration
            int totalDuration = mediaPlayer.getDuration();
            txt_total_time.setText(formatDuration(totalDuration));
            seekBar.setMax(totalDuration);

            currentPlayingAudioUrl = url; // Store the currently playing audio URL
            currentPlayingPosition = position; // Store the current position

            // Set the play icon to pause since the audio is now playing
            imgPlay.setImageResource(R.drawable.pause);

            // Start updating the SeekBar and current time
            updateSeekBar(seekBar, txt_current_time);

            // Set a listener to release the MediaPlayer when playback is complete
            mediaPlayer.setOnCompletionListener(mp -> {
                imgPlay.setImageResource(R.drawable.play);
                seekBar.setProgress(0);
                txt_current_time.setText("00:00");
                currentPlayingAudioUrl = null; // Reset current playing URL
            });

        } catch (IOException e) {

//            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//    private void playAudio(String audioFilePath, String url, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
//        // If the same audio is clicked, toggle play/pause
//        if (mediaPlayer != null && currentPlayingAudioUrl != null && currentPlayingAudioUrl.equals(url)) {
//            if (mediaPlayer.isPlaying()) {
//                imgPlay.setImageResource(R.drawable.play);
//                mediaPlayer.pause();
//                return; // Exit if we paused the current audio
//            } else {
//                imgPlay.setImageResource(R.drawable.pause);
//                mediaPlayer.start();
//                updateSeekBar(seekBar, txt_current_time); // Start updating the SeekBar and current time
//                return; // Exit if we resumed the current audio
//            }
//        }
//
//        // Release any previously initialized MediaPlayer
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//
//        }
//
//        // Initialize and play the new audio
//        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(audioFilePath);
//            mediaPlayer.prepare(); // prepare the media player
//            mediaPlayer.start(); // start playback
//
//            // Update the UI with the total duration
//            int totalDuration = mediaPlayer.getDuration();
//            txt_total_time.setText(formatDuration(totalDuration));
//            seekBar.setMax(totalDuration);
//
//            currentPlayingAudioUrl = url; // Store the currently playing audio URL
//            currentPlayingPosition = position; // Store the current position
//
//            // Start updating the SeekBar and current time
//            updateSeekBar(seekBar, txt_current_time);
//
//            // Set a listener to release the MediaPlayer when playback is complete
//            mediaPlayer.setOnCompletionListener(mp -> {
//                imgPlay.setImageResource(R.drawable.play);
//                seekBar.setProgress(0);
//                txt_current_time.setText("00:00");
//                currentPlayingAudioUrl = null; // Reset current playing URL
//            });
//
//        } catch (IOException e) {
//            Log.d("AudioPlayError", e.getMessage());
//            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    // Method to update the SeekBar and current time
    private void updateSeekBar(SeekBar seekBar, TextView txt_current_time) {
        if (mediaPlayer != null) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            txt_current_time.setText(formatDuration(mediaPlayer.getCurrentPosition()));

            // Update every second
            seekBar.postDelayed(() -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    updateSeekBar(seekBar, txt_current_time); // Recursively call to update
                }
            }, 1000);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Optional: Pause the audio while seeking
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Optional: Resume playback after seeking
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                }
            });
        }
    }

    // Helper method to format duration in mm:ss
    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void fetchNotifications( ) {
        prg.setVisibility(View.VISIBLE);
        notificationList.clear();
SalesNotificationList.clear();
AnnouncementNotificationList.clear();
        NotificationRestApiService apiService = NotificationRetrofitClient.getClient().create(NotificationRestApiService.class);
        Call<List<Notification>> call = apiService.getAllNotifications(Integer.parseInt(GlobalData.getStoreID()), AUTH_HEADER);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                prg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {


                    notificationList=response.body();
                    Collections.sort(notificationList, new Comparator<Notification>() {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // Adjust based on your date format

                        @Override
                        public int compare(Notification n1, Notification n2) {
                            try {
                                Date date1 = dateFormat.parse(n1.getStartDate());
                                Date date2 = dateFormat.parse(n2.getStartDate());

                                // Return negative if n1 is newer, positive if n2 is newer
                                return date2.compareTo(date1); // Reverse ordering for newest first

                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0; // If there's an error parsing, leave them in original order
                            }
                        }
                    });
CategorizeNotifications(notificationList);


                    Log.e("APIErrorsasa1", "Response Code: " + response.message());
                } else {
                    Utility.showFailureToast(NotificationListActivity.this,"مشکلی در ارتباط شما با سرور رخ داده است");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Utility.showFailureToast(NotificationListActivity.this,"مشکلی در ارتباط شما با سرور رخ داده است");
                prg.setVisibility(View.GONE);
            }
        });
    }

    private void CategorizeNotifications(List<Notification> _notificationList) {

        for (Notification filteredNotification: _notificationList
        ) {
            if(filteredNotification.getType()==2   ) {
                SalesNotificationList.add(filteredNotification);
            } else if (filteredNotification.getType()==1 || filteredNotification.getType()==3 ) {
                AnnouncementNotificationList.add(filteredNotification);
            }
        }

        showNotificationList(_notificationList);
    }

    private void showNotificationList(List < Notification>  notificationList) {

        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this));
        notificationAdapter = new NotificationAdapter(notificationList, NotificationListActivity.this, NotificationListActivity.this,NotificationListActivity.this);
        recyclerView.setAdapter(notificationAdapter);
        prg.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onAudioClick(Notification notification, String audioUrl, String authHeader, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
        downloadAndPlayAudio(audioUrl, authHeader, position,   seekBar,   txt_current_time,   txt_total_time,imgPlay);

        updateNotificationSeenStatus(notification.getId());
        setNotifPreview(notification.getId());

        notification.setSeen(true);
    }

    @Override
    public void onNotificationClick(Notification notification) {
        if (notification.getType()==2){
            updateNotificationSeenStatus(notification.getId());
            setNotifPreview(notification.getId());
            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://oportal.oshanak.com/Order/OrderList"));
            startActivity(intent2);
            notification.setSeen(true);



        }else {
            // Start the detail activity for result
            Intent intent = new Intent(this, NotificationDetailActivity.class);
            intent.putExtra("notificationID", notification.getId());
            intent.putExtra("title", notification.getTitle());
            intent.putExtra("body", notification.getBody());
            intent.putExtra("notif_date", notification.getPersianStartDate());
            intent.putExtra("imageUrl", notification.getImageUrl());
            intent.putExtra("audioUrl", notification.getVoiceUrl()); // Pass audio URL

            // Start the detail activity with a request code
            startActivityForResult(intent, 101);

            // Optionally mark the notification as seen here, if not done in the detail activity
            notification.setSeen(true);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK && data != null) {
                int notificationID = data.getIntExtra("notificationID", -1);
                boolean seen = data.getBooleanExtra("seen", false);

                if (seen) {
                    // Update your notification list to mark this notification as seen
                    updateNotificationSeenStatus(notificationID);
                }
            }
        }
    }
    private void setNotifPreview(int notificationID) {

        Date currentDate = new Date();

        // Define the date format according to the specified template
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());

        // Format the current date and time
        String formattedDate = dateFormat.format(currentDate);

        NotificationRestApiService apiService = NotificationRetrofitClient.getClient().create(NotificationRestApiService.class);
        Call<previewResponse> call = apiService.SetPreview(new NotificationPreviewRequest(notificationID, Integer.parseInt(GlobalData.getStoreID()), formattedDate), AUTH_HEADER);

        call.enqueue(new Callback<previewResponse>() {
            @Override
            public void onResponse(Call<previewResponse> call, Response<previewResponse> response) {
                Log.e("APIErrorsasa1", "Response Code: " + response.toString());

                if (response.isSuccessful() && response.body() != null) {


                } else {

                }
            }

            @Override
            public void onFailure(Call<previewResponse> call, Throwable t) {
                Log.e("APIErrorsasa1fdsf", "Response Code: " + t.getMessage());

            }
        });

    }
    private void updateNotificationSeenStatus(int notificationID) {

        for (Notification notification:notificationList
             ) {
            if ((notification.getId()==notificationID)){
                notification.setSeen(true);
            }
        }
    }
}
