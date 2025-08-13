package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.VendorData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_vendor extends ArrayAdapter<VendorData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnVendorListCommandListener onVendorListCommandListener;

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
    public row_vendor(Context context, ArrayList<VendorData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onVendorListCommandListener = (OnVendorListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnVendorListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final VendorData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_vendor, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvName = convertView.findViewById(R.id.tvName);
        Utility.setFont(context, tvName);
        tvName.setText(data.getID() + " - " + data.getName());


        int buttonDecTextSize = -20;

        ImageButton bSelect = convertView.findViewById(R.id.bSelect);
        Utility.setFont(context, bSelect);
        Utility.increaseTextSize(bSelect, buttonDecTextSize);

        bSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onVendorListCommandListener.OnVendorListCommand(data, position, VendorListCommandType.Select);
            }
        });
        return convertView;
    }
    public interface OnVendorListCommandListener
    {
        void OnVendorListCommand(VendorData vendorData, int position, VendorListCommandType commandType);
    }
    public enum VendorListCommandType
    {
        Unknown
        ,Select
    }
}
