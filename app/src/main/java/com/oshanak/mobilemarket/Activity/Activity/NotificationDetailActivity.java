package com.oshanak.mobilemarket.Activity.Activity;


import static com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity.EXTRA_IMAGE_URL;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Models.NotificationPreviewRequest;
import com.oshanak.mobilemarket.Activity.PushNotification.Notification;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRestApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.NotificationRetrofitClient;
import com.oshanak.mobilemarket.Activity.PushNotification.PicassoClient;
import com.oshanak.mobilemarket.Activity.PushNotification.SignalerNotificationApiClient;
import com.oshanak.mobilemarket.Activity.PushNotification.SignalerNotificationApiService;
import com.oshanak.mobilemarket.Activity.PushNotification.previewResponse;
import com.oshanak.mobilemarket.Activity.RowAdapter.NotificationAdapter;
import com.oshanak.mobilemarket.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailActivity extends AppCompatActivity {

    private TextView titleTextView, bodyTextView, currentTimeTextView, totalTimeTextView, txt_create_date;
    private ImageView imageView;
    private ImageView playButton;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private final String AUTH_HEADER = "Basic bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";
   public String imageUrl="";
    public String audioUrl="";
    private String auth; // Assuming you have an auth token
    private boolean isPlaying = false;
    private int lastPlaybackPosition = 0; // To remember the position

LinearLayout lyt_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color
         imageUrl="";
        audioUrl="";
        titleTextView = findViewById(R.id.detail_title);
        txt_create_date = findViewById(R.id.txt_create_date);
        bodyTextView = findViewById(R.id.detail_body);
        imageView = findViewById(R.id.detail_image);
        playButton = findViewById(R.id.play_button);
        seekBar = findViewById(R.id.seek_bar);
        currentTimeTextView = findViewById(R.id.current_time);
        totalTimeTextView = findViewById(R.id.total_time);
lyt_voice=findViewById(R.id.lyt_voice);

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int notificationID = intent.getIntExtra("notificationID", 0);
        String body = intent.getStringExtra("body");
        String notif_date = intent.getStringExtra("notif_date");
         imageUrl = intent.getStringExtra("imageUrl" );
        audioUrl = intent.getStringExtra("audioUrl");
        auth = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";

        Log.e("APIErrorsasa1", "axsdfasasdasdasd " + imageUrl+"............"+audioUrl);
        // Set the data to views
        titleTextView.setText(title);
        txt_create_date.setText(notif_date.replace("T", " ").substring(0, 19));
        bodyTextView.setText(body);
        if (imageUrl!=null && !imageUrl.equals("")) {
            imageView.setVisibility(View.VISIBLE);
            String authToken = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";

            Picasso picasso = PicassoClient.getPicassoInstance(NotificationDetailActivity.this, authToken);
            picasso.load("https://onotify.oshanak.com:8443/Home/Api/Notify/Image?fileName=" + imageUrl).into(imageView);

        }else {
            imageView.setVisibility(View.GONE);
        }

        if (audioUrl!=null &&!audioUrl .equals("")) {
            lyt_voice.setVisibility(View.VISIBLE);
            audioUrl="https://onotify.oshanak.com:8443/Home/Api/Notify/Voice?fileName=" +audioUrl;
        }else {
            lyt_voice.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDetailActivity.this, ImageEnlargedActivity.class);
                intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
                intent.putExtra("from", "notifications");
                startActivity(intent);
            }
        });
        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                downloadAndPlayAudio(audioUrl, auth, 0, seekBar, currentTimeTextView, totalTimeTextView, playButton);
            }
        });

        setNotifPreview(notificationID);
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

    private void downloadAndPlayAudio(String url, String auth, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
        SignalerNotificationApiService apiService = SignalerNotificationApiClient.getClient(auth).create(SignalerNotificationApiService.class);
        Call<ResponseBody> call = apiService.downloadAudio(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle the response
                    saveFileAndPlay(response.body(), url, position, seekBar, txt_current_time, txt_total_time, imgPlay); // Pass position here
                } else {

                    Toast.makeText(getApplicationContext(), "مشکلی در ذخیره ی فایل به وجود آمد", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "مشکلی در ذخیره ی فایل به وجود آمد", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFileAndPlay(ResponseBody body, String url, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
        try {
            // Create a file to save the audio
            File audioFile = new File(getExternalFilesDir(null), "downloaded_audio.mp3");
            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(audioFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            // Play the audio
            playAudio(audioFile.getAbsolutePath(), seekBar, txt_current_time, txt_total_time, imgPlay);
        } catch (IOException e) {
            Log.e("FileError", "Error saving audio file: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error saving audio file", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio(String filePath, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                totalTimeTextView.setText(formatDuration(mediaPlayer.getDuration()));
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.seekTo(lastPlaybackPosition); // Seek to last position
                mediaPlayer.start();
                isPlaying = true;
                playButton.setImageResource(R.drawable.pause);
                updateSeekBar(seekBar, txt_current_time);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                lastPlaybackPosition = 0; // Reset position on completion
                playButton.setImageResource(R.drawable.play);
                seekBar.setProgress(0);
                currentTimeTextView.setText("0:00");
            });

            // Updated SeekBar listener
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress); // Change the playback position
                        currentTimeTextView.setText(formatDuration(progress)); // Update current time display
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Optional: Pause audio when the user starts dragging the SeekBar
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Resume playback after the user stops dragging the SeekBar
                    if (mediaPlayer != null && isPlaying) {
                        mediaPlayer.start();

                    }
                }
            });
        } catch (IOException e) {
            Log.e("PlaybackError", "Error playing audio: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBar(SeekBar seekBar, TextView txt_current_time) {
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                runOnUiThread(() -> {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    currentTimeTextView.setText(formatDuration(currentPosition));
                });
                try {
                    Thread.sleep(1000); // Update seekbar every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    private String formatDuration(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            lastPlaybackPosition = mediaPlayer.getCurrentPosition(); // Save current position
            mediaPlayer.pause();
            isPlaying = false;
            playButton.setImageResource(R.drawable.play);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            lastPlaybackPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}


