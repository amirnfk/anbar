package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.PushNotification.Notification;
import com.oshanak.mobilemarket.Activity.PushNotification.PicassoClient;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private OnNotificationClickListener listener;
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }
    private List<Notification> notificationList;
    private Context context;
    private OnAudioClickListener audioClickListener; // Declare the interface
    private List<Notification> notificationListFull; // List to hold the original data

    // Constructor and other methods...


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Notification> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(notificationListFull); // No filter applied
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Notification notification : notificationListFull) {
                        if (notification.getTitle().toLowerCase().contains(filterPattern) ||
                                notification.getBody().toLowerCase().contains(filterPattern)) {
                            filteredList.add(notification);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notificationList.clear();
                notificationList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
    public NotificationAdapter(List<Notification> notificationList, Context context, OnAudioClickListener audioClickListener, OnNotificationClickListener listener) {
        this.notificationListFull = new ArrayList<>(notificationList);
        this.notificationList = notificationList;
        this.context = context;
        this.listener=listener;
        this.audioClickListener = audioClickListener; // Initialize the listener
    }
    public interface OnAudioClickListener {
        void onAudioClick(Notification notificationId, String audioUrl, String authHeader, int position, SeekBar seekBar, TextView txt_current_time, TextView txt_total_time, ImageView imgPlay);
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {


        Notification notification = notificationList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.bodyTextView.setText(notification.getBody());
        holder.txt_notif_date.setText((notification.getPersianStartDate() .substring(0,16)).replace(" ","\n"));


        if (notification.getType()==2 ){
holder.txt_orderIdtype.setVisibility(View.VISIBLE);
            switch (notification.getOrderTypeId()){
                case 0:
                    break;
                case 1:
                    holder.txt_orderIdtype.setText("تلفنی");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.blue_300));
                    break;
                case 2:
                    holder.txt_orderIdtype.setText("مدیسه");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.modise));
                    break;
                case 3:
                    holder.txt_orderIdtype.setText("اسنپ");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.dribble_green));
                    break;
                case 4:
                    holder.txt_orderIdtype.setText("اوانو");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.green_900));
                    break;
                case 5:
                    holder.txt_orderIdtype.setText("هفت کالا");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.haft_blue));
                    break;
                case 6:
                    holder.txt_orderIdtype.setText("دیجی جت");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.orange_A700));
                    break;
                case 7:
                    holder.txt_orderIdtype.setText("باتیلالند");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.figmagreen));
                    break;
                case 8:
                    holder.txt_orderIdtype.setText("بازرگام");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.orange_500));
                    break;
                case 9:
                    holder.txt_orderIdtype.setText("کیپا");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.haft_blue));
                    break;
                default:
                    holder.txt_orderIdtype.setText("هفت کالا");
                    holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.haft_blue));
            }
        }else if (notification.getType()==1 || notification.getType()==3){
            holder.txt_orderIdtype.setText("اطلاعیه");
            holder.txt_orderIdtype.setBackgroundColor(context.getResources().getColor(R.color.HaftOrange));
        }


        if (notification.isSeen()){
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.grey_20));
            holder.bodyTextView.setTextColor(context.getResources().getColor(R.color.grey_20));
            holder.txt_total_time.setTextColor(context.getResources().getColor(R.color.grey_20));
            holder.txt_current_time.setTextColor(context.getResources().getColor(R.color.grey_20));
            holder.imageView.setImageAlpha(100);
            holder.imgPlayAudio.setImageAlpha(100);
            holder.seekBar.setAlpha(0.5F);
        }else{
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.Black));
            holder.bodyTextView.setTextColor(context.getResources().getColor(R.color.Black));
            holder.txt_total_time.setTextColor(context.getResources().getColor(R.color.Black));
            holder.txt_current_time.setTextColor(context.getResources().getColor(R.color.Black));
            holder.imageView.setImageAlpha(255);
            holder.imgPlayAudio.setImageAlpha(255);
            holder.seekBar.setAlpha(1F);
        }
        holder.titleTextView.setOnClickListener(v -> openDetailActivity(holder,notification));

        // Set click listener for bodyTextView
        holder.bodyTextView.setOnClickListener(v -> openDetailActivity(holder,notification));

        // Set click listener for imageView
        holder.imageView.setOnClickListener(v -> openDetailActivity(holder,notification));


        if (notification.getVoiceUrl() != null && !notification.getVoiceUrl().isEmpty()) {
            holder.lytVoice.setVisibility(View.VISIBLE);
            holder.imgPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (audioClickListener != null) {
                        String audioUrl ="https://onotify.oshanak.com:8443/Home/Api/Notify/Voice?fileName="+ notification.getVoiceUrl(); // Replace with your audio URL

                        String authHeader = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM="; // If the header differs for each notification
                        audioClickListener.onAudioClick( notification,audioUrl, authHeader,position,holder.seekBar,holder.txt_current_time,holder.txt_total_time,holder.imgPlayAudio); // Notify the click
                    }
                }
            });
        } else {
            holder.lytVoice.setVisibility(View.GONE);
        }


        if (notification.getImageUrl() != null && !notification.getImageUrl().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            String authToken = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";

            Picasso picasso = PicassoClient.getPicassoInstance(context, authToken);
            picasso.load("https://onotify.oshanak.com:8443/Home/Api/Notify/Image?fileName=" + notification.getImageUrl())
                    .resize(200, 200) // Resize to a smaller size
                    .centerCrop() // Crop to fit the dimensions
                    .error(R.drawable.haft_icon) // Error placeholder
                    .placeholder(R.drawable.progress_animation) // Loading placeholder
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        // Load the image if it exists
//        if (notification.getImageUrl() != null && !notification.getImageUrl().isEmpty()) {
//            holder.imageView.setVisibility(View.VISIBLE);
//            String authToken = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";
//
//            Picasso picasso = PicassoClient.getPicassoInstance(context, authToken);
//
//                picasso.load("https://onotify.oshanak.com:8443/Home/Api/Notify/Image?fileName="+notification.getImageUrl()).error(R.drawable.haft_icon).placeholder(R.drawable.progress_animation).into(holder.imageView);
//        } else {
//            holder.imageView.setVisibility(View.GONE);
//        }
    }

    private void openDetailActivity(NotificationViewHolder holder, Notification notification) {
        if (listener != null) {
            listener.onNotificationClick(notification);
        }
        holder.titleTextView.setTextColor(context.getResources().getColor(R.color.grey_20));
        holder.bodyTextView.setTextColor(context.getResources().getColor(R.color.grey_20));
        holder.txt_total_time.setTextColor(context.getResources().getColor(R.color.grey_20));
        holder.txt_current_time.setTextColor(context.getResources().getColor(R.color.grey_20));
        holder.imageView.setImageAlpha(100);
        holder.imgPlayAudio.setImageAlpha(100);
        holder.seekBar.setAlpha(0.5F);


    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,txt_orderIdtype;
        TextView bodyTextView,txt_current_time,txt_total_time,txt_notif_date;
        ImageView imageView;
        ImageView imgPlayAudio;
        SeekBar seekBar;
LinearLayout lytVoice;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            txt_orderIdtype = itemView.findViewById(R.id.txt_orderIdtype);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            txt_notif_date = itemView.findViewById(R.id.txt_notif_date);
            txt_total_time = itemView.findViewById(R.id.txt_total_time);
            txt_current_time = itemView.findViewById(R.id.txt_current_time);
            imageView = itemView.findViewById(R.id.imageView);
            imgPlayAudio = itemView.findViewById(R.id.img_play);
            lytVoice=itemView.findViewById(R.id.lyt_voice);
            seekBar=itemView.findViewById(R.id.notif_seekbar);
        }
    }


}