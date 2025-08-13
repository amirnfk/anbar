package com.oshanak.mobilemarket.Activity.PickingApp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_picking_control_Line extends ArrayAdapter<PickingDeliverHeaderData>
{
    Context context;
    private OnItemClickListener listener;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnPickingControlCommandListener onPickingControlCommandListener;

    public void setSelection(int position)
    {
        if (selectedPos == position)
        {
            //selectedPos = NOT_SELECTED;
        } else
        {
            selectedPos = position;
        }
        notifyDataSetChanged();
    }
    public row_picking_control_Line(Context context, ArrayList<PickingDeliverHeaderData> list, OnItemClickListener listener)
    {
        super(context, 0, list);
        this.context = context;
        this.listener = listener;

        try
        {
            onPickingControlCommandListener = (OnPickingControlCommandListener) context;
        }
        catch (ClassCastException e)
        {

//            throw new ClassCastException(context.toString() + " must implement OnPickingControlCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final PickingDeliverHeaderData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_picking_control_header_level2, parent, false);
        }
//        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvDocNo = convertView.findViewById(R.id.tvDocNo);
//        Utility.setFont(context, tvDocNo);
//        Utility.increaseTextSize(tvDocNo, 10);
//        Utility.setFontBold(tvDocNo);
        tvDocNo.setText(position + 1 + ". " + "شماره: " + data.getVBELN());
        TextView tvCollector = convertView.findViewById(R.id.tvCollector);
//        Utility.setFont(context, tvCollector);
//        Utility.increaseTextSize(tvCollector, 10);
//        Utility.setFontBold(tvCollector);
        tvCollector.setText("کالکتور: " + data.getCollectorName());

        TextView tvDate = convertView.findViewById(R.id.tvDate);
//        Utility.setFont(context, tvDate);
//        tvDate.setText(data.getDate().substring(0,16));
        tvDate.setText(data.getBLDAT());

        TextView tvItemCount = convertView.findViewById(R.id.tvItemCount);
//        Utility.setFont(context, tvItemCount);
        tvItemCount.setText("لاین: : " + data.getLine());

        TextView tvPalletCount = convertView.findViewById(R.id.tvStatus);
//        Utility.setFont(context, tvPalletCount);
        tvPalletCount.setText("وضعیت : " + data.getStatusName());
        TextView tvStoreiD = convertView.findViewById(R.id.tvStoreId);
//        Utility.setFont(context, tvStoreiD);
        tvStoreiD.setText("فروشگاه : " + data.getStoreID());


        int buttonDecTextSize = -20;


//        Button bItems = convertView.findViewById(R.id.bItems);
//        Utility.setFont(context, bItems);
//        Utility.increaseTextSize(bItems, buttonDecTextSize);
        CardView bItems = convertView.findViewById(R.id.bItems);
ImageButton img_info_control_line=convertView.findViewById(R.id.img_info_control_line);


img_info_control_line.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showInfoDialog(data.getInsertDate(),
                data.getInsertUser(),
                data.getUpdateDate(),
                data.getUpdateUser());
    }
});

        bItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(data); // Trigger the interface method
                }
//                Intent intent = new Intent(context, PickingItemListActivity.class);
//                intent.putExtra("pickingDeliverHeaderData",data);
//
//                context.startActivity(intent);

             }
        });

        return convertView;
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


    public interface OnPickingControlCommandListener
    {
        void OnPickingControlCommand(PickingDeliverHeaderData pickingControlHeaderData, int position, PickingControlCommandType commandType);
    }
    public interface OnItemClickListener {
        void onItemClick(PickingDeliverHeaderData data);
    }
    public enum PickingControlCommandType
    {
        Unknown
        ,Items
    }
}
