package com.takkat.counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.Model.ItemGetFile;
import com.takkat.counting.Model.ItemGetImage;
import com.takkat.counting.adapter.AdapterFile;
import com.takkat.counting.adapter.AdapterImage;
import com.takkat.counting.fil.NewCameraActivity;
import com.takkat.counting.fil.NewGallaryActivity;
import com.takkat.counting.fil.ShowImageActivity;
import com.takkat.counting.fil.UploadFileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FillDataRequestActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener ,
        AdapterImage.OnItemClickListener{

    private int iEvId;
    private String sEvAddDate, sSchemaNum, sRequestNum, sAqarType, sRegion, sCity, sVallage, sCusName, sCusMobile;

    private TextView tReqNumber, tAqartype, tRegion, tCity, tVallage, tSchemaNumber, tCustomerName, tCustomerMobile;

    private ImageButton cum, gal, pd;
    private Button btnSa, btnFin;
    private CheckBox chkCus;
    private String stChek;
    private Domain domain;
    private SaveData sD;
    private EditText edNots, edRep;
    private String edsNots, edsRep;

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;

    private ArrayList<ItemGetImage> item;
    private RecyclerView mRecycler;
    private AdapterImage mAdapter;

    private ArrayList<ItemGetFile> itemFile;
    private RecyclerView mRecyclerFile;
    private AdapterFile mAdapterFile;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data_request);


        dataInIntent();


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.default_glow_color,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

     /*   mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                imageEval();
                fileEval();
            }
        });*/

        cum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Bundle args = new Bundle();
                args.putInt("idEv", iEvId);
                CustomerCameraDialog dialog = new CustomerCameraDialog();
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "MyCustomerLocationDailog");*/

                Intent intent = new Intent(FillDataRequestActivity.this , NewCameraActivity.class);
                intent.putExtra("idEv" , iEvId);
                startActivity(intent);
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FillDataRequestActivity.this, NewGallaryActivity.class);
                intent.putExtra("idEv", iEvId);
                startActivity(intent);


            }
        });

        pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FillDataRequestActivity.this, UploadFileActivity.class);
                intent.putExtra("idEv", iEvId);
                startActivity(intent);
            }
        });

        chkCus = findViewById(R.id.chcbox_cust);


        chkCus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                stChek = String.valueOf(isChecked);

            }
        });
    }

    private void dataInIntent() {

        iEvId = getIntent().getIntExtra("EvaluationID", 0);
        sEvAddDate = getIntent().getStringExtra("EvalAddedDate");
        sSchemaNum = getIntent().getStringExtra("SchemaNumber");
        sRequestNum = getIntent().getStringExtra("RequestNumber");
        sAqarType = getIntent().getStringExtra("AqarType");
        sRegion = getIntent().getStringExtra("Region");
        sCity = getIntent().getStringExtra("City");
        sVallage = getIntent().getStringExtra("Vallage");
        sCusName = getIntent().getStringExtra("CustomerName");
        sCusMobile = getIntent().getStringExtra("CustomerMobile");

        mSwipeRefreshLayout = findViewById(R.id.progress_bar_fill_data_request);
        tReqNumber = findViewById(R.id.edit_text_fill_req_number);
        tReqNumber.setText(sRequestNum);

        tAqartype = findViewById(R.id.edit_text_fill_type_aqar);
        tAqartype.setText(sAqarType);

        tRegion = findViewById(R.id.edit_text_fill_region);
        tRegion.setText(sRegion);

        tCity = findViewById(R.id.edit_text_fill_city);
        tCity.setText(sCity);

        tVallage = findViewById(R.id.edit_text_fill_vallage);
        tVallage.setText(sVallage);

        tSchemaNumber = findViewById(R.id.edit_text_fill_schema_number);
        tSchemaNumber.setText(sSchemaNum);

        tCustomerName = findViewById(R.id.edit_text_fill_cust_name);
        tCustomerName.setText(sCusName);

        tCustomerMobile = findViewById(R.id.edit_text_fill_cust_number);
        tCustomerMobile.setText(sCusMobile);

        edNots = findViewById(R.id.edit_text_fill_nots);
        edRep = findViewById(R.id.edit_text_fill_report);

        cum = findViewById(R.id.button_intent_to_camera_three_image);
        gal = findViewById(R.id.button_intent_to_gallary_three_image);
        pd = findViewById(R.id.lin_pdf);

        mRecycler = findViewById(R.id.recycler_get_image);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(FillDataRequestActivity.this));


        mRecyclerFile = findViewById(R.id.recycler_get_file);
        mRecyclerFile.setHasFixedSize(true);
        mRecyclerFile.setLayoutManager(new LinearLayoutManager(FillDataRequestActivity.this));


        btnSa = findViewById(R.id.btn_save_moamla);
        btnSa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validata()) {

                    try {
                        saveEval("Save", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(FillDataRequestActivity.this, "من فضلك ادخل باقى البيانات .. ", Toast.LENGTH_SHORT).show();


                }


            }
        });

        btnFin = findViewById(R.id.btn_finish_moamla);
        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validata()) {


                    try {
                        saveEval("Finished", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(FillDataRequestActivity.this, "من فضلك ادخل باقى البيانات .. ", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void saveEval(String stat, int sca) throws JSONException {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        String URL = domain.URL + "/moqaeemActions/addEval";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("EvalIDs", iEvId);
        jsonObject.put("state", stChek);

        JSONArray fn = new JSONArray();
        fn.put("");
        jsonObject.put("fileNames", fn);


        JSONArray bs = new JSONArray();
        bs.put("");
        jsonObject.put("fileBase64s", bs);

        JSONArray t = new JSONArray();
        t.put("");
        jsonObject.put("fileTypes", t);


        jsonObject.put("MoqaeemNotes", edNots.getText().toString());
        jsonObject.put("MoqaeemReport", edRep.getText().toString());


        final String requestBody = jsonObject.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (sca) {

                            case 1:
                                Toast.makeText(FillDataRequestActivity.this, "تم حفظ المعامله... ", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:

                                finish();
                                Toast.makeText(FillDataRequestActivity.this, "تم انهاء المعامله ... ", Toast.LENGTH_SHORT).show();
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(FillDataRequestActivity.this, "" + response.data, Toast.LENGTH_SHORT).show();
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
                customHeaders.put("Authorization", "bearer 0CXxqMQkcfowIInF2-XjPsdkMm2-PNiTMpFBByU9lI1YPr9jRa6Kq0JVJI_3nh6gmmNCf-0EpKFZXYQFzIifAKX1GLDPNGL4C7wYruYBZTip1U86EbEVTOC6YahMPciJ0B3ktNCzXS9hsyD9Uu56TpZcMKBHfUsJlm40hNmkB08cK98qR4ua1XIaUOZ-OFwRk41C26iEEA-SWqyVlWuqUZXxL6MBH3UOFlTacmayoZgbpDdeoKv8Kpx7IuXHbPU0TGQPgDyxyUJ1ozfu4rjjZ0Nle0P4yiNFwFmB-xJ8-H7Y-0Mh-zrNK-FDyFuNPJvxfVVtIngxtoRcefeRL8bDCo_cb9cqlB6UZDySHUjzfA284GcBcvtNiUeByLZgcKyRM8mLM4iD4_qGU-pY20WCtZB2-wqXPdwBko9dPTBrTi_mucDB7yZhzki_WtXEJ2NFIcQDYcav4k7zQHYoTfUAkXqZ29lwU3ebAYy-qxmI-rJdJUipu0Mw7t0OLlbygsEBVFwreIycEpAFeH76eTkKb4ravEXEFj2Gue_O6Oj2mrg");
                customHeaders.put("Submit", stat);
                customHeaders.put("Content-Type", "application/json");


                return customHeaders;
            }
        };
        queue.add(request);

    }


    private boolean validata() {

        String no = edNots.getText().toString();

        if (no.isEmpty()) {
            edNots.setError("من فضلك ادخل الملاحظات ");
            return false;
        } else {

            edNots.setError(null);

        }

        String pr = edRep.getText().toString();

        if (pr.isEmpty()) {
            edRep.setError("من فضلك ادخل التقرير ");
            return false;
        } else {

            edRep.setError(null);

        }

        return true;
    }


    private void imageEval() {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        item = new ArrayList<>();

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(FillDataRequestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecycler.setLayoutManager(horizontalLayoutManagaer);
        try {

            requestQueue = Volley.newRequestQueue(FillDataRequestActivity.this);

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/one",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject object = jsonObject.getJSONObject("Images");
                                JSONArray jsonArray = object.getJSONArray("ImageName");

                                for (int t = 0; t < jsonArray.length(); t++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(t);
                                    String d = jsonObject1.getString("name");


                                    item.add(new ItemGetImage(d));
                                }


                                mAdapter = new AdapterImage(FillDataRequestActivity.this , item);
/*
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                mRecycler.setLayoutManager(layoutManager);*/

                                mRecycler.setAdapter(mAdapter);
                                mSwipeRefreshLayout.setRefreshing(false);
                                mAdapter.notifyDataSetChanged();

                                mAdapter.setOnItemClickListener(FillDataRequestActivity.this);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FillDataRequestActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();

                    headers.put("EvalID", String.valueOf(iEvId));
                    headers.put("Authorization", "bearer " + sD.Token);
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }


    }

    private void fileEval() {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        itemFile = new ArrayList<>();

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(FillDataRequestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerFile.setLayoutManager(horizontalLayoutManagaer);
        try {

            requestQueue = Volley.newRequestQueue(FillDataRequestActivity.this);

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/one",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);


                                String no = jsonObject.getString("MoqaeemNotes");
                                if (no.equals("") || no.equals(null) || no.equals("null")){
                                    edNots.setText("");
                                }else {
                                    edNots.setText(no);

                                }
                                String re = jsonObject.getString("MoqaeemReport");
                                if (re.equals("") || re.equals(null) || re.equals("null")){
                                    edRep.setText("");
                                }else {
                                    edRep.setText(re);

                                }


                                boolean dddd = jsonObject.getBoolean("AgentNotCoOperated");
                                chkCus.setChecked(dddd);

                                JSONObject object = jsonObject.getJSONObject("Files");
                                JSONArray jsonArray = object.getJSONArray("FileName");

                                for (int t = 0; t < jsonArray.length(); t++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(t);
                                    String d = jsonObject1.getString("name");


                                    itemFile.add(new ItemGetFile(d));
                                }


                                mAdapterFile = new AdapterFile(FillDataRequestActivity.this , itemFile);
/*
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                mRecycler.setLayoutManager(layoutManager);*/
                                mSwipeRefreshLayout.setRefreshing(false);
                                mRecyclerFile.setAdapter(mAdapterFile);

                                mAdapterFile.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FillDataRequestActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();

                    headers.put("EvalID", String.valueOf(iEvId));
                    headers.put("Authorization", "bearer " + sD.Token);
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }


    }

    @Override
    public void onRefresh() {
        imageEval();
        fileEval();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        imageEval();
        fileEval();

    }

    @Override
    public void onItemClick(int position) {

        ItemGetImage ima = item.get(position);
        Intent intent = new Intent(FillDataRequestActivity.this , ShowImageActivity.class);
        intent.putExtra("imName" , ima.getName());
        startActivity(intent);
    }
}
