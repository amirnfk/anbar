package com.oshanak.mobilemarket.Activity.RowAdapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.Activity.New_Document_Upload_Activity;
import com.oshanak.mobilemarket.Activity.Activity.Uploaded_Docs_Model;
import com.oshanak.mobilemarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Docs_Image_Url_Adapter extends RecyclerView.Adapter<Docs_Image_Url_Adapter.ViewHolder> {

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

    public Docs_Image_Url_Adapter(Context context,
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
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {


//        String url = myList.get(position).getUploaded_Doc_Date();
        String url = "";



        Picasso picasso = new Picasso.Builder(context).build();

        picasso.get()
                .load(url)
                .placeholder(R.drawable.receipt_search)
                .error(R.drawable.receipt_search)
                .into(holder.img_uploaded_doc);


        holder.txt_docs_type.setText(myList.get(position).getDocumentTypeTitle() + "");
        holder.txt_docs_title.setText(myList.get(position).getUploaded_Doc_Title() + "");
        holder.txt_docs_date.setText(myList.get(position).getUploaded_Doc_Date() + "");

        holder.crd_uploaded_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, New_Document_Upload_Activity.class);
                context.startActivity(intent);
            }
        });
//        holder.lyt_uploaded_doc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null) {
//                    Toast.makeText(context, myList.get(position) + " clicked ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

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
        LinearLayout lyt_uploaded_doc;


        public ViewHolder(View convertview) {
            super(convertview);
            txt_docs_date = convertview.findViewById(R.id.txt_docs_date);
            txt_docs_title = convertview.findViewById(R.id.txt_docs_title);
            txt_docs_type = convertview.findViewById(R.id.txt_docs_type);
            img_uploaded_doc = convertview.findViewById(R.id.img_uploaded_doc);
            lyt_uploaded_doc = convertview.findViewById(R.id.lyt_uploaded_doc);
            crd_uploaded_doc = convertview.findViewById(R.id.crd_uploaded_doc);

        }
    }


}

