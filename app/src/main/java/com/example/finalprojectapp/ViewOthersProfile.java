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

public class ViewOthersProfile extends AppCompatActivity implements View.OnClickListener{

    ImageView otherImageView;
    TextView otherNameHolder,otherEmailHolder;
    FloatingActionButton viewOthersPhotoButton;

    FirebaseDatabase userInformation;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_profile);

        mAuth = FirebaseAuth.getInstance();
        userInformation = FirebaseDatabase.getInstance();
        ref = userInformation.getReference().child("UsersInformation1");
        uid = getIntent().getStringExtra("Uid");

        otherImageView = findViewById(R.id.others_profile_image);
        otherNameHolder = findViewById(R.id.other_user_name_text_view);
        otherEmailHolder = findViewById(R.id.others_email_text_view);
        viewOthersPhotoButton = findViewById(R.id.view_others_photo_button);

        Picasso.get().load(getIntent().getStringExtra("image")).into(otherImageView);
        otherNameHolder.setText(getIntent().getStringExtra("name"));
        otherEmailHolder.setText(getIntent().getStringExtra("email"));

        viewOthersPhotoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.view_others_photo_button:
                Intent intent = new Intent(ViewOthersProfile.this,ViewOtherUsersPhoto.class);
                intent.putExtra("Uid",uid);
                startActivity(intent);
                break;
        }
    }
}
