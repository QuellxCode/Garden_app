package com.example.androiddeveloper.gardenapp.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androiddeveloper.gardenapp.R;
import com.example.androiddeveloper.gardenapp.Adapters.savephotoAdapter;
import com.example.androiddeveloper.gardenapp.Models.savephotomodel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Developer on 4/11/2018.
 */

public class SavePhoto extends android.support.v4.app.Fragment {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;
    String id;
    // Creating List of ImageUploadInfo class.
    List<savephotomodel> list;

    public SavePhoto() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.save_photo, container, false);

       /* prefs = rootView.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        id = prefs.getString("id", null);
*/


        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(rootView.getContext());

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images.");

        // Showing progress dialog.
        //  progressDialog.show();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_savephoto);
        list = new ArrayList<>();
        adapter = new savephotoAdapter(rootView.getContext(), list);

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));


        recyclerView.setAdapter(adapter);

        preparedata();
        //  progressDialog.dismiss();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
       /* databaseReference = FirebaseDatabase.getInstance().getReference("savephoto").child(id);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {




                    savephotomodel save = postSnapshot.getValue(savephotomodel.class);
                    String key = postSnapshot.getKey();
                    if(key.equalsIgnoreCase(id)){
                        list.add(save);
                    }

                }

                adapter = new savephotoAdapter(rootView.getContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });*/
        return rootView;
    }


    public void preparedata() {


        savephotomodel a = new savephotomodel(R.drawable.gallery1, "Alpine", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);
        a = new savephotomodel(R.drawable.gallery2, "Trial", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);
        a = new savephotomodel(R.drawable.gallery3, "Model", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);
   /*     a = new savephotomodel(R.drawable.herbsgarden, "Herbs", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);
        a = new savephotomodel(R.drawable.fruitgarden, "Fruit", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);
        a = new savephotomodel(R.drawable.alpinehouse, "Alpine", "https://www.rhs.org.uk/gardens/wisley");
        list.add(a);*/


        adapter.notifyDataSetChanged();


    }

}
