package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Activity Desc : Activity for viewing photos of current user

public class ViewCurrentUserPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    // Instantiating variables

    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<Photo> photoList;
    CurrentUserPhotosListAdapter adapter;
    FloatingActionButton addPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo);

        recyclerView = findViewById(R.id.recycler_view_for_photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoList = new ArrayList<Photo>();
        addPhoto = findViewById(R.id.add_photo);

        addPhoto.setOnClickListener(this);

        ref = FirebaseDatabase.getInstance().getReference().child("UsersImages");


        // loading photos
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Photo photo = dataSnapshot.getValue(Photo.class);
                    photoList.add(photo);
                }
                adapter = new CurrentUserPhotosListAdapter(ViewCurrentUserPhotoActivity.this,photoList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_photo){
            // Go to add photo activity
            startActivity(new Intent(ViewCurrentUserPhotoActivity.this,AddUserPhotoActivity.class));
            finish();
        }
    }
}
