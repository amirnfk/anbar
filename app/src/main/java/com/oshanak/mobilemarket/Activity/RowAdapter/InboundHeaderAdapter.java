package com.oshanak.mobilemarket.Activity.RowAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.Activity.StoreDeliverInboundHeaderActivity;
import com.oshanak.mobilemarket.Activity.Activity.StoreInboundDetailActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundApiService;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundClearRequestModel;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundRequestModel;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundResponseModel;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.OpenInboundRetrofitClientOperation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.OpenInboundRetrofitClientPilot;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboundHeaderAdapter extends RecyclerView.Adapter<InboundHeaderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<InboundHeaderData> dataList;

    public InboundHeaderAdapter(Context context, ArrayList<InboundHeaderData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_inbound_header, parent, false);
        return new ViewHolder(view);
    }

    public void clearData() {
        if (!dataList.isEmpty()){
            dataList.clear();
        }

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InboundHeaderData data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInboundId, tvInboundPosition, tvInboundDate, tvInboundType , tvLastStatusName;
        Button tvContinueOperation, tvResetOperation, tvStartNewOperation;
        LinearLayout lytRoot , lytOperationButtoms;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInboundId = itemView.findViewById(R.id.tvInboundId);
            tvInboundPosition = itemView.findViewById(R.id.tvInboundPosition);
            tvInboundDate = itemView.findViewById(R.id.tvDate);
            tvInboundType = itemView.findViewById(R.id.tvInboundtype);
            tvContinueOperation = itemView.findViewById(R.id.tvContinueOperation);
            tvLastStatusName = itemView.findViewById(R.id.tvStatusName);
            tvResetOperation = itemView.findViewById(R.id.tvResetOperation);
            tvStartNewOperation = itemView.findViewById(R.id.tvStartNewOperation);
            lytRoot = itemView.findViewById(R.id.lyt_root);
            lytOperationButtoms = itemView.findViewById(R.id.lyt_operation_buttons);
        }

        public void bind(final InboundHeaderData data) {


            if (data.getSendToSapStatus()==1) {
                lytOperationButtoms.setVisibility(View.GONE);
                tvResetOperation.setVisibility(View.GONE);
                tvContinueOperation.setVisibility(View.GONE);
                tvStartNewOperation.setVisibility(View.GONE);
            } else if (data.getSendToSapStatus()==0) {
                switch (data.getItemCount() ){
                    case  0:
                        lytOperationButtoms.setVisibility(View.VISIBLE);
                        tvResetOperation.setVisibility(View.GONE);
                        tvContinueOperation.setVisibility(View.GONE);
                        tvStartNewOperation.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        lytOperationButtoms.setVisibility(View.VISIBLE);
                        tvStartNewOperation.setVisibility(View.GONE);
                        tvResetOperation.setVisibility(View.VISIBLE);
                        tvContinueOperation.setVisibility(View.VISIBLE);
                        break;
                    default:
                        lytOperationButtoms.setVisibility(View.VISIBLE);
                        tvStartNewOperation.setVisibility(View.GONE);
                        tvResetOperation.setVisibility(View.VISIBLE);
                        tvContinueOperation.setVisibility(View.VISIBLE);
                        break;
                }
            }







            tvInboundPosition.setText((getAdapterPosition() + 1) + "");
            tvInboundId.setText(data.getInboundId() + "");
            tvInboundDate.setText(data.getDATEN_S() + "");
            if (data.getLastUpdateUser().length()>2){
                tvLastStatusName.setText("آخرین تغییرات توسط "+ data.getLastUpdateUserName());
            }else{
                tvLastStatusName.setText("");
            }
            switch (data.getLOGGR()){
                case "غذایی":
                    tvInboundType.setText( data.getLOGGR());
                    tvInboundType.setTextColor(context.getResources().getColor(R.color.red_500));
                    tvInboundId.setTextColor(context.getResources().getColor(R.color.red_500));
                    tvContinueOperation.setTextColor(context.getResources().getColor(R.color.red_500));
                    tvResetOperation.setTextColor(context.getResources().getColor(R.color.red_500));
                    tvStartNewOperation.setTextColor(context.getResources().getColor(R.color.red_500));
//                    tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.red_500));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.red_500));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.orange_50));
                    break;
                case "شوینده":
                    tvInboundType.setText( data.getLOGGR());
                    tvInboundType.setTextColor(context.getResources().getColor(R.color.dribble_green));
                    tvInboundId.setTextColor(context.getResources().getColor(R.color.dribble_green));
                    tvContinueOperation.setTextColor(context.getResources().getColor(R.color.dribble_green));
                    tvResetOperation.setTextColor(context.getResources().getColor(R.color.dribble_green));
                    tvStartNewOperation.setTextColor(context.getResources().getColor(R.color.dribble_green));
//                    tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.dribble_green));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.dribble_green));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                    break;
                case "یخچالی":
                    tvInboundType.setText( data.getLOGGR());
                    tvInboundType.setTextColor(context.getResources().getColor(R.color.haft_blue));
                    tvInboundId.setTextColor(context.getResources().getColor(R.color.haft_blue));
                    tvContinueOperation.setTextColor(context.getResources().getColor(R.color.haft_blue));
                    tvResetOperation.setTextColor(context.getResources().getColor(R.color.haft_blue));
                    tvStartNewOperation.setTextColor(context.getResources().getColor(R.color.haft_blue));
//                    tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.haft_blue));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.haft_blue));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
                    break;
                default:
                    tvInboundType.setText( "...");
                    tvInboundType.setTextColor(context.getResources().getColor(R.color.Black));
                    tvInboundId.setTextColor(context.getResources().getColor(R.color.Black));
                    tvContinueOperation.setTextColor(context.getResources().getColor(R.color.Black));
                    tvResetOperation.setTextColor(context.getResources().getColor(R.color.Black));
                    tvStartNewOperation.setTextColor(context.getResources().getColor(R.color.Black));
//                    tvInboundPosition.setBackgroundColor(context.getResources().getColor(R.color.Black));
//                tvInboundPosition.setTextColor(context.getResources().getColor(R.color.haft_blue));
//                lytRoot.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
                    break;
            }
            tvContinueOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInboundDetailActivity(data);
                }
            });

            tvResetOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                    String message = "آیا می خواهید اطلاعاتی که قبلا ثبت شده اند حذف شوند؟";

                    dlgAlert.setMessage(message);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton("بله",
                            (DialogInterface dialog, int which)->
                            {
                                resetInboundHeaderData(data);

                            });
                    dlgAlert.setNegativeButton("خیر",
                            (DialogInterface dialog, int which)-> {
                            });
                    dlgAlert.setIcon(R.drawable.question128);
                    dlgAlert.create().show();



                }
            });
            tvStartNewOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    openInboundDetailActivity(data);
                }
            });

            // Set text and colors based on LOGGR value as before
        }


    }
    private void openInboundDetailActivity(InboundHeaderData data) {

        Intent intent = new Intent(context, StoreInboundDetailActivity.class);
        intent.putExtra("inboundHeaderData", data);
        intent.putExtra("inboundHeaderID", data.getID()+"");
        context.startActivity(intent);
    }
    private void resetInboundHeaderData(InboundHeaderData data) {

        Common c = new Common(context);
        String s = c.URL();


        OpenInboundApiService apiService;

        if (s.contains("pilot")) {
            apiService = OpenInboundRetrofitClientPilot.getClient().create(OpenInboundApiService.class);
        } else {
            apiService = OpenInboundRetrofitClientOperation.getClient().create(OpenInboundApiService.class);
        }




        // Prepare your request body
        OpenInboundClearRequestModel requestModel = new OpenInboundClearRequestModel();
        requestModel.setID( data.getID()+"");
        MetaData metaData = new MetaData();
        metaData.setUserName(GlobalData.getUserName());
        metaData.setAppVersionCode(GlobalData.getAppVersionCode());

        metaData.setDeviceInfo(Utility.getDeviceInfo()+"");
        metaData.setAppMode("StoreHandheld");
        requestModel.setMetaData(metaData);
        Log.e("API_CALL", "resp0onse code: " + "responseBody" );
        // Make the API call
        Call<TaskResult> call = apiService.ClearInboundHeader(requestModel);
        call.enqueue(new Callback<TaskResult>() {
            @Override
            public void onResponse(Call<TaskResult> call, Response<TaskResult> response) {
                TaskResult responseBody = response.body();
                if (response.isSuccessful() && responseBody.message.equals("Successful.")) {

                    Log.e("API_CALL", "resp0onse code: " + responseBody.message.toString());
                    Toast.makeText(context, "حذف اطلاعات موفقیت  بود", Toast.LENGTH_SHORT).show();
                    openInboundDetailActivity(data);

                } else if (response.isSuccessful() && response.body().message.equals("no item to delete.")) {
                    Log.e("API_CALL", "resp0onse code: " + "responseBody...........".toString());
                    // Handle error response
                    Toast.makeText(context, "آیتمی برای حذف وجود ندارد", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e("API_CALL", "resp0onse code: " + response.body().message.toString());
                    Toast.makeText(context, "حذف اطلاعات موفقیت آمیز نبود", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskResult> call, Throwable t) {
                // Handle failure
                Log.e("API_CALL", "Failed: " + t.getMessage());
            }
        });
    }
}
