package com.example.finalprojectapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Activity Desc : Activity to view photos of other users

public class ViewOtherUsersPhotoActivity extends AppCompatActivity{

    // Instantiating users
    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<Photo> photoList;
    OtherUsersPhotoListAdapter adapter;
    String uId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_other_user_photo);

        uId = getIntent().getStringExtra("Uid");
        recyclerView = findViewById(R.id.recycler_view_for_others_photos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoList = new ArrayList<Photo>();

        // making list view of other user photos
        ref = FirebaseDatabase.getInstance().getReference().child("UsersImages");

        ref.child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Photo photo = dataSnapshot.getValue(Photo.class);
                    photoList.add(photo);
                }
                adapter = new OtherUsersPhotoListAdapter(ViewOtherUsersPhotoActivity.this,photoList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
