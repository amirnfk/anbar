package com.oshanak.mobilemarket.Activity.PickingApp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
import com.oshanak.mobilemarket.Activity.Enum.ProductUnit;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_picking_control_item extends ArrayAdapter<PickingDeliverItemData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnPickingControlItemCommandListener onPickingControlItemCommandListener;

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
    public row_picking_control_item(Context context, ArrayList<PickingDeliverItemData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onPickingControlItemCommandListener = (row_picking_control_item.OnPickingControlItemCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnPickingControlItemCommandListener");
        }
    }

    private String getUnitDescription(String unitName)
    {
        if(unitName.equals(ProductUnit.ST.getName()))
        {
            return ProductUnit.ST.getShortDesc();
        }
        else if(unitName.equals(ProductUnit.KAR.getName()))
        {
            return ProductUnit.KAR.getShortDesc()/* + "(" + BQ + ")"*/;
        }
        else if(unitName.equals(ProductUnit.G.getName()))
        {
            return ProductUnit.G.getShortDesc();
        }
        else if(unitName.equals(ProductUnit.KG.getName()))
        {
            return ProductUnit.KG.getName()/* + "(" + BQ + ")"*/;
        }
        return "";
    }
    private String getBQ(String unitName, int BQ)
    {
        if(unitName.equals(ProductUnit.ST.getName()))
        {
            return "";
        }
        else if(unitName.equals(ProductUnit.KAR.getName()))
        {
            return "(" + BQ + ")";
        }
        else if(unitName.equals(ProductUnit.G.getName()))
        {
            return "";
        }
        else if(unitName.equals(ProductUnit.KG.getName()))
        {
            return "(" + BQ + ")";
        }
        return "";
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final PickingDeliverItemData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_picking_control_item, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvArticleDefinition = convertView.findViewById(R.id.tvArticleDefinition);
        Utility.setFont(context, tvArticleDefinition);
        tvArticleDefinition.setText(data.getMATNR() + " - " + data.getARKTX());

        TextView tvPositionNo = convertView.findViewById(R.id.tvPositionNo);
        Utility.setFont(context, tvPositionNo);
        tvPositionNo.setText("موقعيت: " + data.getLGPBE());

        //region picking quantity
        TextView tvPickingQuantity1 = convertView.findViewById(R.id.tvPickingQuantity1);
        Utility.setFont(context, tvPickingQuantity1);

        TextView tvPickingQuantity2 = convertView.findViewById(R.id.tvPickingQuantity2);
        Utility.setFont(context, tvPickingQuantity2);
        Utility.setFontBold(tvPickingQuantity2);
        Utility.increaseTextSize(tvPickingQuantity2, 25);
        tvPickingQuantity2.setText(data.getDeliverAmount() + " " + getUnitDescription(data.getDeliverUnit()));

        TextView tvPickingQuantity3 = convertView.findViewById(R.id.tvPickingQuantity3);
        Utility.setFont(context, tvPickingQuantity3);
        tvPickingQuantity3.setText(getBQ(data.getDeliverUnit(), data.getUMVKZ()));
        //endregion picking quantity

        //region bConfirm
        Button bConfirm = convertView.findViewById(R.id.bConfirm);
        if(data.isFinalControlled())
        {
            bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
        }
        else {
            bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark_dis, 0);
        }
        bConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                boolean isFinalControlled = false;
                isFinalControlled = !data.isFinalControlled();


                onPickingControlItemCommandListener.OnPickingControlItemCommand(data, isFinalControlled, position, PickingControlItemCommandType.FinalControl);
            }
        });
        //endregion bConfirm

        return convertView;
    }
    public interface OnPickingControlItemCommandListener
    {
        void OnPickingControlItemCommand(PickingDeliverItemData pickingDeliverItemData,boolean isFinalControlled, int position
                , row_picking_control_item.PickingControlItemCommandType commandType);
    }
    public enum PickingControlItemCommandType
    {
        Unknown
        ,FinalControl
    }
}
