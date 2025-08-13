package com.oshanak.mobilemarket.Activity.Common;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.oshanak.mobilemarket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumericUpDownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumericUpDownFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etFrgAmount;
    private ImageButton bFrgMinus;
    private ImageButton bFrgPlus;
//    private EditText etFrgBarcode;
//    private OnConfirmListener onConfirmListener;
//    private OnBarcodeScannedByCameraListener onBarcodeScannedByCameraListener;
    private OnValueChanged onValueChanged;

    public NumericUpDownFragment() {
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
    public static NumericUpDownFragment newInstance(String param1, String param2) {
        NumericUpDownFragment fragment = new NumericUpDownFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.numeric_up_down_fragment, container, false);
        etFrgAmount = view.findViewById(R.id.etFrgAmount);
        ImageButton bFrgMinus = view.findViewById(R.id.bFrgMinus);
        ImageButton bFrgPlus = view.findViewById(R.id.bFrgPlus);

        etFrgAmount.addTextChangedListener(new ThousandSeparatorWatcher(etFrgAmount));

        etFrgAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                onValueChanged.OnValueChanged( ThousandSeparatorWatcher.removeSeparator( charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        bConfirm.setOnClickListener(view1 -> onConfirmListener.OnConfirm(etFrgBarcode.getText().toString().trim()));
//        bCamera.setOnClickListener(view12 -> {
//            new IntentIntegrator(getActivity()).initiateScan();
//        });
        bFrgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangeCount(view, true);
            }
        });
        bFrgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangeCount(view, false);
            }
        });

        return view;
    }
//    boolean isStarted = false;
//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        if(!isStarted)
//        {
//            isStarted = true;
//            Utility.increaseTextSize( etFrgAmount,50);
//        }
//    }

    private void onClickChangeCount(View view, boolean decrease)
    {
        double d = (etFrgAmount.getText().toString().trim().equals("") ? 0 :
                Double.parseDouble( ThousandSeparatorWatcher.removeSeparator(etFrgAmount.getText().toString().trim())));

        int count = (int)d;
        if(decrease)
        {
            if(count <= 0)
            {
                etFrgAmount.setText("0");
                etFrgAmount.clearFocus();
                Utility.hideKeyboard(getContext());
                return;
            }
            count--;
        }
        else
        {
            if(count >= 999999) return;
            count++;
        }
        etFrgAmount.setText(String.valueOf(count));
        etFrgAmount.clearFocus();
        Utility.hideKeyboard(getContext());
    }

    public interface OnValueChanged
    {
        void OnValueChanged(String value);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try
        {
            onValueChanged = (OnValueChanged) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnValueChanged");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onValueChanged = null;
    }
    public void setValue(String value)
    {
        etFrgAmount.setText(value);
    }
    public String getValue()
    {
        String sVal = etFrgAmount.getText().toString().trim();
        return  ThousandSeparatorWatcher.removeSeparator(sVal);
    }
    public boolean isEmpty()
    {
        String sVal = etFrgAmount.getText().toString().trim();
        return (sVal.equals(""));
    }
}