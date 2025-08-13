package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.R;

import java.util.List;

public class row_unit extends ArrayAdapter<ProductUnitData>
{
    Context context;

    public row_unit(Context context, List<ProductUnitData> list)
    {
        super(context, 0, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProductUnitData item = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_unit, parent, false);
        }

        TextView tvUnitName = convertView.findViewById(R.id.tvUnitName);
        Utility.setFont(context, tvUnitName);
        tvUnitName.setText( item.getUnitName());

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        Utility.setFont(context, tvAmount);
        tvAmount.setText( item.getAmount());

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView( position, convertView, parent);
    }
}
