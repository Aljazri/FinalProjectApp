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

//Adapter for search other user activity to load profiles of other users

public class ProfilesListAdapter extends RecyclerView.Adapter<ProfilesListAdapter.ProfilesViewHolder> {

    ArrayList<Profiles> profilesList;
    Context context;

    public ProfilesListAdapter(Context context, ArrayList<Profiles> profilesList){
        this.profilesList = profilesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_other_user_item,parent,false);
        return new ProfilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilesViewHolder holder, final int position) {

        //setting on click listener for profile list item
        holder.userName.setText(profilesList.get(position).getUsername());
        holder.email.setText(profilesList.get(position).getUserEmail());
        Picasso.get().load(profilesList.get(position).getProfileImage()).into(holder.profileImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ViewOthersProfileActivity.class);
                intent.putExtra("Uid",profilesList.get(position).getUid());
                intent.putExtra("firstName",profilesList.get(position).getFirstName());
                intent.putExtra("lastName",profilesList.get(position).getLastName());
                intent.putExtra("name",profilesList.get(position).getUsername());
                intent.putExtra("email",profilesList.get(position).getUserEmail());
                intent.putExtra("image",profilesList.get(position).getProfileImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return profilesList.size();
    }

    public class ProfilesViewHolder extends RecyclerView.ViewHolder{
        TextView userName,email;
        ImageView profileImage;
        View view;
        public ProfilesViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_holder);
            email = itemView.findViewById(R.id.user_email_holder);
            profileImage = itemView.findViewById(R.id.profile_image_holder);
            view = itemView;
        }
    }
}
