package com.takkat.counting.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.FillDataRequestActivity;
import com.takkat.counting.Model.gEval;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.AcceptRequestAdapterViewHolder>{


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<gEval> mUpload;

    private int azz = 2 ;
    private AcceptRequestAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(AcceptRequestAdapter.OnItemClickListener listener) {
        mListener = listener;

    }


    public AcceptRequestAdapter(Context mContext, List<gEval> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public AcceptRequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.accept_request_adapter, parent, false);
        return new AcceptRequestAdapter.AcceptRequestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptRequestAdapterViewHolder holder, int position) {

        gEval gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
        holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());

        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*

                Intent intent=new Intent(mContext, DateMoaaenaActivity.class);
                intent.putExtra("idRequest" , gEval.getEvaluationID());
                mContext.startActivity(intent);

*/

              //  a();
               holder.linDatAndTime.setVisibility(View.VISIBLE);


            }
        });

        holder.clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linDatAndTime.setVisibility(View.GONE);
                holder.txtTi.setText("");
                holder.txtDa.setText("");
            }
        });

        holder.upda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.txtTi.getText().toString().equals("") || holder.txtDa.getText().toString().equals("")){

                    Toast.makeText(mContext, "من فضلك ادخل البيانات بالكامل... ", Toast.LENGTH_SHORT).show();
                }else {

                    timeRequest(holder.txtTi.getText().toString() ,holder.txtDa.getText().toString() ,  gEval.getEvaluationID());

                    holder.linDatAndTime.setVisibility(View.GONE);
                    holder.txtTi.setText("");
                    holder.txtDa.setText("");
                }

            }
        });

        holder.moaaena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent = new Intent(mContext , FillDataRequestActivity.class);
              intent.putExtra("EvaluationID" , gEval.getEvaluationID());
              intent.putExtra("EvalAddedDate" , gEval.getEvalAddedDate());
              intent.putExtra("SchemaNumber" , gEval.getSchemaNumber());
              intent.putExtra("RequestNumber" , gEval.getRequestNumber());
              intent.putExtra("AqarType" , gEval.getAqarType());
              intent.putExtra("Region" , gEval.getRegion());
              intent.putExtra("City" , gEval.getCity());
              intent.putExtra("CustomerName" , gEval.getCustomerName());
              intent.putExtra("CustomerMobile" , gEval.getCustomerMobile());
              intent.putExtra("Vallage" , gEval.getVallage());
              mContext.startActivity(intent);
            }
        });

        holder.txtTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();

               int tiHour = c.get(Calendar.HOUR_OF_DAY);
                int tiMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, R.style.DatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        String sTime = hourOfDay + ":" + minute + ":" + "00";
                        holder.txtTi.setText(sTime);
                    }
                },tiHour , tiMinute,false);


                timePickerDialog.show();
            }
        });


        holder.txtDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Calendar c = Calendar.getInstance();

                int tiYear = c.get(Calendar.YEAR);
                int tiMonth = c.get(Calendar.MONTH);
                int tiDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(mContext, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String sDate = year + "-" +  (month +1 ) + "-" + dayOfMonth ;
                        holder.txtDa.setText(sDate);
                    }
                }, tiYear, tiMonth, tiDay);

                dialog.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class AcceptRequestAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;
        private Button time, moaaena;
        private LinearLayout linDatAndTime ;
        private Button upda , clos ;
        private TextView txtTi , txtDa ;

        public AcceptRequestAdapterViewHolder(View view) {
            super(view);

            reqNumber = view.findViewById(R.id.txt_accept_request_request_number);
            reqSchema = view.findViewById(R.id.txt_accept_request_schema);
            reqDate = view.findViewById(R.id.txt_accept_request_date);
            reqNameComp = view.findViewById(R.id.txt_accept_request_name_comp);
            time = view.findViewById(R.id.btn_add_time_request);
            moaaena = view.findViewById(R.id.btn_show_request);
            linDatAndTime = view.findViewById(R.id.linear_tim_dat);
            upda = view.findViewById(R.id.btn_update_tim_and_dat);
            clos = view.findViewById(R.id.btn_close_tim_and_dat);


            txtTi = view.findViewById(R.id.txt_upload_new_time);
            txtDa =view.findViewById(R.id.txt_upload_new_date);


        }


        @Override
        public void onClick(View v) {


            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }

    }

/*

    private void timeRequest(int evID ){

        try {

            requestQueue = Volley.newRequestQueue(mContext);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +
                    "/moqaeemActions/addMeatingDate",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(mContext, " " + response, Toast.LENGTH_SHORT).show();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "" + error.getMessage(), Toast.LENGTH_SHORT).show();


                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "bearer " + sD.Token);
                    headers.put("EvalID", String.valueOf(evID));
                    headers.put("MeatingDate", "2020-03-11T13:04:29.097");
                    return headers;
                }


                @Override
                protected Map<String, String>  getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("EvalID", String.valueOf(3));
                    params.put("Type", "Imgs");
                    params.put("Name", "Profile");

                    return params;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }


    }

*/


    private void a(){

        Gson gson = new Gson();



        String url = "http://ihabsalman-001-site10.itempurl.com/moqaeemActions/addEval";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(mContext, " " + response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, " " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            /** Passing some request headers* */


            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "bearer " + "jHfUeYOVtDvzxbWp-Y-zVpLhG0SCF1rPxBi_vI5f8T69lSYZOL9L9Y9mq0g_1tnffuQBJb_n_tzuY1YrVe7p6u6EXl1N52-7Ob__MresUxfpkgOIU6gFZXcUCxX243afml2JNCwmAYfEF29cyyT1LGiJhIW1fA6FPg296-JwMHCPfaCemtBzg6wrJ7MFvo366RDnW96gx4y0zmt8_JyUnUOB1xFuINepqp7pPoUbCDr4am7z9Lw0dqtem-CSIIpxu8UATo9ReLfZ0MqqPUQcIcTNdKtOy92S0zqPbeIcK77GIWYqxLLC4mlaqrNaDwgxrOBUQ9Cm0aBOi5i4-LKzr8eS6eiuAIDmt-GJbzkixMDqGxfxoAtzbdR7lqHyOBvqxJ9IXrvFBO5SiXXKCv8eIhJUmonwbie_1mGm6y5vxDRFgO6v347PaL08jvhCOkX2AwfvYDtyVRH4fNs-f3S1BuSkhjDgBMTy-TeZpOGdq1CxXU0DlzB0gqJGyo7MA1XuYFAfbxC2Ag5hep12h9ZxtrCZHmvOJNanKfQNy8NLxG0");
                headers.put("Submit", "Save");
                return headers;
            }

            @Override
            protected Map<String, String>  getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("EvalIDs", String.valueOf(azz));
                params.put("state", "false");

                JSONArray n = new JSONArray();
                n.put("fd.png");

                String nn = gson.toJson(n);
               // params.put("fileNames" , nn);
                params.put("fileNames" , n.toString());


                JSONArray ba = new JSONArray();
                ba.put("oskpvsdkp");
                String baba = gson.toJson(ba);
               // params.put("fileBase64s" , baba);
                params.put("fileBase64s", ba.toString());



                JSONArray ft = new JSONArray();
                ft.put("Imgs");
                String ftft = gson.toJson(ft);

              //  params.put("fileTypes" , ftft);
                params.put("fileTypes" , ft.toString());


                return params;

            }


        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }


    private void timeRequest(String ti , String da , int ev ) {

        try {

            requestQueue = Volley.newRequestQueue(mContext);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +
                    "/moqaeemActions/addMeatingDate",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(mContext, " " + response, Toast.LENGTH_SHORT).show();


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "" + error.getMessage(), Toast.LENGTH_SHORT).show();


                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "bearer " + sD.Token);
                    headers.put("EvalID", String.valueOf(ev));
                    headers.put("MeatingDate", da + "T" + ti);
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }


    }


}
