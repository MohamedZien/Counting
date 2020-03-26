package com.takkat.counting.storgeLocal.adapterLocal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.R;
import com.takkat.counting.storgeLocal.modelLocal.ModelLocalToDay;
import com.takkat.counting.storgeLocal.modelLocal.ModelLocalTomorow;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLocalTomorow extends RecyclerView.Adapter<AdapterLocalTomorow.AdapterLocalTomorowViewHolder> {


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<ModelLocalTomorow> mUpload;
    private AdapterLocalTomorow.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(AdapterLocalTomorow.OnItemClickListener listener) {
        mListener = listener;

    }


    public AdapterLocalTomorow(Context mContext, List<ModelLocalTomorow> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public AdapterLocalTomorow.AdapterLocalTomorowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.today_request_adapter, parent, false);
        return new AdapterLocalTomorow.AdapterLocalTomorowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLocalTomorow.AdapterLocalTomorowViewHolder holder, int position) {

        ModelLocalTomorow gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
     /*   holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());*/


    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class AdapterLocalTomorowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;


        public AdapterLocalTomorowViewHolder(View view) {
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
