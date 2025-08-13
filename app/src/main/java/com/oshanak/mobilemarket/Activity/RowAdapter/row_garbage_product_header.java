package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductHeaderData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_garbage_product_header extends ArrayAdapter<GarbageProductHeaderData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnGarbageProductHeaderListCommandListener onGarbageProductHeaderListCommandListener;

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
    public row_garbage_product_header(Context context, ArrayList<GarbageProductHeaderData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onGarbageProductHeaderListCommandListener = (OnGarbageProductHeaderListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnGarbageProductHeaderListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final GarbageProductHeaderData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_garbage_product_header, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvHeaderId = convertView.findViewById(R.id.tvHeaderId);
        Utility.setFont(context, tvHeaderId);
        tvHeaderId.setText("شماره سند: " + data.getID());

        TextView tvInsertDate = convertView.findViewById(R.id.tvInsertDate);
        Utility.setFont(context, tvInsertDate);
        Utility.increaseTextSize(tvInsertDate, -10);
        tvInsertDate.setText(  data.getInsertDate().substring(0, 16));

        int buttonDecTextSize = -20;

        ImageButton bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onGarbageProductHeaderListCommandListener.OnInboundHeaderListCommand(data, position, GarbageProductHeaderCommandType.Edit);
            }
        });

        ImageButton bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context, bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onGarbageProductHeaderListCommandListener.OnInboundHeaderListCommand(data, position, GarbageProductHeaderCommandType.Delete);
            }
        });
        return convertView;
    }
    public interface OnGarbageProductHeaderListCommandListener
    {
        void OnInboundHeaderListCommand(GarbageProductHeaderData garbageProductHeaderData
                , int position, GarbageProductHeaderCommandType commandType);
    }
    public enum GarbageProductHeaderCommandType
    {
        Unknown
        ,Edit
        ,Delete
    }
}
