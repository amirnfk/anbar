package com.oshanak.mobilemarket.Activity.Common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.oshanak.mobilemarket.Activity.Enum.Font;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OnDateChangedListener onDateChangedListener;
    private PersianCalendar _persianCalendar = new PersianCalendar();
    public boolean allowChangeDate = true;

    public DateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateFragment newInstance(String param1, String param2) {
        DateFragment fragment = new DateFragment();
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

    ImageView button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_date, container, false);

        View view = inflater.inflate(R.layout.fragment_date, container, false);
        button = view.findViewById(R.id.bDate);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker();
            }
        });
        TextView tvDate = view.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker();
            }
        });
        return view;
    }

    private void showDatePicker()
    {
        if(!allowChangeDate) return;

        PersianCalendar initDate = new PersianCalendar();
        try {
            BeanUtils.copyProperties(initDate, _persianCalendar);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        PersianDatePickerDialog picker = new PersianDatePickerDialog(getContext())
                .setPositiveButtonString("قبول")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1395)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setInitDate(initDate)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(Utility.getTypeFace(getContext(), Font.SansIran))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar)
                    {
                        DateFragment.this._persianCalendar = persianCalendar;
                        TextView tvDate = getView().findViewById(R.id.tvDate);
                        tvDate.setText(_persianCalendar.getPersianLongDate());

                        onDateChangedListener.OnDateChanged(_persianCalendar.getPersianLongDate(), Utility.getPersianShortDate(_persianCalendar));
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
        picker.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            onDateChangedListener = (OnDateChangedListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement OnDateChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
//        {
//            String PersianShortDate =  data.getSerializableExtra("PersianShortDate").toString();
//            String PersianLongDate =  data.getSerializableExtra("PersianLongDate").toString();
//            TextView tvDate = getView().findViewById(R.id.tvDate);
//            tvDate.setText(PersianLongDate);
//            tvDate.setTag(PersianShortDate);
//
//            onDateChangedListener.OnDateChanged(PersianLongDate, PersianShortDate);
//        }
    }
    public interface OnDateChangedListener
    {
        void OnDateChanged(String PersianLongDate, String PersianShortDate);
    }
    public void setDate(PersianCalendar pc)
    {
        TextView tvDate = getView().findViewById(R.id.tvDate);
        _persianCalendar = pc;
        tvDate.setText(pc.getPersianLongDate());
//        tvDate.setTag(Utility.getPersianShortDate(pc));
    }
    public void setDateToCurrent()
    {
        setDate(new PersianCalendar());
    }
    public String getLongDate()
    {
        return _persianCalendar.getPersianLongDate();
    }
    public String getShortDate()
    {
        return ( Utility.getPersianShortDate(_persianCalendar));
    }

    public void setEnabled(boolean enable)
    {
        button.setEnabled(enable);
        if(enable)
        {
            button.setImageResource(R.drawable.date128);
        }
        else
        {
            button.setImageResource(R.drawable.date_disable128);
        }
    }
    public void setTitle(String title)
    {
        TextView tvDateTitle = getView().findViewById(R.id.tvDateTitle);
        tvDateTitle.setText(title);
    }
    public void setTitleWeight(int percent)
    {
        TextView tvDateTitle = getView().findViewById(R.id.tvDateTitle);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateTitle.getLayoutParams();
        params.weight = percent;
        tvDateTitle.setLayoutParams(params);
    }
    public void setValueWeight(int percent)
    {
//        TextView tvDate = getView().findViewById(R.id.tvDate);
        LinearLayout lDate = getView().findViewById(R.id.lDate);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lDate.getLayoutParams();
        params.weight = percent;
        lDate.setLayoutParams(params);
    }
    public void setTitleWidth_WRAP_CONTENT()
    {
        TextView tvDateTitle = getView().findViewById(R.id.tvDateTitle);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateTitle.getLayoutParams();
        params.width= ViewGroup.LayoutParams.WRAP_CONTENT ;
        params.weight = 0;
        tvDateTitle.setLayoutParams(params);
    }
    public void hideTitle()
    {
        getView().findViewById(R.id.tvDateTitle).setVisibility(View.GONE);
    }
}
