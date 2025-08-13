package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingItemUMdata;
import com.oshanak.mobilemarket.R;

import java.util.List;

public class row_unit_warehousing extends ArrayAdapter<WarehousingItemUMdata>
{
    Context context;

    public row_unit_warehousing(Context context, List<WarehousingItemUMdata> list)
    {
        super(context, 0, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        WarehousingItemUMdata item = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_unit, parent, false);
        }

        TextView tvUnitName = convertView.findViewById(R.id.tvUnitName);
        Utility.setFont(context, tvUnitName);
        tvUnitName.setText( item.getUMID());

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        Utility.setFont(context, tvAmount);
        tvAmount.setText(ThousandSeparatorWatcher.addSeparator( item.getMultipleConvert()));

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView( position, convertView, parent);
    }
}
