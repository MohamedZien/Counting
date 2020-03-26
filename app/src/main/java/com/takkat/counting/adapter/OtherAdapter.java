package com.takkat.counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.takkat.counting.Model.EvalOther;
import com.takkat.counting.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OtherAdapter  extends RecyclerView.Adapter<OtherAdapter.OtherAdapterViewHolder> {


    private View view;

    private Context mContext;
    private List<EvalOther> mUpload;
    private OtherAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(OtherAdapter.OnItemClickListener listener) {
        mListener = listener;

    }


    public OtherAdapter(Context mContext, List<EvalOther> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }


    @NonNull
    @Override
    public OtherAdapter.OtherAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.today_request_adapter, parent, false);
        return new OtherAdapter.OtherAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherAdapter.OtherAdapterViewHolder holder, int position) {

        EvalOther gEval = mUpload.get(position);

        holder.reqNumber.setText("" + gEval.getRequestNumber());
        holder.reqSchema.setText("" + gEval.getSchemaNumber());
        holder.reqDate.setText("" + gEval.getEvalAddedDate());
        holder.reqNameComp.setText("" + gEval.getCompName());


    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }


    public class OtherAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reqNumber, reqSchema, reqDate, reqNameComp;


        public OtherAdapterViewHolder(View view) {
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
