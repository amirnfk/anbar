package com.oshanak.mobilemarket.Activity.RowAdapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Activity.StoreInboundDetailActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;


public class row_inbound_header extends ArrayAdapter<InboundHeaderData>
{
    private Activity activity;

    Context context;
    public interface ResetOperationClickListener {
        void onResetOperationClick();
    }
    private ResetOperationClickListener resetOperationClickListener;

    public void setResetOperationClickListener(ResetOperationClickListener listener) {
        this.resetOperationClickListener = listener;
    }
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
//    private OnPickingOrderListCommandListener onPickingOrderListCommandListener;

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
    public row_inbound_header(Context context, ArrayList<InboundHeaderData> list , Activity activity )
    {
        super(context, 0, list );
        this.context = context;
        this.activity = activity;

//        try
//        {
//            onPickingOrderListCommandListener = (OnPickingOrderListCommandListener) context;
//        }
//        catch (ClassCastException e)
//        {
//            throw new ClassCastException(context.toString() + " must implement OnPickingOrderListCommandListener");
//        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final InboundHeaderData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_inbound_header, parent, false);
        }
        Utility.setGridIntervalColor2(position, selectedPos, convertView, context);

        TextView tvInboundId = convertView.findViewById(R.id.tvInboundId);
        Button tvContinueOperation = convertView.findViewById(R.id.tvContinueOperation);
        Button tvResetOperation = convertView.findViewById(R.id.tvResetOperation);
        TextView tvInboundPosition = convertView.findViewById(R.id.tvInboundPosition);
        TextView tvInboundDate = convertView.findViewById(R.id.tvDate);
        TextView tvInboundType = convertView.findViewById(R.id.tvInboundtype);
        TextView tvInboundStoreId = convertView.findViewById(R.id.tvInboundStoreId);
        LinearLayout lytRoot=convertView.findViewById(R.id.lyt_root);
//        Utility.setFont(context, tvInboundId);
//        Utility.increaseTextSize(tvInboundId, 10);
//        Utility.setFontBold(tvInboundId);
        tvInboundPosition.setText((position + 1) +"" );
        tvInboundId.setText(data.getInboundId()+""  );
        tvInboundDate.setText(data.getDATEN_S()+""  );
        tvContinueOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInboundDetailActivity(data);
            }
        });
        tvResetOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetOperationClickListener != null) {
                    resetOperationClickListener.onResetOperationClick( );
                }




            }
        });



        switch (data.getLOGGR()){
            case "غذایی":
                tvInboundType.setText( data.getLOGGR());
                tvInboundType.setTextColor(context.getResources().getColor(R.color.red_500));
                tvInboundId.setTextColor(context.getResources().getColor(R.color.red_500));
                tvContinueOperation.setTextColor(context.getResources().getColor(R.color.red_500));
                tvResetOperation.setTextColor(context.getResources().getColor(R.color.red_500));
                tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.red_500));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.red_500));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.orange_50));
                break;
            case "شوینده":
                tvInboundType.setText( data.getLOGGR());
                tvInboundType.setTextColor(context.getResources().getColor(R.color.dribble_green));
                tvInboundId.setTextColor(context.getResources().getColor(R.color.dribble_green));
                tvContinueOperation.setTextColor(context.getResources().getColor(R.color.dribble_green));
                tvResetOperation.setTextColor(context.getResources().getColor(R.color.dribble_green));
                tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.dribble_green));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.dribble_green));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                break;
            case "یخچالی":
                tvInboundType.setText( data.getLOGGR());
                tvInboundType.setTextColor(context.getResources().getColor(R.color.haft_blue));
                tvInboundId.setTextColor(context.getResources().getColor(R.color.haft_blue));
                tvContinueOperation.setTextColor(context.getResources().getColor(R.color.haft_blue));
                tvResetOperation.setTextColor(context.getResources().getColor(R.color.haft_blue));
                tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.haft_blue));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.haft_blue));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
                break;
        }


//        if(data.getStatusCode() == PickingrderStatus.Ready.getCode())
//        {
//            tvStatusName.setTextColor(Color.GRAY);
//        }
//        else if(data.getStatusCode() == PickingOrderStatus.InPacking.getCode())
//        {
//            tvStatusName.setTextColor(Color.BLACK);
//        }
//        else if(data.getStatusCode() == PickingOrderStatus.PickedUp.getCode())
//        {
//            tvStatusName.setTextColor(Color.BLUE);
//        }
//        else
//        {
//            tvStatusName.setTextColor(Color.GRAY);
//        }

//        int buttonDecTextSize = -20;
//        Button bItems = convertView.findViewById(R.id.bItems);
//        Utility.setFont(context, bItems);
//        Utility.increaseTextSize(bItems, buttonDecTextSize);
//
//        bItems.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onPickingOrderListCommandListener.OnPickingOrderListCommand(data, position, PickingOrderListCommandType.Items);
//            }
//        });

        return convertView;
    }

    private void openInboundDetailActivity(InboundHeaderData data)
    {
        Intent intent = new Intent(context, StoreInboundDetailActivity.class);

         intent.putExtra("inboundHeaderData", data);
        startActivity(context,intent,null);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

//    public interface OnPickingOrderListCommandListener
//    {
//        void OnPickingOrderListCommand(PickingDeliverHeaderData pickingDeliverHeaderData, int position, PickingOrderListCommandType commandType);
//    }
//    public enum PickingOrderListCommandType
//    {
//        Unknown
//        ,Items
//    }
}
