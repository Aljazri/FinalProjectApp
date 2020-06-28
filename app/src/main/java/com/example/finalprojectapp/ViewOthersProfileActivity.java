package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

// Viewing profile of other user

public class ViewOthersProfileActivity extends AppCompatActivity implements View.OnClickListener{


    // Instantiating variables
    ImageView otherImageView;
    TextView otherFirstNameHolder,otherLastNameHolder,otherNameHolder,otherEmailHolder;
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
        ref = userInformation.getReference().child("UsersInformation2");
        uid = getIntent().getStringExtra("Uid");

        // Creating other user profile
        otherImageView = findViewById(R.id.others_profile_image);
        otherFirstNameHolder = findViewById(R.id.other_first_name_text_view);
        otherLastNameHolder = findViewById(R.id.other_last_name_text_view);
        otherNameHolder = findViewById(R.id.other_user_name_text_view);
        otherEmailHolder = findViewById(R.id.others_email_text_view);
        viewOthersPhotoButton = findViewById(R.id.view_others_photo_button);

        Picasso.get().load(getIntent().getStringExtra("image")).into(otherImageView);
        otherFirstNameHolder.setText(getIntent().getStringExtra("firstName"));
        otherLastNameHolder.setText(getIntent().getStringExtra("lastName"));
        otherNameHolder.setText(getIntent().getStringExtra("name"));
        otherEmailHolder.setText(getIntent().getStringExtra("email"));

        viewOthersPhotoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // Viewing photos of other users
            case R.id.view_others_photo_button:
                Intent intent = new Intent(ViewOthersProfileActivity.this, ViewOtherUsersPhotoActivity.class);
                intent.putExtra("Uid",uid);
                startActivity(intent);
                break;
        }
    }
}
