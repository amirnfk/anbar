package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.EditInboundDetailActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Models.UpdateDetailResponse;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.UpdateStatusDetailRequest;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class row_inbound_detail extends ArrayAdapter<InboundDetailData>
{
    Context context;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    private OnInboundDetailListCommandListener onInboundDetailListCommandListener;

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
    public row_inbound_detail(Context context, ArrayList<InboundDetailData> list)
    {
        super(context, 0, list);
        this.context = context;

        try
        {
            onInboundDetailListCommandListener = (OnInboundDetailListCommandListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onInboundDetailListCommandListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final InboundDetailData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_inbound_detail, parent, false);
        }
        Utility.setGridIntervalColor(position, selectedPos, convertView, context);

        TextView tvRowCount = convertView.findViewById(R.id.tvRowCount);
        Utility.setFont(context, tvRowCount);
        tvRowCount.setText("." + (position + 1));

        TextView tvName = convertView.findViewById(R.id.tvName);
        Utility.setFont(context, tvName);
        tvName.setText(data.getItemId() + " - " + data.getItemName());

        TextView tvUMREZ = convertView.findViewById(R.id.tvUMREZ);
        Utility.setFont(context, tvUMREZ);
        tvUMREZ.setText("تعداد در جعبه: " + data.getUMREZ());

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        Utility.setFont(context, tvAmount);
        tvAmount.setText("تعداد تحويل: " + ThousandSeparatorWatcher.addSeparator( data.getUserCount()) + " " + data.getUserMeins());

        int buttonDecTextSize = -20;

        Button bEdit = convertView.findViewById(R.id.bEdit);
        Button bConfirm = convertView.findViewById(R.id.bConfirm);
        Button bReject = convertView.findViewById(R.id.bReject);
        Utility.setFont(context, bEdit);
        Utility.increaseTextSize(bEdit, buttonDecTextSize);

        if (data.getStatusID() ==2) {
            bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
        } else {
            bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark_dis, 0);
        }
        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onInboundDetailListCommandListener.OnInboundDetailListCommand(data, position, InboundDetailListCommandType.Confirm );
                if(data.getStatusID()==2){
                    data.setStatusID(1);
                    UpdateInboundListDetailStatusDataUsingRestApi(data.getID(), 1,bConfirm);
                } else if (data.getStatusID()==1 || data.getStatusID()==0) {
                    UpdateInboundListDetailStatusDataUsingRestApi(data.getID(), 2,bConfirm);
                    data.setStatusID(2);
                }

            }
        });
        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onInboundDetailListCommandListener.OnInboundDetailListCommand(data, position, InboundDetailListCommandType.Edit);
            }
        });

        ImageButton bDelete = convertView.findViewById(R.id.bDelete);
        Utility.setFont(context, bDelete);
        Utility.increaseTextSize(bDelete, buttonDecTextSize);

        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onInboundDetailListCommandListener.OnInboundDetailListCommand(data, position, InboundDetailListCommandType.Delete);
            }
        });

        return convertView;
    }
    public interface OnInboundDetailListCommandListener
    {
        void OnInboundDetailListCommand(InboundDetailData inboundDetailData, int position, InboundDetailListCommandType commandType);
    }
    public enum InboundDetailListCommandType
    {
        Unknown
        ,Edit
        ,Delete
        ,Confirm
    }

    private void UpdateInboundListDetailStatusDataUsingRestApi(int inBoundId,int  newStatusId ,Button _bConfirm) {

        UpdateStatusDetailRequest request = new UpdateStatusDetailRequest(inBoundId,   newStatusId,new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(context),"", ApplicationMode.StoreHandheld.toString(),Utility.getDeviceInfo(),GlobalData.getStoreID()));

        // Create Retrofit client and service



        Common c = new Common(context);
        String s = c.URL();


        ApiInterface apiService;

        if (s.contains("pilot")) {
            apiService = Inbound_Data_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiService = Inbound_Data_API_Operation.getAPI().create(ApiInterface.class);
        }


        // Make the POST request
        Call<UpdateDetailResponse> call = apiService.UpdateInboundDetailStatus(request);
        call.enqueue(new Callback<UpdateDetailResponse>() {
            @Override
            public void onResponse(Call<UpdateDetailResponse> call, Response<UpdateDetailResponse> response) {


                if (response.body().getMessage().equals("Successful.")) {
if(newStatusId==2){


    _bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);

}else{


    _bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark_dis, 0);

}


                }else {
                    Toast.makeText(context, "Request Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateDetailResponse> call, Throwable t) {

                Toast.makeText(context, "Request Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
