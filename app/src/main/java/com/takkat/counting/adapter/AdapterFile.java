package com.takkat.counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.takkat.counting.Model.ItemGetFile;
import com.takkat.counting.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterFile extends RecyclerView.Adapter<AdapterFile.AdapterFileViewHolder> {


    private Context mContext ;
    private ArrayList<ItemGetFile> mExampleList ;

    public AdapterFile(Context mContext, ArrayList<ItemGetFile> mExampleList) {
        this.mContext = mContext;
        this.mExampleList = mExampleList;
    }



    @NonNull
    @Override
    public AdapterFile.AdapterFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.file_list , parent , false);
        return new AdapterFile.AdapterFileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFile.AdapterFileViewHolder holder, int position) {

        ItemGetFile currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getName();

        //Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.imageView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loaaading)
                .error(R.drawable.loaaading);


        Glide.with(mContext).load(R.drawable.ic_insert_drive_file_red_100dp).apply(options).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class AdapterFileViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView ;

        public AdapterFileViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_adapter_file);



        }
    }
}
