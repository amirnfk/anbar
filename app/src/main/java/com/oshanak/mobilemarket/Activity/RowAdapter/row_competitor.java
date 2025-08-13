package com.oshanak.mobilemarket.Activity.RowAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_competitor extends ArrayAdapter<CompetitorData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnCompetitorListCommandListener onCompetitorListCommandListener;

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
    public row_competitor(Context context, ArrayList<CompetitorData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onCompetitorListCommandListener = (OnCompetitorListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnCompetitorListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final CompetitorData data = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_competitor, parent, false);
        }
        Utility.setGridIntervalColor( position, selectedPos, convertView, context);

        TextView tvCompanyName = convertView.findViewById(R.id.tvCompanyName);
        Utility.setFont(context, tvCompanyName);
        Utility.increaseTextSize(tvCompanyName, 10);
        Utility.setFontBold(tvCompanyName);
        tvCompanyName.setText( position + 1 + ". " + data.getNameWithCompany());

        TextView tvInsertDate = convertView.findViewById(R.id.tvInsertDate);
        Utility.setFont(context, tvInsertDate);
        tvInsertDate.setText( data.getDateTime());

        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        Utility.setFont(context, tvUserName);
        tvUserName.setText( data.getRegisterUserName());

        int buttonDecTextSize = -20;

        Button bAddPrice = convertView.findViewById(R.id.bAddPrice);
        Utility.setFont(context,bAddPrice);
        Utility.increaseTextSize(bAddPrice, buttonDecTextSize);

        Button bPriceList = convertView.findViewById(R.id.bPriceList);
        Utility.setFont(context,bPriceList);
        Utility.increaseTextSize(bPriceList, buttonDecTextSize);

        Button bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context,bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        Button bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context,bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bAddPrice.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCompetitorListCommandListener.OnCompetitorListCommand(data, position, CompetitorListCommandType.AddPrice);
            }
        });
        bEdit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCompetitorListCommandListener.OnCompetitorListCommand(data, position, CompetitorListCommandType.Edit);
            }
        });
        bPriceList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCompetitorListCommandListener.OnCompetitorListCommand(data, position, CompetitorListCommandType.PriceList);
            }
        });
        bDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCompetitorListCommandListener.OnCompetitorListCommand(data, position, CompetitorListCommandType.Delete);
            }
        });

        return convertView;
    }
    public interface OnCompetitorListCommandListener
    {
        void OnCompetitorListCommand(CompetitorData selectedCompetitor, int position, CompetitorListCommandType commandType);
    }
    public enum CompetitorListCommandType
    {
        Unknown
        ,AddPrice
        ,Edit
        ,PriceList
        ,Delete
    }
}
