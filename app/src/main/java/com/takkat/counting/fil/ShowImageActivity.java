package com.takkat.counting.fil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.takkat.counting.R;

public class ShowImageActivity extends AppCompatActivity {

    private String sNameUmage ;
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        sNameUmage = getIntent().getStringExtra("imName");
        imageView = findViewById(R.id.show_image_image_view);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loaaading)
                .error(R.drawable.loaaading);


        Glide.with(ShowImageActivity.this).load(sNameUmage).apply(options).into(imageView);


    }
}
