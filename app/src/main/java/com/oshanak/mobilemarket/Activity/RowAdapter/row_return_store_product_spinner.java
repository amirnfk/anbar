package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.Enum.StoreReturnItemReason;
import com.oshanak.mobilemarket.R;

import java.util.List;

public class row_return_store_product_spinner extends ArrayAdapter<StoreReturnItemReason>
{
    Context context;

    public row_return_store_product_spinner(Context context, List<StoreReturnItemReason> list)
    {
        super(context, 0, list);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        StoreReturnItemReason item = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_general_spinner, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        Utility.setFont(context, tvTitle);

        tvTitle.setText( item.getName());

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView( position, convertView, parent);
    }
}
