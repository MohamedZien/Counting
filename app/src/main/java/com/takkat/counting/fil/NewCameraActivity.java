package com.takkat.counting.fil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class NewCameraActivity extends AppCompatActivity {

    private int evalId ;
    private int evaluationNumber;
    private LocationManager locationManager;
    private static final int CAMERA_REQUEST_CODE = 1;
    private final int RequestPermissionCode = 1;
    private final int ACTION_REQUEST_GALLERY = 2;
    final int LOCATION_REQEST_CODE = 3;
    private double latitude, longitude;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private String encodimg;
    private ImageButton imageView;
    private ImageView clear;
    private Button button;
    private Uri mImageUri;
    private SaveData sD;
    private FloatingActionButton addImage;
    private ProgressBar mProgress;
    private Domain domain ;
    private String namePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camera);


        definitionId();
        selectAndTakePhoto();
        clearImageView();
        uploadImage();
    }


    private void definitionId() {

        evalId = getIntent().getIntExtra("idEv" , 0);


        imageView = findViewById(R.id.image_customer_camera_dialog);
        clear = findViewById(R.id.clear_url_customer_camera_dialog);
        clear.setVisibility(View.INVISIBLE);
        mProgress = findViewById(R.id.progress_bar_wait_upload_image_customer_camera_dialog);
        mProgress.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.btn_save_photo_in_customer_camera_dialog);
        button.setVisibility(View.INVISIBLE);
        addImage = findViewById(R.id.add_image_in_request_customer_camera_dialog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(NewCameraActivity.this, " " + evalId, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void requestPermissionCamera() {

        ActivityCompat.requestPermissions(NewCameraActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;



                    if (CameraPermission && ReadContactsPermission) {

                        selectImage();

                    } else {



                    }
                }

                break;

            case LOCATION_REQEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(NewCameraActivity.this, "من فضلك تشغيل الصلاحيه", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }


    }


    public boolean checkPermissionCamera() {


        int FirstPermissionResult = ContextCompat.checkSelfPermission(NewCameraActivity.this, CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(NewCameraActivity.this, READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;


    }

    // check permission open GPS
    private void selectAndTakePhoto() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (checkPermissionCamera()) {
                    selectImage();

                } else {

                    requestPermissionCamera();

                }



            }
        });
    }


    private void selectImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }


    // set image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //   getLoactiona();
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:


                    mImageUri = data.getData();
                    imageView.setImageURI(mImageUri);
                    convert_base64();

                    break;


                case CAMERA_REQUEST_CODE:

                    if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);

                        byte[] byteArray = stream.toByteArray();
                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

                        // Display the compressed bitmap in ImageView
                        imageView.setImageBitmap(compressedBitmap);

                        // imageView.setImageBitmap(imageBitmap);

                        //getImageUri(getContext(),imageBitmap);
                        Uri tempUri = getImageUri(NewCameraActivity.this, compressedBitmap);
                        namePath = tempUri.getLastPathSegment();
                        convert_base64();

                        break;

                    }
            }


        }


        changeButton();
    }

    // cahnge image bitmap to uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String nameImage = "255" + " " + "-" + " " + "Latitude: " + latitude + " " + "-" + "Longitude: " + longitude;

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, nameImage, null);


        return Uri.parse(path);
    }

    private void convert_base64()  {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
        encodimg = Base64.encodeToString(byteArrayInputStream.toByteArray(), Base64.DEFAULT);

        Log.e("aaaaa" , encodimg);

    }

    private void changeButton() {

        if (encodimg != null) {

            button.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
            addImage.setVisibility(View.INVISIBLE);

        } else {

            button.setVisibility(View.INVISIBLE);
            clear.setVisibility(View.INVISIBLE);
            addImage.setVisibility(View.VISIBLE);

        }

    }


    private void clearImageView() {

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button.setVisibility(View.INVISIBLE);
                clear.setVisibility(View.INVISIBLE);
                addImage.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_camera_enhance_blue_220dp);
            }
        });
    }

    private void uploadImage(){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setVisibility(View.INVISIBLE);
                clear.setVisibility(View.INVISIBLE);

                // new CustomerCameraDialog.PostDataTask().execute(domain.URL +"/api/APIEvaluation/InsertEvImages");

                try {
                    Upload_Photos(encodimg , namePath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    class PostDataTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // progress start
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            mProgress.setVisibility(View.INVISIBLE);
            clear.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);

            addImage.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_camera_enhance_blue_220dp);
            Toast.makeText(NewCameraActivity.this, "تم حفظ الصوره...", Toast.LENGTH_SHORT).show();
            // uploadSetUnderRevsion();




            Toast.makeText(NewCameraActivity.this, "" + result, Toast.LENGTH_SHORT).show();


        }

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            JSONObject dataToSend = new JSONObject();
            try {
                //Create data to send to server
              /*  JSONObject dataToSend = new JSONObject();
                dataToSend.put("EvaluationId", evalId);

                JSONArray jsonImage = new JSONArray();
                jsonImage.put(encodimg);
                dataToSend.put("fileBase64s" , jsonImage);

                JSONArray jsonLng = new JSONArray();
                jsonLng.put("11");
                dataToSend.put("lat" , jsonLng.put("11"));
                dataToSend.put("lng" , jsonLng.put("11"));


                dataToSend.put("amp" ,jsonLng.put("11"));

*/

                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();
                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }

            return result.toString();
        }
    }


    public void Upload_Photos(String ur, String na) throws JSONException {

        ProgressDialog dialog = new ProgressDialog(NewCameraActivity.this);
        dialog.setMessage("من فضلك انتظر...");
        dialog.show();

        String URL = domain.URL + "/moqaeemActions/OneBy";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("EvalID", evalId);
        jsonObject.put("Type", "Imgs");
        jsonObject.put("Name", na+".jpg");
        jsonObject.put("Base64Name", ur);
        final String requestBody = jsonObject.toString();

        RequestQueue queue = Volley.newRequestQueue(NewCameraActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mProgress.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);

                        addImage.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.ic_camera_enhance_blue_220dp);
                        Toast.makeText(NewCameraActivity.this, "تم حفظ الصوره...", Toast.LENGTH_SHORT).show();
                        // uploadSetUnderRevsion();
                        dialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;


                        mProgress.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);

                        addImage.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.ic_camera_enhance_blue_220dp);



                        Toast.makeText(NewCameraActivity.this, "" +  response.data, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

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
