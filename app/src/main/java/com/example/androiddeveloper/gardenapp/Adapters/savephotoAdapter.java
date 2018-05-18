package com.example.androiddeveloper.gardenapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androiddeveloper.gardenapp.Models.savephotomodel;
import com.example.androiddeveloper.gardenapp.R;

import java.util.List;

/**
 * Created by Android Developer on 4/11/2018.
 */

public class savephotoAdapter extends RecyclerView.Adapter<savephotoAdapter.ViewHolder> {

    Context context;
    List<savephotomodel> MainImageUploadInfoList;

    public savephotoAdapter(Context context, List<savephotomodel> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public savephotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.savephoto_card, parent, false);

        savephotoAdapter.ViewHolder viewHolder = new savephotoAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(savephotoAdapter.ViewHolder holder, int position) {

        final savephotomodel UploadInfo = MainImageUploadInfoList.get(position);


        holder.title.setText(UploadInfo.getName());
        holder.moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = UploadInfo.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getUrl()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, moreinfo;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageViewsave);
            title = (TextView) itemView.findViewById(R.id.titlesave);
            moreinfo = (ImageView) itemView.findViewById(R.id.moreinfosave);


        }
    }
}
