package com.takkat.counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.takkat.counting.Model.gEval;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewRequestAdapter extends RecyclerView.Adapter<NewRequestAdapter.NewRequestAdapterViewHolder> {


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<gEval> mUpload;
    private NewRequestAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(NewRequestAdapter.OnItemClickListener listener) {
        mListener = listener;

    }


    public NewRequestAdapter(Context mContext, List<gEval> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public NewRequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.new_request_adapter, parent, false);
        return new NewRequestAdapter.NewRequestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRequestAdapterViewHolder holder, int position) {

        gEval gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
        holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());

        holder.addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptReq(gEval.getEvaluationID(),position);
            }
        });

        holder.canselRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                canselReq(gEval.getEvaluationID(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class NewRequestAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;
        private Button addRequest, canselRequest;

        public NewRequestAdapterViewHolder(View view) {
            super(view);

            reqNumber = view.findViewById(R.id.txt_new_request_request_number);
            reqSchema = view.findViewById(R.id.txt_new_request_schema);
            reqDate = view.findViewById(R.id.txt_new_request_date);
            reqNameComp = view.findViewById(R.id.txt_new_request_name_comp);

            addRequest = view.findViewById(R.id.btn_add_request);
            canselRequest = view.findViewById(R.id.btn_cansel_request);
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


    private void acceptReq(int evID , int po){

        try {

            requestQueue = Volley.newRequestQueue(mContext);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +
                    "/eval/status",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            if (response.equals("true") || response.equals(true)){
                                removeAt(po);

                                Toast.makeText(mContext, "تم قبول المعامله...", Toast.LENGTH_SHORT).show();

                            }



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
                    headers.put("EvalID", String.valueOf(evID));
                    headers.put("Status", "Accept");
                    headers.put("Who", "Moqaeem");
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

    private void canselReq(int evID , int po){

        try {

            requestQueue = Volley.newRequestQueue(mContext);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +
                    "/eval/status",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("true") || response.equals(true)){
                                removeAt(po);
                                Toast.makeText(mContext, "تم رفض المعامله...", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "error :" + error.getMessage(), Toast.LENGTH_SHORT).show();


                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("EvalID", String.valueOf(evID));
                    headers.put("Status", "Reject");
                    headers.put("Who", "Moqaeem");
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

    public void removeAt(int position) {
        mUpload.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mUpload.size());
    }

}
