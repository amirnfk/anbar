package com.oshanak.mobilemarket.Activity.RowAdapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.DataStructure.Promotion_Info_Model;
import com.oshanak.mobilemarket.Activity.Service.RemoteApi;
import com.oshanak.mobilemarket.Activity.Service.RetrofitImageLoader;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Promotions_Adapter extends RecyclerView.Adapter<Promotions_Adapter.ViewHolder> implements Filterable {



    Context context;
    ProgressBar prg_promotion_list;
    int resource;
    private OnItemClickListener mOnItemClickListener;
    ArrayList<Promotion_Info_Model> myList;
    ArrayList<Promotion_Info_Model> myListAll;

    TextView txtpromotionCount;

    public interface OnItemClickListener {
        void onItemClick(View view, Promotion_Info_Model obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Promotions_Adapter(Context context,
                              int resource,
                              ArrayList<Promotion_Info_Model> myList,
                              ProgressBar prg_promotion_list,
                              TextView txt_promotion_count) {

        this.context = context;
        this.myList = myList;
        this.resource = resource;
        this.prg_promotion_list = prg_promotion_list;
        myListAll = new ArrayList<>(myList);
        this.txtpromotionCount = txt_promotion_count;

    }

    @SuppressLint("ResourceAsColor")

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotions_inner_layout_linear, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchTextraw = constraint.toString().toLowerCase();
            String searchText=convertPersianToLatinNumbers(searchTextraw);
            List<Promotion_Info_Model> tempList = new ArrayList<>();
//            List<Promotion_Info_Model> filteredList=new ArrayList<>();
            if (searchText.isEmpty() || searchText.length() == 0) {
                tempList.addAll(myListAll);
            } else {
                for (Promotion_Info_Model item : myListAll) {
                    if (item.getPromotion_Product_Name().toLowerCase().contains(searchText) || item.getPromotion_Product_Id().toLowerCase().contains(searchText)) {
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            myList.clear();
            myList.addAll((Collection<? extends Promotion_Info_Model>) results.values);
            txtpromotionCount.setText(((Collection<?>) results.values).size() + " کالای تخفیفی ");

            //if (constraint.length()>3 || constraint.length()==0 ){
            notifyDataSetChanged();
            prg_promotion_list.setVisibility(View.INVISIBLE);

        }


//        }
    };



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {




        holder.img_promotion_product.setImageResource(R.drawable.progress_animation);


                        try{
                            loadImageWithRetrofit(holder.img_promotion_product,myList.get(position).getPromotion_Product_Id());
                        }catch (Exception e){
                            holder.img_promotion_product.setImageResource(R.drawable.progress_animation);
                        }





        holder.txt_price_unit.setText("ریال");
        holder.txt_promotion_id.setText("کد کالا: " + myList.get(position).getPromotion_Product_Id());
        holder.txt_promotion_todate.setText("اعتبار از تاریخ: " + myList.get(position).getPromotion_Product_From_Date_S()+" تا تاریخ: "+ myList.get(position).getPromotion_Product_To_Date_S());
        holder.txt_promotion_name.setText(myList.get(position).getPromotion_Product_Name());
        holder.txt_promotion_price.setText(ThousandSeparatorWatcher.addSeparator(myList.get(position).getPromotion_Product_Price() + ""));
        holder.txt_promotion_off_price.setText(ThousandSeparatorWatcher.addSeparator(myList.get(position).getPromotion_Product_Off_Price() + ""));
//        holder.txt_promotion_price.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
//        holder.txt_promotion_off_price.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
//        holder.txt_price_unit.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        holder.txt_promotion_price.setPaintFlags(holder.txt_promotion_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txt_off_percent.setText(myList.get(position).getPromotion_Product_Discount() + " % تخفیف");

        holder.lyt_promotion_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, myList.get(position), position);
                }
            }
        });

    }

    private String convertPersianToLatinNumbers(String input) {
        String[] persianNumbers = {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        String[] latinNumbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (int i = 0; i < persianNumbers.length; i++) {
            input = input.replace(persianNumbers[i], latinNumbers[i]);
        }
        return input;
    }

    public void loadImageWithRetrofit(ImageView imageView, String _promotion_product_id) {

        final RetrofitImageLoader imageLoader = new RetrofitImageLoader(imageView);

        RemoteApi api = RemoteApi.Factory.create();




        api.getImage(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT,_promotion_product_id).enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ResponseBody body = response.body();
                if (response.isSuccessful() && body != null) {
                    imageView.setImageResource(R.drawable.progress_animation);

                    imageLoader.execute(body.byteStream());



                } else {
                    imageView.setImageResource(R.drawable.progress_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageView.setImageResource(R.drawable.progress_animation);
            }
        });
    }
    @Override
    public int getItemCount() {
        return myList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_promotion_product;
        TextView txt_promotion_name;
        TextView txt_promotion_id;
        TextView txt_promotion_todate;
        TextView txt_promotion_price;
        TextView txt_off_percent;
        TextView txt_price_unit;
        TextView txt_promotion_title;
        TextView txt_promotion_date;
        TextView txt_promotion_off_price;
        LinearLayout lyt_promotion_product;


        public ViewHolder(View convertview) {
            super(convertview);
            txt_promotion_name = convertview.findViewById(R.id.txt_promotion_name);
            txt_promotion_todate = convertview.findViewById(R.id.txt_promotion_todate);
            txt_promotion_id = convertview.findViewById(R.id.txt_promotion_id);
            img_promotion_product = convertview.findViewById(R.id.img_promotion_product);
            txt_promotion_price = convertview.findViewById(R.id.txt_promotion_price);
            txt_promotion_off_price = convertview.findViewById(R.id.txt_promotion_off_price);
            txt_off_percent = convertview.findViewById(R.id.txt_off_percent);
            txt_price_unit = convertview.findViewById(R.id.txt_price_unit);
            txt_promotion_title = convertview.findViewById(R.id.txt_promotion_title);
            txt_promotion_date = convertview.findViewById(R.id.txt_promotion_date);
            lyt_promotion_product = convertview.findViewById(R.id.lyt_promotion_product);

        }
    }


}

