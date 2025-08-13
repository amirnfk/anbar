package com.oshanak.mobilemarket.Activity.RowAdapter;


import static com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity.EXTRA_IMAGE_URL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
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

public class Docs_Adapter extends RecyclerView.Adapter<Docs_Adapter.ViewHolder> {

    Context context;

    int resource;
    private OnItemClickListener mOnItemClickListener;
    ArrayList<Uploaded_Docs_Model> myList;
    ArrayList<Uploaded_Docs_Model> myListAll;


    public interface OnItemClickListener {
        void onItemClick(View view, Uploaded_Docs_Model obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Docs_Adapter(Context context,
                        int resource,
                        ArrayList<Uploaded_Docs_Model> myList
    ) {

        this.context = context;
        this.myList = myList;
        this.resource = resource;


    }

    @SuppressLint("ResourceAsColor")

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploaded_docs_inner, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int _position) {


        String url = myList.get(_position).getUploaded_Doc_img1();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.receipt_search);
        Glide.with(context).load(url).apply(options).into(holder.img_uploaded_doc);
holder.img_uploaded_doc.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        Intent intent = new Intent(context, ImageEnlargedActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        context.startActivity(intent);
    }
});

        String Doc_Uploaded_Date = myList.get(_position).getUploaded_Doc_Date().replace(" ", "\n");
        holder.txt_docs_type.setText(myList.get(_position).getDocumentTypeTitle() + "");
        holder.txt_docs_title.setText(myList.get(_position).getUploaded_Doc_Title() + "");
        holder.txt_docs_date.setText(Doc_Uploaded_Date);

        holder.crd_uploaded_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetails(_position, myList);
            }
        });
        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    goToDetails(_position,myList);
                }catch (Exception e){
                    Toast.makeText(context, "مشکلی در ارتباط با سرور رخ داد.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_answer_confirmation_not_able_to_receive);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.findViewById(R.id.btn_confirm_answer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            deleteDocById(myList.get(_position).getUploaded_Doc_Id(), _position);
                        } catch (Exception e) {
                            Toast.makeText(context, "حذف سند موفقیت آمیز نبود.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                dialog.findViewById(R.id.btn_cancel_answer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
                dialog.show();


            }
        });

    }

    private void deleteDocById(int uploaded_doc_id, int position) {
        Common c = new Common(context);
        String s = c.URL();


        ApiInterface apiInterface;

        if (s.contains("pilot")) {
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }
        Call<DeleteDocResult> logCall = apiInterface.deleteDocById(
                "DeleteDocument", new DeleteDocRequestModel(uploaded_doc_id, new metaData(GlobalData.getUserName(), GlobalData.getAppVersionCode(), Utility.getDeviceInfo(), Utility.applicationMode + "")));

        logCall.enqueue(new Callback<DeleteDocResult>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<DeleteDocResult> call, Response<DeleteDocResult> response) {

                if (response.body().isSuccessful() == true && response.body().getMessage().equals("Successful.")) {

                    Toast.makeText(context, "سند با موفقیت حذف شد.", Toast.LENGTH_SHORT).show();
                    myList.remove(position);
                    notifyDataSetChanged();

                } else {

                    Toast.makeText(context, "حذف سند موفقیت آمیز نبود.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DeleteDocResult> call, Throwable t) {
                Toast.makeText(context, "حذف سند موفقیت آمیز نبود.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToDetails(int _position, ArrayList<Uploaded_Docs_Model> __myList) {

        Intent intent = new Intent(context, Document_Detail_Activity.class);
        intent.putExtra("doc_id", __myList.get(_position).getUploaded_Doc_Id());
        intent.putExtra("doc_title", __myList.get(_position).getUploaded_Doc_Title());
        intent.putExtra("doc_type_code", __myList.get(_position).getDocumentTypeCode());
        intent.putExtra("doc_desc", __myList.get(_position).getUploaded_Doc_Desc());
        intent.putExtra("doc_date", __myList.get(_position).getUploaded_Doc_Date());
        intent.putExtra("doc_url_1_id", __myList.get(_position).getUploaded_Doc_img1_ID());
        intent.putExtra("doc_url_2_id", __myList.get(_position).getUploaded_Doc_img2_ID());
        intent.putExtra("doc_url_3_id", __myList.get(_position).getUploaded_Doc_img3_ID());
        intent.putExtra("doc_url_4_id", __myList.get(_position).getUploaded_Doc_img4_ID());
        intent.putExtra("doc_url_5_id", __myList.get(_position).getUploaded_Doc_img5_ID());
        intent.putExtra("doc_url_1", __myList.get(_position).getUploaded_Doc_img1());
        intent.putExtra("doc_url_2", __myList.get(_position).getUploaded_Doc_img2());
        intent.putExtra("doc_url_3", __myList.get(_position).getUploaded_Doc_img3());
        intent.putExtra("doc_url_4", __myList.get(_position).getUploaded_Doc_img4());
        intent.putExtra("doc_url_5", __myList.get(_position).getUploaded_Doc_img5());

        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return myList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_uploaded_doc;
        TextView txt_docs_title;
        TextView txt_docs_type;
        TextView txt_docs_date;
        CardView crd_uploaded_doc;
        ImageButton btnedit;
        ImageButton btnDelete;
        LinearLayout lyt_uploaded_doc;


        public ViewHolder(View convertview) {
            super(convertview);
            txt_docs_date = convertview.findViewById(R.id.txt_docs_date);
            txt_docs_title = convertview.findViewById(R.id.txt_docs_title);
            txt_docs_type = convertview.findViewById(R.id.txt_docs_type);
            img_uploaded_doc = convertview.findViewById(R.id.img_uploaded_doc);
            lyt_uploaded_doc = convertview.findViewById(R.id.lyt_uploaded_doc);
            crd_uploaded_doc = convertview.findViewById(R.id.crd_uploaded_doc);
            btnedit = convertview.findViewById(R.id.btn_edit);
            btnDelete = convertview.findViewById(R.id.btn_delete);
        }
    }


}

