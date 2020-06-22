package com.example.finalprojectapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfilesViewHolder> {

    ArrayList<Profiles> profilesList;
    Context context;

    public ProfilesAdapter(Context context, ArrayList<Profiles> profilesList){
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

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),ViewOthersProfile.class);
                intent.putExtra("Uid",profilesList.get(position).getUid());
                intent.putExtra("name",profilesList.get(position).getUsername());
                intent.putExtra("email",profilesList.get(position).getUserEmail());
                intent.putExtra("image",profilesList.get(position).getProfileImage());
                context.startActivity(intent);
            }
        });

        holder.userName.setText(profilesList.get(position).getUsername());
        holder.email.setText(profilesList.get(position).getUserEmail());
        Picasso.get().load(profilesList.get(position).getProfileImage()).into(holder.profileImage);

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
