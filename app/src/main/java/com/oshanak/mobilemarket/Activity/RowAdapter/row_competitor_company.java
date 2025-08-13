package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorCompanyData;
import com.oshanak.mobilemarket.R;

import java.util.List;


public class row_competitor_company extends ArrayAdapter<CompetitorCompanyData>
{

    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnCompetitorCompanyCommandListener onCompetitorCompanyCommandListener;

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

    public row_competitor_company(Context context, List<CompetitorCompanyData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onCompetitorCompanyCommandListener = (OnCompetitorCompanyCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnCompetitorCompanyCommandListener");
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final CompetitorCompanyData item = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_competitor_company, parent, false);
        }
        Utility.setGridIntervalColor( position, selectedPos, convertView, context);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        Utility.setFont(context, tvTitle);
        Utility.increaseTextSize(tvTitle, 20);
        tvTitle.setText(position + 1 + ". " + item.getName());

        int buttonDecTextSize = -20;

        Button bSelect = convertView.findViewById(R.id.bSelect);
        Utility.setFont(context,bSelect);
        Utility.increaseTextSize(bSelect, buttonDecTextSize);

        bSelect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCompetitorCompanyCommandListener.OnCompetitorCompanyCommand(item, position, CompetitorCompanyCommandType.Select);
            }
        });

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView( position, convertView, parent);
    }
    public interface OnCompetitorCompanyCommandListener
    {
        void OnCompetitorCompanyCommand(CompetitorCompanyData competitorCompanyData, int position, CompetitorCompanyCommandType commandType);
    }
    public enum CompetitorCompanyCommandType
    {
        Unknown
        ,Select
    }
}
