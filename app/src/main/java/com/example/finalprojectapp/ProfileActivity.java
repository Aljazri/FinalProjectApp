package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    TextView userNameHolder,userEmailHolder;
    FloatingActionButton searchOtherButton,viewUserPhotoButton;

    FirebaseDatabase userInformation;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mAuth = FirebaseAuth.getInstance();
        userInformation = FirebaseDatabase.getInstance();
        ref = userInformation.getReference().child("UsersInformation1");

        imageView = findViewById(R.id.user_profile_image);
        userNameHolder = findViewById(R.id.user_name_text_view);
        userEmailHolder = findViewById(R.id.email_text_view);
        searchOtherButton = findViewById(R.id.search_other_button);
        viewUserPhotoButton = findViewById(R.id.view_photo_button);

        ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageUrl = snapshot.child("profileImage").getValue().toString();
                    String userName = snapshot.child("username").getValue().toString();
                    String email = snapshot.child("userEmail").getValue().toString();

                    Picasso.get().load(imageUrl).into(imageView);
                    userNameHolder.setText(userName);
                    userEmailHolder.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewUserPhotoButton.setOnClickListener(this);
        searchOtherButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.view_photo_button:
                startActivity(new Intent(ProfileActivity.this,ViewUserPhoto.class));
                break;
            case R.id.search_other_button:
                startActivity(new Intent(ProfileActivity.this,SearchOtherUser.class));
                break;
        }
    }
}
