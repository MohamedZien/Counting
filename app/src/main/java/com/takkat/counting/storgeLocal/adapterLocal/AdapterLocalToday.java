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
import com.takkat.counting.Model.EvalToday;
import com.takkat.counting.R;
import com.takkat.counting.adapter.ToDayAdapter;
import com.takkat.counting.storgeLocal.modelLocal.ModelLocalToDay;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLocalToday extends RecyclerView.Adapter<AdapterLocalToday.AdapterLocalTodayViewHolder> {


    private View view;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private SaveData sD ;
    private Context mContext;
    private List<ModelLocalToDay> mUpload;
    private AdapterLocalToday.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(AdapterLocalToday.OnItemClickListener listener) {
        mListener = listener;

    }


    public AdapterLocalToday(Context mContext, List<ModelLocalToDay> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public AdapterLocalToday.AdapterLocalTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.today_request_adapter, parent, false);
        return new AdapterLocalToday.AdapterLocalTodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLocalToday.AdapterLocalTodayViewHolder holder, int position) {

        ModelLocalToDay gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
     /*   holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());*/


    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class AdapterLocalTodayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;


        public AdapterLocalTodayViewHolder(View view) {
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
