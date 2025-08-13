package com.oshanak.mobilemarket.Activity.RowAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverItemData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_deliver_item extends ArrayAdapter<DeliverItemData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnDeliverItemListCommandListener onDeliverItemListCommandListener;

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
    public row_deliver_item(Context context, ArrayList<DeliverItemData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onDeliverItemListCommandListener = (OnDeliverItemListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onDeliverItemListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final DeliverItemData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_deliver_item, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        Utility.setFont(context, tvItemName);
//        Utility.increaseTextSize(tvItemName, 10);
        Utility.setFontBold(tvItemName);
        tvItemName.setText(position + 1 + ". " + data.getItemName());

        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        Utility.setFont(context, tvQuantity);
        tvQuantity.setText("تعداد سفارش: " + ThousandSeparatorWatcher.addSeparator( data.getQuantity()));

        TextView tvDeliverQuantity = convertView.findViewById(R.id.tvDeliverQuantity);
        Utility.setFont(context, tvDeliverQuantity);
        tvDeliverQuantity.setText("تعداد تحويل: " + ThousandSeparatorWatcher.addSeparator( data.getDeliverQuantity()));

        TextView tvUnitPrice = convertView.findViewById(R.id.tvUnitPrice);
        Utility.setFont(context, tvUnitPrice);
        tvUnitPrice.setText("قيمت واحد: " + ThousandSeparatorWatcher.addSeparator( data.getUnitPrice()));

        TextView tvTotalPrice = convertView.findViewById(R.id.tvTotalPrice);
        Utility.setFont(context, tvTotalPrice);
        tvTotalPrice.setText("قيمت كل: " + ThousandSeparatorWatcher.addSeparator( data.getUnitPrice() * data.getDeliverQuantity()));

        TextView tvComment = convertView.findViewById(R.id.tvComment);
        Utility.setFont(context, tvComment);
        LinearLayout lComment = convertView.findViewById(R.id.lComment);
        if(data.getComment().equals(""))
        {
            lComment.setVisibility(View.GONE);
        }
        else
        {
            lComment.setVisibility(View.VISIBLE);
            tvComment.setText("توضيحات: " + data.getComment());
        }

        int buttonDecTextSize = -20;

        Button bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeliverItemListCommandListener.OnDeliverItemListCommand(data, position, DeliverItemListCommandType.Edit);
            }
        });

        return convertView;
    }
    public interface OnDeliverItemListCommandListener
    {
        void OnDeliverItemListCommand(DeliverItemData deliverItemData, int position, DeliverItemListCommandType commandType);
    }
    public enum DeliverItemListCommandType
    {
        Unknown
        ,Edit
    }
}
