package com.example.finalprojectapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Activity description: Adapter for showing photos of other users
public class OtherUsersPhotoListAdapter extends RecyclerView.Adapter<OtherUsersPhotoListAdapter.MyViewHolder> {

    Context context;//Application context
    ArrayList<Photo> photos;//other user photo list

    public OtherUsersPhotoListAdapter(Context context, ArrayList<Photo> photos){
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating the layout
        return new MyViewHolder((LayoutInflater.from(context)
                .inflate(R.layout.view_other_each_photo,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //loading images of other users
        Picasso.get().load(photos.get(position).getUserImage()).into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView userPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.other_user_photo_holder);
        }
    }

}

