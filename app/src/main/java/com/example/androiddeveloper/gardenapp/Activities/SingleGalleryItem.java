package com.example.androiddeveloper.gardenapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androiddeveloper.gardenapp.R;

/**
 * Created by Android Developer on 4/11/2018.
 */

public class SingleGalleryItem extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_gallery_item);

        String url = getIntent().getStringExtra("imgurl");
        String title = getIntent().getStringExtra("imgtitle");
        String details = getIntent().getStringExtra("imgdetails");
        ImageView imageView = (ImageView) findViewById(R.id.imagesingle);
        TextView titlesingle = (TextView) findViewById(R.id.titlesingle);
        TextView detailsingle = (TextView) findViewById(R.id.details);
        Glide.with(this).load(url).into(imageView);
        titlesingle.setText(title);
        detailsingle.setText(details);
    }
}
