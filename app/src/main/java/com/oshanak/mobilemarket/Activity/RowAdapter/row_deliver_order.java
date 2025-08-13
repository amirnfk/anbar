package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.Enum.DeliverOrderStatus;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_deliver_order extends ArrayAdapter<DeliverOrderData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnDeliverOrderListCommandListener onDeliverOrderListCommandListener;

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
    public row_deliver_order(Context context, ArrayList<DeliverOrderData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onDeliverOrderListCommandListener = (OnDeliverOrderListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onDeliverOrderListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final DeliverOrderData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_deliver_order, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
        Utility.setFont(context, tvCustomerName);
        Utility.increaseTextSize(tvCustomerName, 10);
        Utility.setFontBold(tvCustomerName);
        tvCustomerName.setText(position + 1 + ". " + data.getCustomerName());

        TextView tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
        Utility.setFont(context, tvOrderDate);
        tvOrderDate.setText(data.getOrderDate().substring(0,16));

        TextView tvStore = convertView.findViewById(R.id.tvStore);
        Utility.setFont(context, tvStore);
        tvStore.setText("فروشگاه: " + data.getRetailStoreId());

        TextView tvOrderStatus = convertView.findViewById(R.id.tvOrderStatus);
        Utility.setFont(context, tvOrderStatus);
        tvOrderStatus.setText(data.getOrderStatusName());
        if(data.getOrderStatusId() == DeliverOrderStatus.ReadyToSend.getCode())
        {
            tvOrderStatus.setTextColor(Color.BLACK);
        }
        else if(data.getOrderStatusId() == DeliverOrderStatus.Delivered.getCode())
        {
            tvOrderStatus.setTextColor(Color.BLUE);
        }
        else if(data.getOrderStatusId() == DeliverOrderStatus.Return.getCode())
        {
            tvOrderStatus.setTextColor(Color.RED);
        }
        else
        {
            tvOrderStatus.setTextColor(Color.GRAY);
        }

        int buttonDecTextSize = -20;

        Button bDeliver = convertView.findViewById(R.id.bDeliver);
        Utility.setFont(context, bDeliver);
        Utility.increaseTextSize(bDeliver, buttonDecTextSize);

        Button bReturn = convertView.findViewById(R.id.bReturn);
        Utility.setFont(context, bReturn);
        Utility.increaseTextSize(bReturn, buttonDecTextSize);

        Button bItems = convertView.findViewById(R.id.bItems);
        Utility.setFont(context, bItems);
        Utility.increaseTextSize(bItems, buttonDecTextSize);

        bDeliver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeliverOrderListCommandListener.OnDeliverOrderListCommand(data, position, DeliverOrderListCommandType.Deliver);
            }
        });
        bReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeliverOrderListCommandListener.OnDeliverOrderListCommand(data, position, DeliverOrderListCommandType.Return);
            }
        });
        bItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeliverOrderListCommandListener.OnDeliverOrderListCommand(data, position, DeliverOrderListCommandType.Items);
            }
        });

        return convertView;
    }
    public interface OnDeliverOrderListCommandListener
    {
        void OnDeliverOrderListCommand(DeliverOrderData deliverOrderData, int position, DeliverOrderListCommandType commandType);
    }
    public enum DeliverOrderListCommandType
    {
        Unknown
        ,Deliver
        ,Return
        ,Items
    }
}
