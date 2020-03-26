package com.takkat.counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.takkat.counting.Model.ItemGetImage;
import com.takkat.counting.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.AdapterImageViewHolder> {


    private Context mContext ;
    private ArrayList<ItemGetImage> mExampleList ;
    private AdapterImage.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(AdapterImage.OnItemClickListener listener) {
        mListener = listener;

    }


    public AdapterImage(Context mContext, ArrayList<ItemGetImage> mExampleList) {
        this.mContext = mContext;
        this.mExampleList = mExampleList;
    }



    @NonNull
    @Override
    public AdapterImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.image_list , parent , false);
        return new AdapterImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageViewHolder holder, int position) {

        ItemGetImage currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getName();

        //Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.imageView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loaaading)
                .error(R.drawable.loaaading);


        Glide.with(mContext).load(imageUrl).apply(options).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class AdapterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView imageView ;

        public AdapterImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_adapter_image);
            itemView.setOnClickListener(this);


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
