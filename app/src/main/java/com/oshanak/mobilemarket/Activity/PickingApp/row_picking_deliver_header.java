package com.oshanak.mobilemarket.Activity.PickingApp;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.Enum.PickingOrderStatus;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

public class row_picking_deliver_header extends RecyclerView.Adapter<row_picking_deliver_header.ViewHolder> {
    private ArrayList<PickingDeliverHeaderData> dataList;
    private int selectedPos = -1;
    private OnPickingOrderListCommandListener onPickingOrderListCommandListener;

    public row_picking_deliver_header( OnPickingOrderListCommandListener listener,ArrayList<PickingDeliverHeaderData> list) {
        this.dataList = list;
        this.onPickingOrderListCommandListener = listener;
    }

    public void setSelection(int position) {
        if (selectedPos == position) {
            // Selected position would be cleared. Uncomment next line to enable this feature.
            // selectedPos = -1;
        } else {
            selectedPos = position;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_picking_deliver_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickingDeliverHeaderData data = dataList.get(position);

        holder.tvDocNo.setText(position + 1 + ". " + "شماره: " + data.getVBELN());
        holder.tvDate.setText(data.getBLDAT());
//        holder.tvStoreId.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey ));

        if (data.getOrderType().equalsIgnoreCase("S")) {
            holder.tvStoreId.setText("* سفارش زنجیره سرد *");
            holder.rootLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_blue1));
        } else {
            holder.tvStoreId.setText("فروشگاه: " + " (" + data.getKUNNR() + ") " + data.getPLANT_NAME());
        }

        if (data.getStatusID() == PickingOrderStatus.Ready.getCode()) {
            holder.tvStatusName.setTextColor(Color.GRAY);
        } else if (data.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
            holder.tvStatusName.setTextColor(Color.BLACK);
        } else if (data.getStatusID() == PickingOrderStatus.PickedUp.getCode()) {
            holder.tvStatusName.setTextColor(Color.BLUE);
        } else {
            holder.tvStatusName.setTextColor(Color.GRAY);
        }

        holder.tvStatusName.setText(data.getStatusName() + " (لاین: " + data.getLine() + ")");

        holder.bItems.setOnClickListener(v -> {
            onPickingOrderListCommandListener.OnPickingOrderListCommand(data, position, PickingOrderListCommandType.Items);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocNo;
        TextView tvDate;
        TextView tvStoreId;
        TextView tvStatusName;
        View rootLayout;
        CardView bItems;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDocNo = itemView.findViewById(R.id.tvDocNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStoreId = itemView.findViewById(R.id.tvStoreId);
            tvStatusName = itemView.findViewById(R.id.tvStatusName);
            rootLayout = itemView.findViewById(R.id.lyt_root);
            bItems = itemView.findViewById(R.id.bItems);
        }
    }

    public interface OnPickingOrderListCommandListener {
        void OnPickingOrderListCommand(PickingDeliverHeaderData pickingDeliverHeaderData, int position, PickingOrderListCommandType commandType);
    }

    public enum PickingOrderListCommandType {
        Unknown, Items
    }
}


