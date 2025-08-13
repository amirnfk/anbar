package com.oshanak.mobilemarket.Activity.PickingApp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
import com.oshanak.mobilemarket.Activity.Enum.PickingItemStatus;
import com.oshanak.mobilemarket.Activity.Enum.ProductUnit;
import com.oshanak.mobilemarket.R;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class row_picking_item extends RecyclerView.Adapter<row_picking_item.ViewHolder> {
    private Context context;
    private ArrayList<PickingDeliverItemData> myList;
    private ArrayList<PickingDeliverItemData> filteredList; // Filtered list

    private OnPickingItemListCommandListener onPickingItemListCommandListener;
    public String orderType;
    public String editableRow = "0";
    public int minimumInventory = 0;

    public row_picking_item(Context context, ArrayList<PickingDeliverItemData> myList) {
        this.context = context;
        this.myList = myList;
        this.filteredList = new ArrayList<>(myList); // Initialize filtered list

        try {
            onPickingItemListCommandListener = (OnPickingItemListCommandListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPickingItemListCommandListener");
        }
    }
    public void filter(String _query) {
        String rawquery = new String(_query.getBytes(), StandardCharsets.UTF_8);
        String query=normalizeNumbers(rawquery);
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(myList);
        } else {
            String lowerCaseQuery = query.toLowerCase(); // Normalize query to lower case
            for (PickingDeliverItemData item : myList) {
                // Check for Persian characters
                if (item.getMATNR().toLowerCase().contains(lowerCaseQuery) ||
                        item.getARKTX().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    private String normalizeNumbers(String input) {
        return input.replace('۰', '0')
                .replace('۱', '1')
                .replace('۲', '2')
                .replace('۳', '3')
                .replace('۴', '4')
                .replace('۵', '5')
                .replace('۶', '6')
                .replace('۷', '7')
                .replace('۸', '8')
                .replace('۹', '9');
    }
//    public row_picking_item(Context context, ArrayList<PickingDeliverItemData> list, String orderType, String editableRow, int minimumInventory) {
//        this.context = context;
//        this.myList = list;
//        this.orderType = orderType;
//        this.editableRow = editableRow;
//        this.minimumInventory = minimumInventory;
//        try {
//            onPickingItemListCommandListener = (OnPickingItemListCommandListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnPickingItemListCommandListener");
//        }
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_picking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final PickingDeliverItemData data = myList.get(position);
        PickingDeliverItemData data = filteredList.get(position); // Use filtered data

        holder.tvRowCount.setText( (position + 1)+" . "+ data.getMATNR() + " - " + data.getARKTX());
        holder.tvPositionNo.setText("موقعيت: " + data.getLGPBE());

        // Additional item setup
        if (data.isIs_PC_Unit()) {
             holder.txt_is_pc_unit.setVisibility(View.VISIBLE);
            holder.txt_is_pc_unit.setText("صرفا مجاز به جمع آوری به صورت واحد جزء (pc)");
            holder.txt_is_pc_unit.setTextColor(Color.RED);
        }

        // Order and Picking Quantities
        holder.tvOrderQuantity2.setText("سفارش: "+Math.round(data.getLFIMG()) + " " + getUnitDescription(data.getVRKME())+getBQ(data.getVRKME(), data.getUMVKZ()) );
        holder.tvPickingQuantity2.setText("تحویل: "+data.getDeliverAmount() + " " + getUnitDescription(data.getDeliverUnit())+getBQ(data.getDeliverUnit(), data.getUMVKZ()) );


        // Inventory check
        int inventoryPcUnit = data.getLBKUM();
        double orderPcUnit = data.getLFIMG() * data.getUMVKZ();
        if (inventoryPcUnit >= orderPcUnit) {
            holder.tvInventory.setText("موجودی کافی");
            holder.tvInventory.setTextColor(Color.GRAY);
        } else {
            holder.tvInventory.setText("موجودی ناکافی");
            holder.tvInventory.setTextColor(Color.RED);
        }

        // Confirm Button Action
//        holder.bConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleConfirmClick(data, position);
//            }
//        });

        if (data.getStatusID() == PickingItemStatus.Reject.getCode()) {
            holder. bReject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reject, 0);
        } else {
            holder. bReject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reject_dis, 0);
        }
        holder. bReject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(minimumInventory>0){
                    Toast.makeText(context, "نمی توانید این سفارش را حذف کنید", Toast.LENGTH_SHORT).show();

                }else {
                    if (data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {

                        PickingItemStatus newStatus = PickingItemStatus.Unknown;
                        if (data.getStatusID() == PickingItemStatus.Unknown.getCode() || data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
//                    data.setStatusID(PickingItemStatus.Reject.getCode());
                            newStatus = PickingItemStatus.Reject;
                        } else if (data.getStatusID() == PickingItemStatus.Reject.getCode()) {
//                    data.setStatusID(PickingItemStatus.Unknown.getCode());
                            newStatus = PickingItemStatus.Unknown;
                        }
                        onPickingItemListCommandListener.OnPickingItemListCommand(data, newStatus, position, PickingItemListCommandType.Reject, minimumInventory,orderType);

                    } else {
                        Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        // Reject Button Action
//        holder.bReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleRejectClick(data, position);
//            }
//        });

        // Edit Button Action
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {
                    onPickingItemListCommandListener.OnPickingItemListCommand(data, null, position, PickingItemListCommandType.Edit, minimumInventory, orderType);
                } else {
                    Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show Inventory Action
        holder.tvInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickingItemListCommandListener.OnPickingItemListCommand(data, null, position, PickingItemListCommandType.ShowInventory, minimumInventory, orderType);
            }
        });

        if (data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
            holder.bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
        } else {
            holder. bConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark_dis, 0);
        }
        holder. bConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (data.getDeliverAmount() > 0) {

                    if (data.isIs_PC_Unit()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);

                        builder.setMessage("آیا کالا به صورت واحد جزء جمع آوری شده است");
                        builder.setPositiveButton("بله",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {

                                            PickingItemStatus newStatus = PickingItemStatus.Unknown;
                                            if (data.getStatusID() == PickingItemStatus.Unknown.getCode() || data.getStatusID() == PickingItemStatus.Reject.getCode()) {
                                                newStatus = PickingItemStatus.Confirm;
                                            } else if (data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
                                                newStatus = PickingItemStatus.Unknown;
                                            }
                                            onPickingItemListCommandListener.OnPickingItemListCommand(data, newStatus, position, PickingItemListCommandType.Confirm, minimumInventory, orderType);
                                        } else {
                                            Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        if (data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {

                            PickingItemStatus newStatus = PickingItemStatus.Unknown;
                            if (data.getStatusID() == PickingItemStatus.Unknown.getCode() || data.getStatusID() == PickingItemStatus.Reject.getCode()) {
//                    data.setStatusID(PickingItemStatus.Confirm.getCode());
                                newStatus = PickingItemStatus.Confirm;
                            } else if (data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
//                    data.setStatusID(PickingItemStatus.Unknown.getCode());
                                newStatus = PickingItemStatus.Unknown;
                            }
                            onPickingItemListCommandListener.OnPickingItemListCommand(data, newStatus, position, PickingItemListCommandType.Confirm, minimumInventory, orderType);
                        } else {
                            Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Utility.showErrorDialog(context, "تعداد تحویل نمی تواند صفر باشد. از قسمت ویرایش تعداد تحویل سفارش را اصلاح نمایید.");
                }
            }
        });
        //endregion bConfirm


//        Utility.setFont(context, bEdit);
//        Utility.increaseTextSize(bEdit, buttonDecTextSize);

//        holder.bEdit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(data.getMATNR().equals(Integer.parseInt(editableRow)+"") || editableRow.equals("0")) {
//                    onPickingItemListCommandListener.OnPickingItemListCommand(data, null, position, PickingItemListCommandType.Edit,minimumInventory,orderType);
//
//                }else {
//                    Toast.makeText(context, "ابتدا سفارش "+Integer.parseInt(editableRow)+ " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
    }

//    private void handleConfirmClick(PickingDeliverItemData data, int position) {
//        if (minimumInventory > 0) {
//            Utility.showErrorDialog(context, "تعداد تحویل نمی تواند صفر باشد. از قسمت ویرایش تعداد تحویل سفارش را اصلاح نمایید.");
//        } else {
//            if (data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {
//                PickingItemStatus newStatus = PickingItemStatus.Unknown;
//                if (data.getStatusID() == PickingItemStatus.Unknown.getCode() || data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
//                    newStatus = PickingItemStatus.Confirm;
//                } else {
//                    newStatus = PickingItemStatus.Unknown;
//                }
//                onPickingItemListCommandListener.OnPickingItemListCommand(data, newStatus, position, PickingItemListCommandType.Confirm, minimumInventory, orderType);
//            } else {
//                Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    private void handleRejectClick(PickingDeliverItemData data, int position) {
//        if (minimumInventory > 0) {
//            Toast.makeText(context, "نمی توانید این سفارش را حذف کنید", Toast.LENGTH_SHORT).show();
//        } else {
//            if (data.getMATNR().equals(Integer.parseInt(editableRow) + "") || editableRow.equals("0")) {
//                PickingItemStatus newStatus = PickingItemStatus.Unknown;
//                if (data.getStatusID() == PickingItemStatus.Unknown.getCode() || data.getStatusID() == PickingItemStatus.Confirm.getCode()) {
//                    newStatus = PickingItemStatus.Reject;
//                } else if (data.getStatusID() == PickingItemStatus.Reject.getCode()) {
//                    newStatus = PickingItemStatus.Unknown;
//                }
//                onPickingItemListCommandListener.OnPickingItemListCommand(data, newStatus, position, PickingItemListCommandType.Reject, minimumInventory, orderType);
//            } else {
//                Toast.makeText(context, "ابتدا سفارش " + Integer.parseInt(editableRow) + " را تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Update to return the filtered list size

    }

    public void setSelectedPosition(int position) {
//        selectedPos = position;
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRowCount,  tvPositionNo, txt_is_pc_unit, tvOrderQuantity2,   tvPickingQuantity2,   tvInventory;
        public Button bConfirm, bReject, bEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRowCount = itemView.findViewById(R.id.tvRowCount);
            tvPositionNo = itemView.findViewById(R.id.tvPositionNo);
            txt_is_pc_unit = itemView.findViewById(R.id.txt_is_pc_unit);
            tvOrderQuantity2 = itemView.findViewById(R.id.tvOrderQuantity2);
            tvPickingQuantity2 = itemView.findViewById(R.id.tvPickingQuantity2);
            tvInventory = itemView.findViewById(R.id.tvInventory);
            bConfirm = itemView.findViewById(R.id.bConfirm);
            bReject = itemView.findViewById(R.id.bReject);
            bEdit = itemView.findViewById(R.id.bEdit);
         }
    }

    public interface OnPickingItemListCommandListener {
        void OnPickingItemListCommand(PickingDeliverItemData pickingDeliverItemData, PickingItemStatus newStatus, int position, PickingItemListCommandType commandType, int minimumInventory, String orderType);
    }

    public enum PickingItemListCommandType {
        Unknown, Edit, Confirm, Reject, ShowInventory
    }

    private String getUnitDescription(String unitName) {
        if (unitName.equals(ProductUnit.ST.getName())) {
            return ProductUnit.ST.getShortDesc();
        } else if (unitName.equals(ProductUnit.KAR.getName())) {
            return ProductUnit.KAR.getShortDesc()/* + "(" + BQ + ")"*/;
        } else if (unitName.equals(ProductUnit.G.getName())) {
            return ProductUnit.G.getShortDesc();
        } else if (unitName.equals(ProductUnit.KG.getName())) {
            return ProductUnit.KG.getName()/* + "(" + BQ + ")"*/;
        }
        return "";
    }

    private String getBQ(String unitName, int BQ) {
        if (unitName.equals(ProductUnit.ST.getName())) {
            return "";
        } else if (unitName.equals(ProductUnit.KAR.getName())) {
            return "( " + BQ + " عددی )";
        } else if (unitName.equals(ProductUnit.G.getName())) {
            return "";
        } else if (unitName.equals(ProductUnit.KG.getName())) {
            return "(" + BQ + " عددی )";
        }
        return "";
    }
}






