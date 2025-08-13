package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditDeliverItemActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverItemData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.DeliverOrderService;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class EditDeliverItemActivity extends BaseActivity implements OnTaskCompleted
{
    private EditText etDeliverQuantity;
    private EditText etComment;
    private TextView tvName;
    private DeliverItemData deliverItemData;
    private EditDeliverItemActivityMode editDeliverItemActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deliver_item);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        etDeliverQuantity = findViewById(R.id.etDeliverQuantity);
        etComment = findViewById(R.id.etComment);
        tvName = findViewById(R.id.tvName);

        etDeliverQuantity.addTextChangedListener(new ThousandSeparatorWatcher(etDeliverQuantity));

        Intent intent = getIntent();
        deliverItemData = (DeliverItemData)intent.getSerializableExtra("deliverItemData");

        etDeliverQuantity.setText(ThousandSeparatorWatcher.addSeparator(deliverItemData.getDeliverQuantity()));
        etComment.setText(deliverItemData.getComment());
        tvName.setText(deliverItemData.getItemName());
    }
    public void onClickExit(View view)
    {
        onBackPressed();
    }
    public void onClickConfirm(View view)
    {
        if (!validateData()) return;
        Update();
    }
    private void Update()
    {
        double deliverQuantity = Double.valueOf( ThousandSeparatorWatcher.removeSeparator( etDeliverQuantity.getText().toString()));

        DeliverOrderService service;

        editDeliverItemActivityMode = EditDeliverItemActivityMode.BeforeUpdate;
        service = new DeliverOrderService(DeliverOrderServiceMode.UpdateDeliverItem, this);
        service.deliverItemData.setOrderId(deliverItemData.getOrderId());
        service.deliverItemData.setItemId(deliverItemData.getItemId());
        service.deliverItemData.setDeliverQuantity( deliverQuantity);
        service.deliverItemData.setComment(etComment.getText().toString().trim());

        service.listener = this;
        service.execute();
        startWait();

    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(editDeliverItemActivityMode == EditDeliverItemActivityMode.BeforeUpdate)
        {
            if(!taskResult.isSuccessful)
            {
                if (Utility.generalErrorOccurred(taskResult, this))
                {
                    return;
                }
                if(taskResult.isExceptionOccured("order status not valid to change."))
                {
                    Utility.simpleAlert(this, "با توجه به وضعيت سفارش امكان اصلاح اقلام آن وجود ندارد.",""
                            , DialogIcon.Warning);
                    return;
                }
                else
                {
                    Utility.simpleAlert(this, getString(R.string.update_do_not), DialogIcon.Error);
                    return;
                }
            }
            Toast.makeText(this, getString( R.string.update_done),Toast.LENGTH_SHORT).show();
            editDeliverItemActivityMode = EditDeliverItemActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }
    private boolean validateData()
    {
        if (Utility.editTextIsEmpty(etDeliverQuantity, "تعداد كالا تحويلي به مشتري را وارد نماييد")) return false;
        return true;
    }
    public void onClickChangeCount(View view)
    {
        double d = (etDeliverQuantity.getText().toString().trim().equals("") ? 1 :
                Double.parseDouble( ThousandSeparatorWatcher.removeSeparator(etDeliverQuantity.getText().toString().trim())));

        int count = (int)d;
        if(view.getId() == findViewById(R.id.ibMinus).getId())
        {
            if(count <= 0) return;
            count--;
        }
        else
        {
            if(count >= 999999) return;
            count++;
        }
        etDeliverQuantity.setText(String.valueOf(count));
        etDeliverQuantity.clearFocus();
        Utility.hideKeyboard(this);
    }
    @Override
    public void onBackPressed()
    {
        if(editDeliverItemActivityMode == EditDeliverItemActivityMode.AfterUpdate )
        {
            double deliverQuantity = Double.valueOf( ThousandSeparatorWatcher.removeSeparator( etDeliverQuantity.getText().toString()));

            Intent returnIntent = new Intent();
            returnIntent.putExtra("deliverQuantity", deliverQuantity);
            returnIntent.putExtra("comment", etComment.getText().toString().trim());
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
