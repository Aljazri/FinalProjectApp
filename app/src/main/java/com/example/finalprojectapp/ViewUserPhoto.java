package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
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


public class ViewUserPhoto extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<Photo> photoList;
    RecyclerViewPhotoAdapter adapter;
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

        ref = FirebaseDatabase.getInstance().getReference();

        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Photo photo = dataSnapshot.getValue(Photo.class);
                    photoList.add(photo);
                }
                adapter = new RecyclerViewPhotoAdapter(ViewUserPhoto.this,photoList);
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
            startActivity(new Intent(ViewUserPhoto.this,AddUserPhotoActivity.class));
            finish();
        }
    }
}
