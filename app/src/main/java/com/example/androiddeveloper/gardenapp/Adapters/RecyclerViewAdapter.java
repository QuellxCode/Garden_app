package com.example.androiddeveloper.gardenapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androiddeveloper.gardenapp.Admin.DisplayImagesActivity;
import com.example.androiddeveloper.gardenapp.Models.ImageUploadInfo;
import com.example.androiddeveloper.gardenapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        holder.approve.setVisibility(View.VISIBLE);
        holder.disapprove.setVisibility(View.VISIBLE);
        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference imgref = firebaseDatabase.getReference().child("gallery").child(UploadInfo.getKey());
                DatabaseReference imgref1 = firebaseDatabase.getReference().child("pendingimages");
                imgref1.child(UploadInfo.getKey()).removeValue();

                Map<String, String> map = new HashMap<>();
                map.put("imgid", UploadInfo.getImageURL());
                map.put("status", "pending");
                map.put("title", UploadInfo.getTitle());
                map.put("details", UploadInfo.getDetails());
                map.put("dkey", UploadInfo.getKey());
                imgref.setValue(map);
                Toast.makeText(context, "Image Approved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, DisplayImagesActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);


            }
        });
        holder.disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference imgref = firebaseDatabase.getReference().child("pendingimages");
                imgref.child(UploadInfo.getKey()).removeValue();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                String s = "" + UploadInfo.getKey().charAt(3) + UploadInfo.getKey().charAt(4);
// Create a reference to the file to delete
                StorageReference desertRef = storageRef.child("images/gardens" + s + ".jpg");

// Delete the file
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Image Rejected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, DisplayImagesActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });


            }
        });
        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).placeholder(R.drawable.loading).into(holder.imageView);


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public Button approve, disapprove;

        public ViewHolder(View itemView) {
            super(itemView);
            approve = (Button) itemView.findViewById(R.id.approve);
            disapprove = (Button) itemView.findViewById(R.id.disapprove);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
        }
    }
}
