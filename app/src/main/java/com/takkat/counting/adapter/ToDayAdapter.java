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
import com.takkat.counting.Model.EvalToday;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDayAdapter  extends RecyclerView.Adapter<ToDayAdapter.ToDayAdapterViewHolder> {


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<EvalToday> mUpload;
    private ToDayAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(ToDayAdapter.OnItemClickListener listener) {
        mListener = listener;

    }


    public ToDayAdapter(Context mContext, List<EvalToday> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public ToDayAdapter.ToDayAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.today_request_adapter, parent, false);
        return new ToDayAdapter.ToDayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDayAdapter.ToDayAdapterViewHolder holder, int position) {

        EvalToday gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
        holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());


    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class ToDayAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;


        public ToDayAdapterViewHolder(View view) {
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

}
