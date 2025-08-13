package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.appintro.SlideBackgroundColorHolder;
import com.github.appintro.SlidePolicy;
import com.oshanak.mobilemarket.R;

public class CustomSlidePolicyFragment extends Fragment implements SlidePolicy, SlideBackgroundColorHolder {

    private CheckBox checkBox;
    private int backgroundColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_slide_policy, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBox = view.findViewById(R.id.check_box);
    }

    @Override
    public boolean isPolicyRespected() {
        return checkBox.isChecked();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(requireContext(), R.string.please_select_the_checkbox_before_proceeding, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getDefaultBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public static CustomSlidePolicyFragment newInstance() {
        return new CustomSlidePolicyFragment();
    }

    @Override
    public int getDefaultBackgroundColorRes() {
        return 0;
    }
}
