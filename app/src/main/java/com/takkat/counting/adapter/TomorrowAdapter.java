package com.takkat.counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.takkat.counting.Model.EvalTomorrow;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TomorrowAdapter extends RecyclerView.Adapter<TomorrowAdapter.TomorrowAdapterViewHolder> {


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<EvalTomorrow> mUpload;
    private TomorrowAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(TomorrowAdapter.OnItemClickListener listener) {
        mListener = listener;

    }


    public TomorrowAdapter(Context mContext, List<EvalTomorrow> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public TomorrowAdapter.TomorrowAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.today_request_adapter, parent, false);
        return new TomorrowAdapter.TomorrowAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TomorrowAdapter.TomorrowAdapterViewHolder holder, int position) {

        EvalTomorrow gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
        holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());


    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class TomorrowAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;


        public TomorrowAdapterViewHolder(View view) {
            super(view);

            reqNumber = view.findViewById(R.id.txt_today_request_number);
            reqSchema = view.findViewById(R.id.txt_today_schema);
            reqDate = view.findViewById(R.id.txt_today_date);
            reqNameComp = view.findViewById(R.id.txt_today_name_comp);


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

                            Toast.makeText(mContext, " " + response, Toast.LENGTH_SHORT).show();

                            if (response.equals("true") || response.equals(true)){
                                removeAt(po);
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

                            Toast.makeText(mContext, " " + response, Toast.LENGTH_SHORT).show();
                            if (response.equals("true") || response.equals(true)){
                                removeAt(po);
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
