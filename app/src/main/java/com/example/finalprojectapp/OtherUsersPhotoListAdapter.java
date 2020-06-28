package com.example.finalprojectapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //loading images of other users
        holder.comment.setText(photos.get(position).getComment());
        Picasso.get().load(photos.get(position).getUserImage()).into(holder.userPhoto);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),ViewLargePhoto.class);
                intent.putExtra("imageUrl",photos.get(position).getUserImage());
                intent.putExtra("comment",photos.get(position).getComment());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return photos.size();}

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView userPhoto;
        TextView comment;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.other_user_photo_holder);
            comment = itemView.findViewById(R.id.other_comment);
            view = itemView;
        }
    }

}

