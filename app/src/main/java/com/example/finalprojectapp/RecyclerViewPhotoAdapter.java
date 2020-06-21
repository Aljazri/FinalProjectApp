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

public class RecyclerViewPhotoAdapter extends RecyclerView.Adapter<RecyclerViewPhotoAdapter.MyViewHolder> {

    Context context;
    ArrayList<Photo> photos;

    public RecyclerViewPhotoAdapter(Context context,ArrayList<Photo> photos){
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder((LayoutInflater.from(context).inflate(R.layout.view_each_photo,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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

            userPhoto = itemView.findViewById(R.id.user_photo_holder);
        }
    }

}

