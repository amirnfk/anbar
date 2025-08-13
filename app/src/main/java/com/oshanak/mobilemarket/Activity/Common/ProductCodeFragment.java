package com.oshanak.mobilemarket.Activity.Common;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.oshanak.mobilemarket.Activity.Activity.SearchProductByNameActivity;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.LocalDB.DBHandler;
import com.oshanak.mobilemarket.Activity.LocalDB.Param;
import com.oshanak.mobilemarket.R;
import android.content.pm.PackageManager;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCodeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etFrgBarcode;
    private OnConfirmListener onConfirmListener;
    private OnBarcodeScannedByCameraListener onBarcodeScannedByCameraListener;
    private OnBarcodeChanged onBarcodeChanged;
    private boolean autoScanVisible = false;
    private ImageButton bAutoScan;

    public ProductCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductCodeFragment newInstance(String param1, String param2) {
        ProductCodeFragment fragment = new ProductCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        Utility.setFontBold(etFrgBarcode);
//        Utility.increaseTextSize(etFrgBarcode, 20);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_code, container, false);
        etFrgBarcode = view.findViewById(R.id.etFrgBarcode);
        ImageButton bConfirm = view.findViewById(R.id.bConfirm);
        ImageButton bCamera = view.findViewById(R.id.bCamera);
        ImageButton bSearchByName = view.findViewById(R.id.bSearcByName);
        ImageButton bDelete = view.findViewById(R.id.bDelete);
        bAutoScan = view.findViewById(R.id.bAutoScan);

        etFrgBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(getContext(),charSequence,Toast.LENGTH_SHORT).show();
                onBarcodeChanged.OnBarcodeChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(getContext(),editable.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT))
                {
                    onConfirmListener.OnConfirm(etFrgBarcode.getText().toString().trim());
                }
                return false;
            }
        };
        etFrgBarcode.setOnEditorActionListener(editorActionListener);

        bConfirm.setOnClickListener(view1 -> onConfirmListener.OnConfirm(etFrgBarcode.getText().toString().trim()));
        bCamera.setOnClickListener(view1 -> {

//            if(!isCamera_api33_permissited()){
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
//                dlgAlert.setMessage("لطفا اجازه استفاده از دوربین گوشی را به اپلیکیشن فروشگاهی اعطا کنید");
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton("اوکی",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                openAppDetailsSettings();
//                            }
//                        });
//
//
//                dlgAlert.setIcon(R.drawable.question128);
//                dlgAlert.create().show();
//
//
//            }


            startScan();
        });
        bSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSeachByName();
            }
        });
        bDelete.setOnClickListener(v -> {
            etFrgBarcode.setText("");
            etFrgBarcode.requestFocus();
            Utility.showKeyboard(getContext());
        });


        bAutoScan.setVisibility(isAutoScanVisible() ? View.VISIBLE : View.GONE);
        bAutoScan.setImageResource(isAutoScanEnable() ? R.drawable.autoon: R.drawable.autooff);
        bAutoScan.setOnClickListener(v -> {

            setAutoScanEnable(!isAutoScanEnable());
        });
        return view;
    }

    private void openAppDetailsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private boolean isCamera_api33_permissited() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 97);
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    public void startScan()
    {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setPrompt("باركد كالا را دقيقاً داخل كادر قرار دهيد");
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setBeepEnabled(false);

//            integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != GlobalData.getSearchByNameActivityRequestCode()) return;


        if (requestCode == GlobalData.getSearchByNameActivityRequestCode()) {
            if (resultCode == RESULT_OK) {

                String scanContent = data.getStringExtra("searchByNameKey");
                etFrgBarcode.setText(scanContent);
                // The result was successful
                // You can retrieve data from the Intent using data.getStringExtra(), etc.
            } else if (resultCode == RESULT_CANCELED) {
                // The result was canceled by the user or some other condition
            }
        }
    }
    public void startSeachByName()
    {
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
//        integrator.setPrompt("برای جستجو حداقل سه کاراکتر وارد نمایید");
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//        integrator.setBeepEnabled(false);
////            integrator.setOrientationLocked(true);
//        integrator.initiateScan();
Intent intent=new Intent(getContext(), SearchProductByNameActivity.class);
startActivityForResult(intent,GlobalData.getSearchByNameActivityRequestCode());
    }
    public interface OnBarcodeScannedByCameraListener
    {
        void OnBarcodeScannedByCamera(String barcode);
    }
    public void setResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode != GlobalData.getBarcodeActivityRequestCode()) return;
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult == null || scanningResult.getContents() == null)
        {
            etFrgBarcode.setText("");
            Toast.makeText(getActivity(), "بارکد کالا به درستی اسکن نشد.", Toast.LENGTH_SHORT).show();

        } else
        {
            String scanContent = scanningResult.getContents();
            etFrgBarcode.setText(scanContent);
            if(!scanContent.equals("")) {
                onBarcodeScannedByCameraListener.OnBarcodeScannedByCamera(scanContent);
            }
        }
    }
    public interface OnConfirmListener
    {
        void OnConfirm(String barcode);
    }
    public interface OnBarcodeChanged
    {
        void OnBarcodeChanged(String barcode);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try
        {
            onConfirmListener = (OnConfirmListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onConfirmListener");
        }
        try
        {
            onBarcodeScannedByCameraListener = (OnBarcodeScannedByCameraListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onBarcodeScannedByCameraListener");
        }
        try
        {
            onBarcodeChanged = (OnBarcodeChanged) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onBarcodeChanged");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onConfirmListener = null;
    }
    public void setProductCode(String code)
    {
        etFrgBarcode.setText(code);
    }
    public String getProductCode()
    {
        return etFrgBarcode.getText().toString().trim();
    }
    public void setFont()
    {
        Utility.setFontBold(etFrgBarcode);
        Utility.increaseTextSize(etFrgBarcode, 40);
    }

    public boolean isAutoScanVisible() {
        return autoScanVisible;
    }

    public void setAutoScanVisible(boolean autoScanVisible) {
        this.autoScanVisible = autoScanVisible;
        bAutoScan.setVisibility(autoScanVisible ? View.VISIBLE : View.GONE);
    }

    public boolean isAutoScanEnable() {
        DBHandler dbHandler = new DBHandler(getContext());
        String autoScanEnable = dbHandler.getParamValue(Param.AutoScanEnable);
        return autoScanEnable.equals("1");
    }

    private void setAutoScanEnable(boolean autoScanEnable) {
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.setParamValue(Param.AutoScanEnable, autoScanEnable ? "1" : "0");
        bAutoScan.setImageResource(autoScanEnable ? R.drawable.autoon: R.drawable.autooff);
    }
}