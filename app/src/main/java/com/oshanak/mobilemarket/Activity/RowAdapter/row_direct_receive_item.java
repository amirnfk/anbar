package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveDetailData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_direct_receive_item extends ArrayAdapter<DirectReceiveDetailData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnDirectReceiveDetailListCommandListener onDirectReceiveDetailListCommandListener;

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
    public row_direct_receive_item(Context context, ArrayList<DirectReceiveDetailData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onDirectReceiveDetailListCommandListener = (OnDirectReceiveDetailListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnDirectReceiveDetailListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final DirectReceiveDetailData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_direct_receive_item, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvName = convertView.findViewById(R.id.tvName);
        Utility.setFont(context, tvName);
        tvName.setText(data.getItemID() + " - " + data.getItemName());

        TextView tvAmount1 = convertView.findViewById(R.id.tvAmount1);
        Utility.setFont(context, tvAmount1);

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        Utility.setFont(context, tvAmount);
        Utility.increaseTextSize(tvAmount, 20);
        Utility.setFontBold(tvAmount);
        tvAmount.setText(ThousandSeparatorWatcher.addSeparator( data.getAmount()));

        int buttonDecTextSize = -20;

        ImageButton bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDirectReceiveDetailListCommandListener.OnDirectReceiveListCommand(data, position, DirectReceiveDetailCommandType.Edit);
            }
        });

        ImageButton bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context, bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDirectReceiveDetailListCommandListener.OnDirectReceiveListCommand(data, position, DirectReceiveDetailCommandType.Delete);
            }
        });
        return convertView;
    }
    public interface OnDirectReceiveDetailListCommandListener
    {
        void OnDirectReceiveListCommand(DirectReceiveDetailData directReceiveDetailData
                , int position, DirectReceiveDetailCommandType commandType);
    }
    public enum DirectReceiveDetailCommandType
    {
        Unknown
        ,Edit
        ,Delete
    }
}
