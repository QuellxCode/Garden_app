package com.example.androiddeveloper.gardenapp.Adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Models.Client;
import com.example.androiddeveloper.gardenapp.R;

import java.util.List;

public class RegisterUserAdapter extends RecyclerView.Adapter<RegisterUserAdapter.MyViewHolder> {

    private Context mContext;
    private List<Client> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, sname, email;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            sname = (TextView) view.findViewById(R.id.surname);
            email = (TextView) view.findViewById(R.id.email);

        }
    }


    public RegisterUserAdapter(Context mContext, List<Client> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public RegisterUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);

        return new RegisterUserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RegisterUserAdapter.MyViewHolder holder, int position) {


        Client album = albumList.get(position);
        //name, surname , email;
        holder.name.setText(album.getName());
        holder.sname.setText(album.getSname());
        holder.email.setText(album.getEmail());
        // loading album cover using Glide library
        //Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

       /* holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
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
        return albumList.size();
    }
}
