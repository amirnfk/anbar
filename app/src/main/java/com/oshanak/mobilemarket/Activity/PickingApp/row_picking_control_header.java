
package com.oshanak.mobilemarket.Activity.PickingApp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class row_picking_control_header  extends RecyclerView.Adapter<row_picking_control_header.ViewHolder> {
    private ArrayList<PickingDeliverHeaderData> dataList;
    private OnSendToSapClickListener sendToSapClickListener;
    private OnPickingControlCommandListener pickingControlCommandListener;
Context context;

    public void updateList(List<PickingDeliverHeaderData> newList) {
        this.dataList = (ArrayList<PickingDeliverHeaderData>) newList;
        notifyDataSetChanged();
    }

    public interface OnSendToSapClickListener {
        void onSendToSapClick(PickingDeliverHeaderData data,ProgressBar prg);
    }

    public interface OnPickingControlCommandListener {
        void OnPickingControlCommand(PickingDeliverHeaderData pickingControlHeaderData, int position, PickingControlCommandType commandType);
    }

    public enum PickingControlCommandType {
        Unknown, Items
    }

    public row_picking_control_header(Context context, OnPickingControlCommandListener onPickingControlCommandListener , OnSendToSapClickListener listener, ArrayList<PickingDeliverHeaderData> list) {
        this.dataList = list;
        this.sendToSapClickListener = listener;
        this.pickingControlCommandListener = onPickingControlCommandListener;
this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_picking_control_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickingDeliverHeaderData data = dataList.get(position);
        holder.tvDocNo.setText(position + 1 + ". " + "شماره: " + data.getVBELN());
        holder.tvDate.setText(data.getBLDAT());
        holder.tvItemCount.setText("لاین: " + data.getLine());
        holder.tvPalletCount.setText("وضعیت: " + data.getStatusName());


        switch (data.getEKGRP()){
            case 101:
                holder.tvItemType.setText("خوراکی");
                holder.tvItemType.setBackgroundColor( context.getResources().getColor(R.color.orange_200));
                break;
            case 103:
                holder.tvItemType.setText("یخچالی");
                holder.tvItemType.setBackgroundColor( context.getResources().getColor(R.color. blue_200));

                break;
            case 104:
                holder.tvItemType.setText("شوینده");
                holder.tvItemType.setBackgroundColor( context.getResources().getColor(R.color.green_200));

                break;
            default:
                holder.tvItemType.setText("سایر");
                holder.tvItemType.setBackgroundColor( context.getResources().getColor(R.color.red_200));

                break;
        }


        if (data.getStatusName().equals("ارسال شده به سپ")){
            holder.tvPalletCount.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvPalletCount.setBackgroundColor(context.getResources().getColor(R.color.dribble_green));
            holder.Bedit.setImageResource(R.drawable.checkmark);
        }else if( data.getStatusName().equals("اتمام کنترل")){
            holder.tvPalletCount.setTextColor(context.getResources().getColor(R.color.dribble_green));
            holder.Bedit.setImageResource(R.drawable.list2);
        }else{
            holder.tvPalletCount.setTextColor(context.getResources().getColor(R.color.dribble_orange));
            holder.Bedit.setImageResource(R.drawable.list2);
        }
        holder.tvStoreId.setText("فروشگاه: " + " (" + data.getStoreID() + ") " + data.getPLANT_NAME());

        // Set up item click listeners
        holder.Bedit.setOnClickListener(v -> {

            if (pickingControlCommandListener != null) {
                pickingControlCommandListener.OnPickingControlCommand(data, position, PickingControlCommandType.Items);
            }
        });
        holder.img_info_control_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(data.getInsertDate(),
                        data.getInsertUser(),
                        data.getUpdateDate(),
                        data.getUpdateUser());

            }
        });

        if (data.getStatusID() == 1) {
            holder.bSendToSap.setVisibility(View.VISIBLE);
            holder.bSendToSap.setOnClickListener(v -> {
                if (sendToSapClickListener != null) {
                    sendToSapClickListener.onSendToSapClick(data,holder.prg_control_header);
                }
            });
        } else {
            holder.bSendToSap.setVisibility(View.GONE);
        }
    }

    private void showInfoDialog(String info1, String info2, String info3, String info4) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_item_info, null);

        TextView tvInfo1 = dialogView.findViewById(R.id.tvInfo1);
        TextView tvInfo2 = dialogView.findViewById(R.id.tvInfo2);
        TextView tvInfo3 = dialogView.findViewById(R.id.tvInfo3);
        TextView tvInfo4 = dialogView.findViewById(R.id.tvInfo4);

        tvInfo1.setText(info1);
        tvInfo2.setText(info2);
        tvInfo3.setText(info3);
        tvInfo4.setText(info4);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        // پس‌زمینه دیالوگ رو شفاف کن
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // دکمه بستن
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocNo;
        TextView tvDate;
        TextView tvItemCount;
        TextView tvItemType;
        TextView tvPalletCount;
        TextView tvStoreId;
        CardView bItems;
        ImageButton bSendToSap;
        ImageView Bedit;
        ImageButton img_info_control_header;
ProgressBar prg_control_header;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDocNo = itemView.findViewById(R.id.tvDocNo);
            tvItemType = itemView.findViewById(R.id.txt_Item_type);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            tvPalletCount = itemView.findViewById(R.id.tvStatus);
            tvStoreId = itemView.findViewById(R.id.tvStoreId);
            bItems = itemView.findViewById(R.id.bItems);
            bSendToSap = itemView.findViewById(R.id.bSendToSAP);
            prg_control_header=itemView.findViewById(R.id.prg_control_header);
            Bedit=itemView.findViewById(R.id.bEdit);
            img_info_control_header=itemView.findViewById(R.id.img_info_control_header);
        }
    }
}

//package com.oshanak.mobilemarket.Activity.RowAdapter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
//import com.oshanak.mobilemarket.R;
//
//import java.util.ArrayList;
//
//
//public class row_picking_control_header extends ArrayAdapter<PickingDeliverHeaderData>
//{
//    private OnSendToSapClickListener listener;
//
//    Context context;
//    private static final int NOT_SELECTED = -1;
//    private int selectedPos = NOT_SELECTED;
//    private OnPickingControlCommandListener onPickingControlCommandListener;
//    public interface OnSendToSapClickListener {
//        void onSendToSapClick(PickingDeliverHeaderData data);
//    }
//
//    public void setSelection(int position)
//    {
//        if (selectedPos == position)
//        {
//         } else
//        {
//            selectedPos = position;
//        }
//        notifyDataSetChanged();
//    }
//    public row_picking_control_header(Context context, ArrayList<PickingDeliverHeaderData> list, OnSendToSapClickListener listener)
//    {
//        super(context, 0, list);
//        this.context = context;
//        this.listener = listener;
//
//
//        try
//        {
//            onPickingControlCommandListener = (OnPickingControlCommandListener) context;
//        }
//        catch (ClassCastException e)
//        {
//            Log.d("dfsasasada",e.getMessage().toString());
////            throw new ClassCastException(context.toString() + " must implement OnPickingControlCommandListener");
//        }
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent)
//    {
//
//        final PickingDeliverHeaderData data = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_picking_control_header, parent, false);
//        }
//
//        TextView tvDocNo = convertView.findViewById(R.id.tvDocNo);
//
//        tvDocNo.setText(position + 1 + ". " + "شماره: " + data.getVBELN());
//
//        TextView tvDate = convertView.findViewById(R.id.tvDate);
//
//        tvDate.setText(data.getBLDAT());
//
//        TextView tvItemCount = convertView.findViewById(R.id.tvItemCount);
//         tvItemCount.setText("لاین: : " + data.getLine());
//
//        TextView tvPalletCount = convertView.findViewById(R.id.tvStatus);
//         tvPalletCount.setText("وضعیت : " + data.getStatusName());
//        TextView tvStoreiD = convertView.findViewById(R.id.tvStoreId);
//         tvStoreiD.setText("فروشگاه : " + " ("+data.getStoreID()+") "+data.getPLANT_NAME());
//
//
//        ImageButton bItems = convertView.findViewById(R.id.bItems);
//        ImageButton bSendToSap = convertView.findViewById(R.id.bSendToSAP);
//
//        bItems.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("chasdasda11",data.toString()+"1..."+position+"2..."+ row_picking_control_Line.PickingControlCommandType.Items.toString()+"3...");
//                onPickingControlCommandListener.OnPickingControlCommand(data, position, PickingControlCommandType.Items);
//            }
//        });
//        if(data.getStatusID()==1){
//            bSendToSap.setVisibility(View.VISIBLE);
//            bSendToSap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onSendToSapClick(data); // Trigger the interface method
//                    }
//
//                }
//            });
//        }else{
//            bSendToSap.setVisibility(View.GONE);
//        }
//
//
//        return convertView;
//    }
//
//
//    public interface OnPickingControlCommandListener
//    {
//        void OnPickingControlCommand(PickingDeliverHeaderData pickingControlHeaderData, int position, PickingControlCommandType commandType);
//    }
//    public enum PickingControlCommandType
//    {
//        Unknown
//        ,Items
//    }
//}
