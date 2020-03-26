package com.takkat.counting.fil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooser;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UploadFileActivity extends AppCompatActivity {

    private View view;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 13;
    private String path;
    private ProgressDialog dialog;
    private int evalId ;
    private Domain domain ;
    private SaveData sD ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        dialog = new ProgressDialog(this);
        evalId = getIntent().getIntExtra("idEv", 0);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            addFileChooserFragment();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addFileChooserFragment();
            }
        }
    }

    private void addFileChooserFragment() {
        FileChooser.Builder builder = new FileChooser.Builder(FileChooser.ChooserType.FILE_CHOOSER,
                new FileChooser.ChooserListener() {
                    @Override
                    public void onSelect(String path) {

                        String filename=path.substring(path.lastIndexOf("/")+1);

                        encodeFileToBase64Binary(new File(path),filename);

                    }
                }).setFileFormats(new String[]{".pdf"});
        try {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.file_chooser_fragment_container_framelayout, builder.build())
                    .commit();
        } catch (ExternalStorageNotAvailableException e) {
            Toast.makeText(this, "There is no external storage available on this device.",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private String encodeFileToBase64Binary(File yourFile,String pathhh) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final String  urlll = Base64.encodeToString(bytes,Base64.NO_WRAP);
//        Toast.makeText(ChoseFile_Pdf_Dialog.this, "Base64 : "+path, Toast.LENGTH_SHORT).show();

        // upload(path);


        new TTFancyGifDialog.Builder(UploadFileActivity.this)
//                .setTitle("تحميل pdf .....")
                // .setMessage("You can use TT Fancy Dialog without gif also.")
                .setPositiveBtnBackground("#303F9F")
                .setPositiveBtnText("Ok")
                .isCancellable(true)

                .setGifResource(R.drawable.scanner)
                .setNegativeBtnText("Cancel")

                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //  Toast.makeText(ChoseFile_Pdf_Dialog.this,"Ok",Toast.LENGTH_SHORT).show();
                        try {
                            upload(urlll, pathhh);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .OnNegativeClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //  Toast.makeText(ChoseFile_Pdf_Dialog.this,"Cancel ",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        return urlll;
    }

    public void upload(String ur, String na) throws JSONException {

        ProgressDialog dialog = new ProgressDialog(UploadFileActivity.this);
        dialog.setMessage("من فضلك انتظر...");
        dialog.show();

        String URL = domain.URL + "/moqaeemActions/OneBy";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("EvalID", evalId);
        jsonObject.put("Type", "Docs");
        jsonObject.put("Name", na);
        jsonObject.put("Base64Name", ur);
        final String requestBody = jsonObject.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(UploadFileActivity.this, "تم حفظ الملف ...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        dialog.dismiss();
                        Toast.makeText(UploadFileActivity.this, "error: " + response.data, Toast.LENGTH_SHORT).show();
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
