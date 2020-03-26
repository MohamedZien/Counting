package com.takkat.counting.fil;

import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGallaryActivity extends AppCompatActivity implements View.OnClickListener {


    private static Button openCustomGallery;
    private static GridView selectedImageGridView;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;

    private Uri filePath;

    private TextView tes;

    private List<String> selectedImages;

    private String imagesArray;

    private int evalId;

    private Button btnUpl;

    private Domain domain;

    private SaveData sD ;


    private ProgressBar progressBar;

    private ProgressDialog dialog;

    private JSONArray jsonArray = new JSONArray();

    JSONArray imNameaa = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallary);


        evalId = getIntent().getIntExtra("idEv", 0);

        dialog = new ProgressDialog(this);

        initViews();
        setListeners();
        getSharedImages();


    }

    //Init all views
    private void initViews() {
        openCustomGallery = (Button) findViewById(R.id.openCustomGallery);
        selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);
    }

    //set Listeners
    private void setListeners() {
        openCustomGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openCustomGallery:
                //Start Custom Gallery Activity by passing intent id

                startActivityForResult(new Intent(NewGallaryActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);

                break;
        }

    }

    public void onActivityResult(int requestcode, int resultcode,
                                 Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case CustomGallerySelectId:
                if (resultcode == RESULT_OK) {
                    imagesArray = imagereturnintent.getStringExtra("ddddddddd");//get Intent data
                    //Convert string array into List by splitting by ',' and substring after '[' and before ']'
                    selectedImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));



                    for (String e : selectedImages) {

                        //progressBar.setVisibility(View.VISIBLE);


                        encodeFileToBase64Binary(new File(e), "");


                    }

                    loadGridView(new ArrayList<String>(selectedImages));//call load gridview method by passing converted list into arrayList
                }
                break;

        }
    }

    //Load GridView
    private void loadGridView(ArrayList<String> imagesArray) {
        GridView_Adapter adapter = new GridView_Adapter(NewGallaryActivity.this, imagesArray, false);
        selectedImageGridView.setAdapter(adapter);
    }

    //Read Shared Images
    private void getSharedImages() {

        //If Intent Action equals then proceed
        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
                && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);//get Parcelabe list
            ArrayList<String> selectedImages = new ArrayList<>();

            //Loop to all parcelable list
            for (Parcelable parcel : list) {
                Uri uri = (Uri) parcel;//get URI
                String sourcepath = getPath(uri);//Get Path of URI
                selectedImages.add(sourcepath);//add images to arraylist
            }
            loadGridView(selectedImages);//call load gridview
        }
    }


    //get actual path of uri
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private String encodeFileToBase64Binary(File yourFile, String na) {

        File compressedImageFile = Compressor.getDefault(this).compressToFile(yourFile);


        int size = (int) compressedImageFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(compressedImageFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String path = Base64.encodeToString(bytes, Base64.NO_WRAP);
        int currentTime = Calendar.getInstance().getTime().getSeconds();
        String dir = yourFile.getParent();

        File file = new File(yourFile.getAbsolutePath());

        Log.e("dddddd" , file.getName());
        try {
            Upload_Photos(path, file.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // upload(path);

        return path;
    }


    public void Upload_Photos(String ur, String na) throws JSONException {

        ProgressDialog dialog = new ProgressDialog(NewGallaryActivity.this);
        dialog.setMessage("من فضلك انتظر...");
        dialog.show();


        String URL = domain.URL + "/moqaeemActions/OneBy";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("EvalID", evalId);
        jsonObject.put("Type", "Imgs");
        jsonObject.put("Name", na );
        jsonObject.put("Base64Name", ur);
        final String requestBody = jsonObject.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(NewGallaryActivity.this, "تم حفظ الصوره...", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;

                        dialog.dismiss();
                        Toast.makeText(NewGallaryActivity.this, "error: " + response.data, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {    //this is the part, that adds the header to the request
            //this is where you need to add the body related methods
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> customHeaders = new HashMap<>();
                customHeaders.put("Authorization", "bearer " +sD.Token);
                customHeaders.put("Content-Type", "application/json");

                return customHeaders;
            }
        };
        queue.add(request);

    }

}
