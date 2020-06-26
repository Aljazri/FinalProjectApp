package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

//*********************Activity for showing profile of current user******************

public class CurrentUserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    //Instantiating variables

    ImageView imageView;
    TextView firstNameHolder,lastNameHolder,userNameHolder,userEmailHolder;
    FloatingActionButton searchOtherButton,viewUserPhotoButton;

    FirebaseDatabase userInformation;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mAuth = FirebaseAuth.getInstance(); //Authenticating instance
        userInformation = FirebaseDatabase.getInstance();
        ref = userInformation.getReference().child("UsersInformation1");//Users database reference

        imageView = findViewById(R.id.user_profile_image);
        firstNameHolder = findViewById(R.id.first_name_text_view);
        lastNameHolder = findViewById(R.id.last_name_text_view);
        userNameHolder = findViewById(R.id.user_name_text_view);
        userEmailHolder = findViewById(R.id.email_text_view);
        searchOtherButton = findViewById(R.id.search_other_button);
        viewUserPhotoButton = findViewById(R.id.view_photo_button);

        ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            //Building user profile page

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageUrl = snapshot.child("profileImage").getValue().toString();
                    String firstName = snapshot.child("firstName").getValue().toString();
                    String lastName = snapshot.child("lastName").getValue().toString();
                    String userName = snapshot.child("username").getValue().toString();
                    String email = snapshot.child("userEmail").getValue().toString();

                    Picasso.get().load(imageUrl).into(imageView);
                    firstNameHolder.setText(firstName);
                    lastNameHolder.setText(lastName);
                    userNameHolder.setText(userName);
                    userEmailHolder.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing
            }
        });

        viewUserPhotoButton.setOnClickListener(this);
        searchOtherButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.view_photo_button:

                //Go to view user photos

                Intent intent = new Intent(CurrentUserProfileActivity.this, ViewCurrentUserPhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.search_other_button:

                //Go to search other users

                startActivity(new Intent(CurrentUserProfileActivity.this, SearchOtherUserActivity.class));
                break;
        }
    }
}
