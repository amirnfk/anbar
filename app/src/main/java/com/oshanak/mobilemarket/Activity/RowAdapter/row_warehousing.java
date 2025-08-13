package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingDetailData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_warehousing extends ArrayAdapter<WarehousingDetailData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnWarehousingCommandListener onWarehousingCommandListener;

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
    public row_warehousing(Context context, ArrayList<WarehousingDetailData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onWarehousingCommandListener = (OnWarehousingCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context + " must implement OnWarehousingCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final WarehousingDetailData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_warehousing, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvName = convertView.findViewById(R.id.tvName);
        Utility.setFont(context, tvName);
        tvName.setText(data.getItemID() + " - " + data.getItemName());

        TextView tvLocation = convertView.findViewById(R.id.tvLocation);
        Utility.setFont(context, tvLocation);
        tvLocation.setText("موقعيت: " + ThousandSeparatorWatcher.addSeparator( data.getLocation()));

        TextView tvCountDone = convertView.findViewById(R.id.tvCountDone);
        Utility.setFont(context, tvCountDone);
        Utility.increaseTextSize(tvCountDone, -15);
        if(data.isCountingDone())
        {
            tvCountDone.setText("شمارش شد");
            tvCountDone.setTextColor(Color.BLACK);
        }
        else
        {
            tvCountDone.setText("شمارش نشده");
            tvCountDone.setTextColor(Color.RED);
        }

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        Utility.setFont(context, tvAmount);
        Utility.increaseTextSize(tvAmount, 20);
        Utility.setFontBold(tvAmount);
        tvAmount.setText(ThousandSeparatorWatcher.addSeparator( data.getCountValue()));

        int buttonDecTextSize = -20;

        ImageButton bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onWarehousingCommandListener.OnWarehousingCommand(data, position, WarehousingCommandType.Edit);
            }
        });

        ImageButton bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context, bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onWarehousingCommandListener.OnWarehousingCommand(data, position, WarehousingCommandType.Delete);
            }
        });
        return convertView;
    }
    public interface OnWarehousingCommandListener
    {
        void OnWarehousingCommand(WarehousingDetailData warehousingDetailData
                , int position, WarehousingCommandType commandType);
    }
    public enum WarehousingCommandType
    {
        Unknown
        ,Edit
        ,Delete
    }
}
