package com.example.androiddeveloper.gardenapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Admin.BookingRequests;
import com.example.androiddeveloper.gardenapp.Models.BookingOrder;
import com.example.androiddeveloper.gardenapp.R;
import com.example.androiddeveloper.gardenapp.EmailService.Sender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookingOrderAdapter extends RecyclerView.Adapter<BookingOrderAdapter.MyViewHolder> {
    private Context mContext;
    private List<BookingOrder> bookingorderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname, email, requesttime, date, time, noofpersons, additionalinformation, status;

        ImageView accept, reject;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            surname = (TextView) view.findViewById(R.id.surname);
            email = (TextView) view.findViewById(R.id.email);
            requesttime = (TextView) view.findViewById(R.id.requesttime);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
            noofpersons = (TextView) view.findViewById(R.id.noofpersons);
            additionalinformation = (TextView) view.findViewById(R.id.additionalinformation);
            status = (TextView) view.findViewById(R.id.status);

            accept = (ImageView) view.findViewById(R.id.accept);
            reject = (ImageView) view.findViewById(R.id.reject);


        }
    }

    public BookingOrderAdapter(Context mContext, List<BookingOrder> bookingorderList) {
        this.mContext = mContext;
        this.bookingorderList = bookingorderList;
    }


    @Override
    public BookingOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card, parent, false);

        return new BookingOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BookingOrderAdapter.MyViewHolder holder, int position) {
        final BookingOrder bookingorder = bookingorderList.get(position);
        //name, surname , email;
        holder.name.setText("Name  : " + bookingorder.getName() + " " + bookingorder.getSname());
        //holder.surname.setText(bookingorder.getSurname());
        holder.email.setText("Email  : " + bookingorder.getEmail());
        holder.requesttime.setText("Request Time : " + bookingorder.getRequesttime());

        holder.date.setText("Booking Date  : " + bookingorder.getDate());
        holder.time.setText("Booking Time  : " + bookingorder.getTime());
        holder.noofpersons.setText("No Of Persons  : " + bookingorder.getNoofpersons());
        holder.additionalinformation.setText("Additional Information  : " + bookingorder.getAditionalinfo());
        holder.status.setText("Status: " + bookingorder.getRequeststate());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(mContext,"Acccepted Clicked",Toast.LENGTH_SHORT).show();
              /*  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference imgref1 = firebaseDatabase.getReference().child("pendingimages");
                imgref1.child(bookingorder.getOdrid()).removeValue();*/
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("booking").child(bookingorder.getOdrid()).child("requeststate");

                myRef.setValue("Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(mContext, Sender.class);
                        i.putExtra("toemail", bookingorder.getEmail());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                Toast.makeText(mContext, "Reservation Successful", Toast.LENGTH_SHORT).show();
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(mContext,"Rejected Clicked",Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("booking").child(bookingorder.getOdrid()).child("requeststate");

                myRef.setValue("Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        //   DatabaseReference imgref = firebaseDatabase.getReference().child("gallery").child(UploadInfo.getKey());
                        DatabaseReference order = firebaseDatabase.getReference().child("booking");
                        order.child(bookingorder.getOdrid()).removeValue();
                        Intent i = new Intent(mContext, BookingRequests.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                Toast.makeText(mContext, "Reservation Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return bookingorderList.size();
    }


}
