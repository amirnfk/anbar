package com.oshanak.mobilemarket.Activity.RowAdapter;


import static android.app.Activity.RESULT_OK;
import static com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity.EXTRA_IMAGE_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oshanak.mobilemarket.Activity.Activity.Document_Detail_Activity;
import com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity;
import com.oshanak.mobilemarket.Activity.Activity.Uploaded_Docs_Model;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Models.DeleteDocRequestModel;
import com.oshanak.mobilemarket.Activity.Models.DeleteDocResult;
import com.oshanak.mobilemarket.Activity.Models.ItemModel;
import com.oshanak.mobilemarket.Activity.Models.metaData;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.ViewHolder> {

    Context context;
Activity activity;
    int resource;
    private OnItemClickListener mOnItemClickListener;
    ArrayList<ItemModel> myList;
    ArrayList<ItemModel> myListAll;


    public interface OnItemClickListener {
        void onItemClick(View view, ItemModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Items_Adapter(Context context,
                         int resource,
                         ArrayList<ItemModel> myList,
    Activity activity) {
this.activity=activity;
        this.context = context;
        this.myList = myList;
        this.resource = resource;


    }

    @SuppressLint("ResourceAsColor")

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_inner, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {




        holder.txtItemName.setText(myList.get(position).getName() + "");
        holder.txtItemBarcode.setText( "بارکد کالا: "+ myList.get(position).getBarcode() );
        holder.tvInboundPosition2.setText( (position+1)+"" );
        holder.lyt_uploaded_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResultAndFinish(myList.get(position).getBarcode());
            }
        });




    }


    public void returnResultAndFinish(String barcode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("searchByNameKey", barcode);
        Utility.playBeep();
        activity.setResult(RESULT_OK, resultIntent);
        activity.finish();
    }


    @Override
    public int getItemCount() {
        return myList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtItemName;
        TextView txtItemBarcode;
        TextView tvInboundPosition2;

        LinearLayout lyt_uploaded_doc;


        public ViewHolder(View convertview) {
            super(convertview);
            txtItemName = convertview.findViewById(R.id.itemName);
            txtItemBarcode = convertview.findViewById(R.id.itemBarcode);
            tvInboundPosition2 = convertview.findViewById(R.id.tvInboundPosition2);
            lyt_uploaded_doc = convertview.findViewById(R.id.lyt_uploaded_doc);

        }
    }


}

