package com.oshanak.mobilemarket.Activity.Activity;


import static com.oshanak.mobilemarket.Activity.Activity.ImageEnlargedActivity.EXTRA_IMAGE_URL;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Models.DeleteDocRequestModel;
import com.oshanak.mobilemarket.Activity.Models.DeleteDocResult;
import com.oshanak.mobilemarket.Activity.Models.DocResponse;
import com.oshanak.mobilemarket.Activity.Models.DocsTypeModel_Id_name;
import com.oshanak.mobilemarket.Activity.Models.UpdateDocModel;
import com.oshanak.mobilemarket.Activity.Models.UploadImageResponse;
import com.oshanak.mobilemarket.Activity.Models.metaData;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.DocsTypeModel;
import com.oshanak.mobilemarket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Document_Detail_Activity extends AppCompatActivity {
    int ImageID1=0;
    int ImageID2=0;
    int ImageID3=0;
    int ImageID4=0;
    int ImageID5=0;
    private Uri imageUri1;
    private Uri imageUri2;
    private Uri imageUri3;
    private Uri imageUri4;
    private Uri imageUri5;
    private static final int REQUEST_CAMERA_PERMISSION = 158;
    int Doc_header_ID=0;
    String docTitle;
    int docType;
    String docDesc;
    String docDate;

    String docUrl1;
    String docUrl2;
    String docUrl3;
    String docUrl4;
    String docUrl5;
    Spinner spinner;
    private final int maxRatio = 900;
    EditText txt_doc_desc;
    EditText txt_docs_title;
    TextView txtLastUpdated_date;
    ImageView imgNewDoc_1;
    ImageView imgNewDoc_2;
    ImageView imgNewDoc_3;
    ImageView imgNewDoc_4;
    ImageView imgNewDoc_5;
    ImageView imgTick_1;
    ImageView imgTick_2;
    ImageView imgTick_3;
    ImageView imgTick_4;
    ImageView imgTick_5;
    TextView txt_img_1;
    TextView txt_img_2;
    TextView txt_img_3;
    TextView txt_img_4;
    TextView txt_img_5;
    CardView crd1;
    CardView crd2;
    CardView crd3;
    CardView crd4;
    CardView crd5;
    Button btnNewDocSubmit;
    ProgressBar prg_new_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);
        setUp();
        docTitle = getIntent().getExtras().getString("doc_title", "عنوان سند ... ");
        Doc_header_ID = getIntent().getExtras().getInt("doc_id", 0);
        docType = getIntent().getExtras().getInt("doc_type_code", 0);
        docDesc = getIntent().getExtras().getString("doc_desc", "شرح سند ");
        docDate = getIntent().getExtras().getString("doc_date", "...");
        ImageID1 = getIntent().getExtras().getInt("doc_url_1_id", 0);
        ImageID2 = getIntent().getExtras().getInt("doc_url_2_id", 0);
        ImageID3 = getIntent().getExtras().getInt("doc_url_3_id", 0);
        ImageID4 = getIntent().getExtras().getInt("doc_url_4_id", 0);
        ImageID5 = getIntent().getExtras().getInt("doc_url_5_id", 0);
        docUrl1 = getIntent().getExtras().getString("doc_url_1", "");
        docUrl2 = getIntent().getExtras().getString("doc_url_2", "");
        docUrl3 = getIntent().getExtras().getString("doc_url_3", "");
        docUrl4 = getIntent().getExtras().getString("doc_url_4", "");
        docUrl5 = getIntent().getExtras().getString("doc_url_5", "");



        getDocsYype(new metaData(GlobalData.getUserName().toString(), GlobalData.getAppVersionCode(),
                "null", ApplicationMode.StoreHandheld.name(), Utility.getDeviceInfo(), GlobalData.getStoreID()));

        txt_docs_title.setText(docTitle);
        txtLastUpdated_date.setText(docDate + " : " + " تاریخ ثبت ");
        txt_doc_desc.setText(docDesc);

        if (!docUrl1.equals("")) {
            try {
                    crd1.setVisibility(View.VISIBLE);
                    crd2.setVisibility(View.VISIBLE);
                    imgTick_1.setVisibility(View.VISIBLE);

                    txt_img_1.setBackgroundColor(getResources().getColor(R.color.dribble_red));
                    txt_img_1.setText("حذف تصویر");
                    txt_img_1.setTag("delete");
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.receipt_search);
                Glide.with(Document_Detail_Activity.this).load(docUrl1).apply(options).into(imgNewDoc_1);
                imgNewDoc_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Document_Detail_Activity.this, ImageEnlargedActivity.class);
                        intent.putExtra(EXTRA_IMAGE_URL, docUrl1);
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {

            }

        }else {
            crd1.setVisibility(View.VISIBLE);
            imgTick_1.setVisibility(View.GONE);
        }

        if (!docUrl2.equals("")) {
            crd2.setVisibility(View.VISIBLE);
            crd3.setVisibility(View.VISIBLE);
            imgTick_3.setVisibility(View.VISIBLE);

            txt_img_2.setBackgroundColor(getResources().getColor(R.color.dribble_red));
            txt_img_2.setText("حذف تصویر");
            txt_img_2.setTag("delete");
            try {
//            picasso.get()
//                    .load(docUrl2)
//                    .placeholder(R.drawable.progress_animation)
//                    .error(R.drawable.receipt_search)
//                    .into(imgNewDoc_2);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation);

                Glide.with(Document_Detail_Activity.this).load(docUrl2).apply(options).into(imgNewDoc_2);

                imgNewDoc_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Document_Detail_Activity.this, ImageEnlargedActivity.class);
                        intent.putExtra(EXTRA_IMAGE_URL, docUrl2);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {

            }
        }else {
            imgTick_2.setVisibility(View.GONE);
        }
        if (!docUrl3.equals("")) {
            crd3.setVisibility(View.VISIBLE);
            crd4.setVisibility(View.VISIBLE);
            imgTick_3.setVisibility(View.VISIBLE);

            txt_img_3.setBackgroundColor(getResources().getColor(R.color.dribble_red));
            txt_img_3.setText("حذف تصویر");
            txt_img_3.setTag("delete");
            try {
//            picasso.get()
//                    .load(docUrl3)
//                    .placeholder(R.drawable.progress_animation)
//                    .error(R.drawable.receipt_search)
//                    .into(imgNewDoc_3);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation);

                Glide.with(Document_Detail_Activity.this).load(docUrl3).apply(options).into(imgNewDoc_3);
                imgNewDoc_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Document_Detail_Activity.this, ImageEnlargedActivity.class);
                        intent.putExtra(EXTRA_IMAGE_URL, docUrl3);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {

            }
        }else {
            imgTick_3.setVisibility(View.GONE);
        }
        if (!docUrl4.equals("")) {
            crd4.setVisibility(View.VISIBLE);
            crd5.setVisibility(View.VISIBLE);
            imgTick_4.setVisibility(View.VISIBLE);

            txt_img_4.setBackgroundColor(getResources().getColor(R.color.dribble_red));
            txt_img_4.setText("حذف تصویر");
            txt_img_4.setTag("delete");
            try {
//            picasso.get()
//                    .load(docUrl4)
//                    .placeholder(R.drawable.progress_animation)
//                    .error(R.drawable.receipt_search)
//                    .into(imgNewDoc_4);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation);

                Glide.with(Document_Detail_Activity.this).load(docUrl4).apply(options).into(imgNewDoc_4);
                imgNewDoc_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Document_Detail_Activity.this, ImageEnlargedActivity.class);
                        intent.putExtra(EXTRA_IMAGE_URL, docUrl4);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {

            }
        }else {
            imgTick_4.setVisibility(View.GONE);
        }
        if (!docUrl5.equals("")) {
            crd5.setVisibility(View.VISIBLE);
            imgTick_5.setVisibility(View.VISIBLE);
            txt_img_5.setBackgroundColor(getResources().getColor(R.color.dribble_red));
            txt_img_5.setText("حذف تصویر");
            txt_img_5.setTag("delete");
            try {
//            picasso.get()
//                    .load(docUrl5)
//                    .placeholder(R.drawable.progress_animation)
//                    .error(R.drawable.receipt_search)
//                    .into(imgNewDoc_5);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation);

                Glide.with(Document_Detail_Activity.this).load(docUrl5).apply(options).into(imgNewDoc_5);
                imgNewDoc_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Document_Detail_Activity.this, ImageEnlargedActivity.class);
                        intent.putExtra(EXTRA_IMAGE_URL, docUrl5);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {

            }
        }else {
            imgTick_5.setVisibility(View.GONE);
        }

txt_img_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(txt_img_1.getTag().equals("add")){
            showImageSelectionDialog(1,11);

        } else if (txt_img_1.getTag().equals("delete")) {

                    prg_new_doc.setVisibility(View.VISIBLE);
                    deleteImageById(ImageID1,1,imgNewDoc_1);


        }
    }
});

txt_img_2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(txt_img_2.getTag().equals("add")){
            showImageSelectionDialog(2,22);

        } else if (txt_img_2.getTag().equals("delete")) {


                prg_new_doc.setVisibility(View.VISIBLE);
                deleteImageById(ImageID2, 2, imgNewDoc_2);
            }
        }

});

  txt_img_3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(txt_img_3.getTag().equals("add")){
            showImageSelectionDialog(3,33);

        } else if (txt_img_3.getTag().equals("delete")) {

                    prg_new_doc.setVisibility(View.VISIBLE);
                    deleteImageById(ImageID3,3,imgNewDoc_3);
                }

    }
});

  txt_img_4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(txt_img_4.getTag().equals("add")){
            showImageSelectionDialog(4,44);

        } else if (txt_img_4.getTag().equals("delete")) {

            prg_new_doc.setVisibility(View.VISIBLE);
            deleteImageById(ImageID4, 4, imgNewDoc_4);
        }
    }
});

  txt_img_5.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(txt_img_5.getTag().equals("add")){
            showImageSelectionDialog(5,55);

        } else if (txt_img_5.getTag().equals("delete")) {

                    prg_new_doc.setVisibility(View.VISIBLE);
                    deleteImageById(ImageID5,5,imgNewDoc_5);

        }

    }
});




    }

    private void showImageSelectionDialog(int i, int i1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image")
                .setItems(new CharSequence[]{"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    openGallery(i);
                                } else {

                                    if (getCameraPermission()) {
                                        try {
                                            openCamera(i1);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else {
                                        getCameraPermission();
                                    }


                                }
                            }
                        })
                .show();
    }

    private void openCamera(int i1) throws IOException {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();

        if(i1==11){
            if (photoFile != null) {
                imageUri1 = FileProvider.getUriForFile(Document_Detail_Activity.this,
                        "com.oshanak.mobilemarket.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);


                startActivityForResult(cameraIntent, i1);
            }} else if (i1==22) {
            if (photoFile != null) {
                imageUri2 = FileProvider.getUriForFile(Document_Detail_Activity.this,
                        "com.oshanak.mobilemarket.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);


                startActivityForResult(cameraIntent, i1);

            }}else if (i1==33) {
            if (photoFile != null) {
                imageUri3 = FileProvider.getUriForFile(Document_Detail_Activity.this,
                        "com.oshanak.mobilemarket.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);


                startActivityForResult(cameraIntent, i1);
            }}else if (i1==44) {
            if (photoFile != null) {
                imageUri4 = FileProvider.getUriForFile(Document_Detail_Activity.this,
                        "com.oshanak.mobilemarket.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri4);


                startActivityForResult(cameraIntent, i1);
            }}else if (i1==55) {
            if (photoFile != null) {
                imageUri5 = FileProvider.getUriForFile(Document_Detail_Activity.this,
                        "com.oshanak.mobilemarket.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);


                startActivityForResult(cameraIntent, i1);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return imageFile;

    }

    private boolean getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Camera permission is already granted
            // Access the camera here

            return true;
        } else {
            // Camera permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        return false;
    }

    private void openGallery(int i) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, i);
    }

    private void deleteImageById(int parseInt, int order,ImageView imgNewDoc_1) {
        Common c = new Common(this);
        String s = c.URL();



        ApiInterface apiInterface;

        if (s.contains("pilot")){
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        }else{
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }
        Call<DeleteDocResult> logCall = apiInterface.deleteImageById(
                "DeleteDocumentItem",new DeleteDocRequestModel(parseInt,new metaData(GlobalData.getUserName(),GlobalData.getAppVersionCode(), Utility.getDeviceInfo(),Utility.applicationMode+"")));

        logCall.enqueue(new Callback<DeleteDocResult>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<DeleteDocResult> call, Response<DeleteDocResult> response) {



                if (response.body().isSuccessful()==true && response.body().getMessage().equals("Successful.")) {

                    prg_new_doc.setVisibility(View.GONE);
showSuccessToast("تصویر با موفقیت حذف شد");

                    switch (order){
                        case 1:
                            imgNewDoc_1.setImageResource(R.drawable.receipt_search);
                            txt_img_1.setText("انتخاب تصویر");
                            imgTick_1.setVisibility(View.GONE);
                            txt_img_1.setTag("add");
                            txt_img_1.setBackgroundColor(getResources().getColor(R.color.haft_orange));
                            imageUri1=null;
                            break;
                        case 2:
                            imgNewDoc_2.setImageResource(R.drawable.receipt_search);
                            txt_img_2.setText("انتخاب تصویر");
                            imgTick_2.setVisibility(View.GONE);

                            txt_img_2.setTag("add");
                            txt_img_2.setBackgroundColor(getResources().getColor(R.color.haft_orange));
                            imageUri2=null;
                            break;
                        case 3:
                            imgNewDoc_3.setImageResource(R.drawable.receipt_search);
                            txt_img_3.setText("انتخاب تصویر");
                            imgTick_3.setVisibility(View.GONE);
                            txt_img_3.setTag("add");
                            txt_img_3.setBackgroundColor(getResources().getColor(R.color.haft_orange));
                            imageUri3=null;
                            break;
                        case 4:
                            imgNewDoc_4.setImageResource(R.drawable.receipt_search);
                            txt_img_4.setText("انتخاب تصویر");
                            imgTick_4.setVisibility(View.GONE);
                            txt_img_4.setTag("add");
                            txt_img_4.setBackgroundColor(getResources().getColor(R.color.haft_orange));
                            imageUri4=null;
                            break;
                        case 5:
                            imgNewDoc_5.setImageResource(R.drawable.receipt_search);
                            txt_img_5.setText("انتخاب تصویر");
                            imgTick_5.setVisibility(View.GONE);
                            txt_img_5.setTag("add");
                            txt_img_5.setBackgroundColor(getResources().getColor(R.color.haft_orange));
                            imageUri5=null;
                            break;
                    }


                } else {
                    prg_new_doc.setVisibility(View.GONE);
                    showErrorToast("حذف تصویر موفقیت آمیز نبود");


                }

            }

            @Override
            public void onFailure(Call<DeleteDocResult> call, Throwable t) {

                showErrorToast("حذف تصویر موفقیت آمیز نبود");
                prg_new_doc.setVisibility(View.GONE);

            }
        });
    }

    private void getDocsYype(metaData _metaData) {

        Common c = new Common(this);
        String s = c.URL();



        ApiInterface apiInterface;

        if (s.contains("pilot")){
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        }else{
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }

        Call<DocsTypeModel> logCall = apiInterface.getDocsTypes(
                "GetDocumentType", _metaData);

        logCall.enqueue(new Callback<DocsTypeModel>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<DocsTypeModel> call, Response<DocsTypeModel> response) {


                prg_new_doc.setVisibility(View.GONE);
                if (response.body() != null && response.isSuccessful() == true) {


                    ArrayList<DocsTypeModel_Id_name> docsTypeList=new ArrayList<>();

docsTypeList.add( new DocsTypeModel_Id_name(
                0,
                "نامشخص"));

                    for (int i = 0; i < response.body().docList.size(); i++) {
                        try {
                            docsTypeList.add(new DocsTypeModel_Id_name(
                                    response.body().getDocList().get(i).getID(),
                                    response.body().getDocList().get(i).getTitle()
                            ));

                        } catch (Exception e) {

                        }
                    }
//                    for (int i=0;i<response.body().docList.size();i++){
//                        try{
//                            docsTypeList.add(new DocsTypeModel_Id_name(response.body().getDocList().get(i).getID(),
//                                    response.body().getDocList().get(i).getTitle()));
//                            Log.d("checkeroosdsad1","number"+i+"   "+response.body().getDocList().get(i));
//
//                        }catch (Exception e){
//                            Log.d("checkeroosdsad1",e.getMessage());
//                        }
//
//                    }
                    ArrayAdapter<DocsTypeModel_Id_name> adapter = new ArrayAdapter<DocsTypeModel_Id_name>(Document_Detail_Activity.this,
                            android.R.layout.simple_spinner_item, docsTypeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                    spinner.post(new Runnable() {
                        @Override
                        public void run() {


                                    spinner.setSelection(docType);


                        }

                    });
                    btnNewDocSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DocsTypeModel_Id_name d = (DocsTypeModel_Id_name) spinner.getSelectedItem();

                            if (txt_docs_title.getText().toString().trim().equals("") || txt_docs_title.getText().toString().trim()== null){
                                txt_docs_title.setError("لطفا عنوان سند را وارد کنید");
                            } else if(d.getID()==0){
                                showErrorToast("لطفا نوع سند را مشخص کنید");

                            }else if (txt_doc_desc.getText().toString().trim().equals("") || txt_doc_desc.getText().toString().trim()== null){
                                txt_doc_desc.setError("لطفا شرح سند را وارد کنید");
                            }else  {
                                try {
                                    updateDoc(Doc_header_ID,txt_docs_title.getText().toString().trim(), txt_doc_desc.getText().toString().trim(), ((DocsTypeModel_Id_name) spinner.getSelectedItem()).getID());

                                }catch (Exception e){

                                }
                            }
                        }
                    });
                } else {
                    prg_new_doc.setVisibility(View.GONE);
                    showErrorToast("مشکلی در واکاوی اطلاعات رخ داد.");


                }

            }

            @Override
            public void onFailure(Call<DocsTypeModel> call, Throwable t) {
                showErrorToast("مشکلی در واکاوی اطلاعات رخ داد.");
                prg_new_doc.setVisibility(View.GONE);
            }
        });
    }

    private void updateDoc(int _doc_header_ID, String doc_title, String doc_description, int doc_doctype) {

        Common c = new Common(this);
        String s = c.URL();



        ApiInterface apiInterface;

        if (s.contains("pilot")){
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        }else{
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }

        Call<DocResponse> logCall = apiInterface.updateDoc("UpdateDocument",
                new UpdateDocModel(doc_title,doc_description,doc_doctype,_doc_header_ID,new com.oshanak.mobilemarket.Activity.Models.metaData(GlobalData.getUserName().toString(),GlobalData.getAppVersionCode(),Utility.getDeviceInfo(),Utility.applicationMode.name())));
        logCall.enqueue(new Callback<DocResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<DocResponse> call, Response<DocResponse> response) {

                if(response.body().getMessage().equals("Successful.")) {
                    Intent intent = new Intent(Document_Detail_Activity.this, UploadDocsActivity.class);

showSuccessToast(" سند با موفقیت اصلاح شد");
                    startActivity(intent);
                }else{

                }
            }

            @Override
            public void onFailure(Call<DocResponse> call, Throwable t) {

            showErrorToast("اصلاح سند موفقیت آمیز نبود");
            }
        });
    }
    private void sendDocsImageToserver(int headerID, Uri fileUri, int image_num,int _ImageID) throws IOException {
        File file=null;

        String strpath1="";

        try {
            strpath1=createCopyAndReturnRealPath(Document_Detail_Activity.this,fileUri);

        }catch (Exception e){

        }


        String coddedStrPah="";




        try {

            coddedStrPah= resizeImage(strpath1,maxRatio,maxRatio);

        }catch (Exception e){

        }



        if(coddedStrPah.equals("")){
            coddedStrPah=null;
        }


        try {
            file = new File(coddedStrPah);


        }catch (Exception e){


        }

        RequestBody image=null;

        MultipartBody.Part imagePart=null;




        if (file!=null){
            image = RequestBody.create(MediaType.parse("image/jpeg"), file);
            imagePart = MultipartBody.Part.createFormData("image1", file.getName(), image);


        }






        Common c = new Common(this);
        String s = c.URL();



        ApiInterface apiInterface;

        if (s.contains("pilot")){
            apiInterface = Doc_Upload_API_Pilot.getAPI().create(ApiInterface.class);
        }else{
            apiInterface = Doc_Upload_API_Operation.getAPI().create(ApiInterface.class);
        }




        Call<UploadImageResponse> logCall = apiInterface.uploadImage("Upload",
                imagePart,headerID,GlobalData.getUserName().toString(),_ImageID);
        logCall.enqueue(new Callback<UploadImageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {



                if(response.body()!=null && response.body().getMessage().equals("Successful.")) {
                            showSuccessToast("تصویر سند با موفقیت ثبت شد.");


                    prg_new_doc.setVisibility(View.GONE);

                    switch (image_num){
                        case 1:
                        case 11:

                            crd2.setVisibility(View.VISIBLE);
                            ImageID1=response.body().getImageID();
                            imgTick_1.setVisibility(View.VISIBLE);
                            txt_img_1.setText("حذف تصویر");
                            txt_img_1.setTag("delete");
                            txt_img_1.setBackgroundColor(getResources().getColor(R.color.dribble_red));
                            break;
                        case 2:
                        case 22:
                            crd3.setVisibility(View.VISIBLE);
                            ImageID2=response.body().getImageID();
                            imgTick_2.setVisibility(View.VISIBLE);
                            txt_img_2.setText("حذف تصویر");
                            txt_img_2.setTag("delete");
                            txt_img_2.setBackgroundColor(getResources().getColor(R.color.dribble_red));
                            break;
                        case 3:
                        case 33:
                            crd4.setVisibility(View.VISIBLE);
                            ImageID3=response.body().getImageID();
                            imgTick_3.setVisibility(View.VISIBLE);
                            txt_img_3.setText("حذف تصویر");
                            txt_img_3.setTag("delete");
                            txt_img_3.setBackgroundColor(getResources().getColor(R.color.dribble_red));
                            break;
                        case 4:
                        case 44:
                            crd5.setVisibility(View.VISIBLE);
                            ImageID4=response.body().getImageID();
                            imgTick_4.setVisibility(View.VISIBLE);
                            txt_img_4.setText("حذف تصویر");
                            txt_img_4.setTag("delete");
                            txt_img_4.setBackgroundColor(getResources().getColor(R.color.dribble_red));
                            break;
                        case 5:
                        case 55:

                            txt_img_5.setText("حذف تصویر");
                            ImageID5=response.body().getImageID();
                            imgTick_5.setVisibility(View.VISIBLE);
                            txt_img_5.setTag("delete");
                            txt_img_5.setBackgroundColor(getResources().getColor(R.color.dribble_red));

                            break;

                    }

                }else{


showErrorToast("مشکلی در ثبت تصویر رخ داد");
                    prg_new_doc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                showErrorToast("مشکلی در ثبت تصویر رخ داد");
                prg_new_doc.setVisibility(View.GONE);
            }
        });
    }
    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {

        if(image.getWidth() < maxRatio && image.getHeight() < maxRatio)
        {
            return image;
        }
        if (maxHeight > 0 && maxWidth > 0)
        {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            }
            else
            {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        }
        return image;
    }
    public  String resizeImage(String imagePath, int targetWidth, int targetHeight) {
        // Decode the image file into a Bitmap object
        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);

        // Calculate the desired scaling ratio
        float widthRatio = (float) targetWidth / originalBitmap.getWidth();
        float heightRatio = (float) targetHeight / originalBitmap.getHeight();

        // Create a matrix for the resizing operation
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio, heightRatio);

        // Resize the bitmap
//        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        Bitmap resizedBitmap = resize(originalBitmap,maxRatio,maxRatio);

        // Create a file to store the resized image
        File resizedFile = createResizedFile();

        // Save the resized bitmap to the file
        saveBitmapToFile(resizedBitmap, resizedFile);

        // Update media library to include the resized image
        updateMediaLibrary(resizedFile);

        // Return the resized image path as a string
        return resizedFile.getAbsolutePath();
    }

    private static File createResizedFile() {
        // Define the directory to save the resized image
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ResizedImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp+"resized_image.jpg";
        // Create a new file to store the resized image

        return new File(directory, fileName);
    }

    private static void saveBitmapToFile(Bitmap bitmap, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (IOException e) {
            Log.e("ImageResizer", "Error saving bitmap to file: " + e.getMessage());
        }
    }

    private void updateMediaLibrary(File file) {
        MediaScannerConnection.scanFile(
                Document_Detail_Activity.this,
                new String[]{file.getAbsolutePath()},
                null,
                null
        );
    }


    public String resizeImage(int i, String imagePath, int targetWidth, int targetHeight) {
        // Load the image from the specified path
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate the desired scale to resize the image
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        int scaleFactor = Math.min(imageWidth / targetWidth, imageHeight / targetHeight);

        // Set the options to load the image with the calculated scale
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        // Decode the image file into a Bitmap object
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        // Rotate the image based on its orientation
        int rotationAngle = getRotationAngle(imagePath);
        bitmap = rotateBitmap(bitmap, rotationAngle);

        // Resize the Bitmap to the desired dimensions
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);

        // Create a new file path for the resized image
        String resizedImagePath = generateResizedImagePath(1);

        // Save the resized image to the new file path
        saveBitmapToFile(resizedBitmap, resizedImagePath);

//        // Return the new file path of the resized image
        return imagePath;
    }

    private int getRotationAngle(String imagePath) {
        int rotationAngle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationAngle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationAngle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationAngle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotationAngle;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private String generateResizedImagePath(int i) {
        // Create a file name for the resized image
        String fileName = "resized_image"+i+getDateTime()+".jpg";

        // Get the directory path for saving the resized image
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File resizedImageFile = new File(directory, fileName);

        // Return the absolute path of the resized image file
        return resizedImageFile.getAbsolutePath();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("mm-ss-SSS");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void saveBitmapToFile(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = ((ContentResolver) contentResolver).openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }
    private void showSuccessToast(String message) {
        View view = getLayoutInflater().inflate(R.layout.ctoast_view,null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    private void showErrorToast(String message) {
        View view = getLayoutInflater().inflate(R.layout.ctoast_view_error,null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }


    private void setUp() {
        spinner = findViewById(R.id.spinner_doc_type);
        txt_doc_desc = findViewById(R.id.new_txt_docs_desc);
        txt_docs_title = findViewById(R.id.new_txt_docs_title);
        imgNewDoc_1 = findViewById(R.id.new_img_uploaded_doc_1);
        imgNewDoc_2 = findViewById(R.id.new_img_uploaded_doc_2);
        imgNewDoc_3 = findViewById(R.id.new_img_uploaded_doc_3);
        imgNewDoc_4 = findViewById(R.id.new_img_uploaded_doc_4);
        imgNewDoc_5 = findViewById(R.id.new_img_uploaded_doc_5);
        imgTick_1 = findViewById(R.id.img1_tick);
        imgTick_2 = findViewById(R.id.img2_tick);
        imgTick_3 = findViewById(R.id.img3_tick);
        imgTick_4 = findViewById(R.id.img4_tick);
        imgTick_5 = findViewById(R.id.img5_tick);
        crd1=findViewById(R.id.crd_image_1);
        crd2=findViewById(R.id.crd_image_2);
        crd3=findViewById(R.id.crd_image_3);
        crd4=findViewById(R.id.crd_image_4);
        crd5=findViewById(R.id.crd_image_5);
        txt_img_1=findViewById(R.id.txt_stat_image_1);
        txt_img_2=findViewById(R.id.txt_stat_image_2);
        txt_img_3=findViewById(R.id.txt_stat_image_3);
        txt_img_4=findViewById(R.id.txt_stat_image_4);
        txt_img_5=findViewById(R.id.txt_stat_image_5);
        txt_img_1.setTag("add");
        txt_img_2.setTag("add");
        txt_img_3.setTag("add");
        txt_img_4.setTag("add");
        txt_img_5.setTag("add");
        btnNewDocSubmit = findViewById(R.id.btn_doc_update);
        txtLastUpdated_date=findViewById(R.id.txt_last_updated_date);
        prg_new_doc = findViewById(R.id.prg_doc_upload);

        crd1.setVisibility(View.GONE);
        crd2.setVisibility(View.GONE);
        crd3.setVisibility(View.GONE);
        crd4.setVisibility(View.GONE);
        crd5.setVisibility(View.GONE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d("checkmydatat",resultCode+"........"+data.toString()+"");

        if (resultCode == RESULT_OK) {



            if (requestCode == 1 && data != null) {
                imageUri1 = data.getData();



                crd1.setVisibility(View.VISIBLE);

                imgNewDoc_1.setImageURI(imageUri1);
                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image1", String.valueOf(imageUri1));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri1,1,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");

                }


            } else if (requestCode == 11) {

//                imageUri = data.getData();

                imgNewDoc_1.setImageURI(imageUri1);

                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image1", String.valueOf(imageUri1));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri1,1,0);
                } catch (IOException e) {

                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 2 && data != null) {
                imageUri2 = data.getData();


                imgNewDoc_2.setImageURI(imageUri2);

                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image2", String.valueOf(imageUri2));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri2,2,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 22) {

                imgNewDoc_2.setImageURI(imageUri2);

                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image2", String.valueOf(imageUri2));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri2,2,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            }
            else if (requestCode == 3 && data != null) {
                imageUri3 = data.getData();


                imgNewDoc_3.setImageURI(imageUri3);

                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image3", String.valueOf(imageUri3));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri3,3,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 33) {

                imgNewDoc_3.setImageURI(imageUri3);


                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image3", String.valueOf(imageUri4));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri3,3,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 4 && data != null) {
                imageUri4 = data.getData();


                imgNewDoc_4.setImageURI(imageUri4);
                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image4", String.valueOf(imageUri4));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri4,4,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 44) {

                imgNewDoc_4.setImageURI(imageUri4);
                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image4", String.valueOf(imageUri4));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri4,4,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 5 && data != null) {
                imageUri5 = data.getData();

                imgNewDoc_5.setImageURI(imageUri5);
                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image5", String.valueOf(imageUri5));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri5,5,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            } else if (requestCode == 55) {


                imgNewDoc_5.setImageURI(imageUri5);
                SharedPreferences.Editor editor = getSharedPreferences("User_Data", MODE_PRIVATE).edit();
                editor.putString("user_image5", String.valueOf(imageUri5));
                editor.apply();
                try {
                    sendDocsImageToserver(Doc_header_ID,imageUri5,5,0);
                } catch (IOException e) {
                    showErrorToast("ثبت تصویر موفقیت آمیز نبود");
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                // Access the camera here
            } else {
                // Camera permission denied
                // Handle the denial case here
            }
        }
        if (requestCode == 22) {
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted, perform required code

                } else {
                    // not granted
                }
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Document_Detail_Activity.this,UploadDocsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}