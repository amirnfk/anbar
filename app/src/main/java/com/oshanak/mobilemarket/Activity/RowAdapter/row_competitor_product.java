package com.oshanak.mobilemarket.Activity.RowAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorProductData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_competitor_product extends ArrayAdapter<CompetitorProductData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnCompetitorPriceListCommandListener onCompetitorPriceListCommandListener;

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
    public row_competitor_product(Context context, ArrayList<CompetitorProductData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onCompetitorPriceListCommandListener = (OnCompetitorPriceListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnCompetitorPriceListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final CompetitorProductData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_competitor_product, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvProduct = convertView.findViewById(R.id.tvProduct);
        Utility.setFont(context, tvProduct);
        Utility.increaseTextSize(tvProduct, 10);
        Utility.setFontBold(tvProduct);
        tvProduct.setText(position + 1 + ". " + data.getName());

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        Utility.setFont(context, tvDate);
        tvDate.setText(data.getDateTime());

        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        Utility.setFont(context, tvPrice);
        tvPrice.setText("قيمت: " + ThousandSeparatorWatcher.addSeparator(data.getPrice()));

        CheckBox cbPromotion = convertView.findViewById(R.id.cbPromotion);
        Utility.setFont(context, cbPromotion);
        cbPromotion.setChecked(data.getIsPromotion() == 1);

        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        Utility.setFont(context, tvUserName);
        tvUserName.setText(data.getRegisterUserName());

        int buttonDecTextSize = -20;

        Button bEdit = convertView.findViewById(R.id.bEdit);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        Button bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context, bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCompetitorPriceListCommandListener.OnCompetitorPriceListCommand(data, position, CompetitorPriceListCommandType.Edit);
            }
        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCompetitorPriceListCommandListener.OnCompetitorPriceListCommand(data, position, CompetitorPriceListCommandType.Delete);
            }
        });

        return convertView;
    }
    public interface OnCompetitorPriceListCommandListener
    {
        void OnCompetitorPriceListCommand(CompetitorProductData selectedCompetitorPrice, int position, CompetitorPriceListCommandType commandType);
    }
    public enum CompetitorPriceListCommandType
    {
        Unknown
        ,Edit
        ,Delete
    }
}
