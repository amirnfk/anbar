package com.oshanak.mobilemarket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity;
import com.oshanak.mobilemarket.R;

public class AppInteroCustomFragment extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private static final String ARG_RES_ID = "ResId";
    private static final String ARG_BACKGROUND_COLOR = "backgroundColor";
    private static final String ARG_HEADER_TEXT = "headerText";
    private static final String ARG_DESCRIPTION_TEXT = "descriptionText";
    private static final String ARG_IMAGE_RES_ID = "imageResId";
    private static final String ARG_IMAGE_Confirm = "imageConfirm";

    private int layoutResId;
    private String ResId="";
    private int backgroundColor;
    private String headerText;
    private String descriptionText;
    private int imageResId;
private boolean imageConfirm;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            ResId = getArguments().getString(ARG_RES_ID);

            backgroundColor = getArguments().getInt(ARG_BACKGROUND_COLOR);
            headerText = getArguments().getString(ARG_HEADER_TEXT);
            descriptionText = getArguments().getString(ARG_DESCRIPTION_TEXT);
            imageResId = getArguments().getInt(ARG_IMAGE_RES_ID);
            imageConfirm=getArguments().getBoolean(ARG_IMAGE_Confirm);

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutResId, container, false);
        rootView.setBackgroundColor(backgroundColor);

        TextView headerTextView = rootView.findViewById(R.id.textViewHeader);
        TextView descriptionTextView = rootView.findViewById(R.id.textViewDescription);
        TextView descriptionTextViewofurl = rootView.findViewById(R.id.textView3);
        ImageView imageView = rootView.findViewById(R.id.imageView_slider);
        descriptionTextViewofurl.setText(ResId);

        Glide.with(getContext())
                .load(descriptionTextViewofurl.getText().toString())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.logo_haft_vector)
                .centerInside()
                .into(imageView);

        ImageView imageViewConfirm = rootView.findViewById(R.id.img_confirm);

        headerTextView.setText(headerText);
        descriptionTextView.setText(descriptionText);
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), ImageEnlargedActivity.class);

        intent.putExtra("extra_image_url",descriptionTextViewofurl.getText().toString());
        startActivity(intent);
    }
});


if (imageConfirm){

    imageViewConfirm.setImageResource(R.drawable.checkmark);
}else{

    imageViewConfirm.setImageResource(R.drawable.reject);
}







        return rootView;
    }

    public static AppInteroCustomFragment newInstance(String  ResId,int layoutResId, int backgroundColor,
                                                           String headerText, String descriptionText,
                                                           int imageResId,boolean imageConfirm) {
        AppInteroCustomFragment fragment = new AppInteroCustomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RES_ID,  ResId);
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        args.putInt(ARG_BACKGROUND_COLOR, backgroundColor);
        args.putString(ARG_HEADER_TEXT, headerText);
        args.putString(ARG_DESCRIPTION_TEXT, descriptionText);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        args.putBoolean(ARG_IMAGE_Confirm, imageConfirm);
        fragment.setArguments(args);
        return fragment;
    }
}
