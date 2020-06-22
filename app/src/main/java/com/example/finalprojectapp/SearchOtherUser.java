package com.example.finalprojectapp;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

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

public class SearchOtherUser extends AppCompatActivity {

    DatabaseReference ref;

    SearchView searchBar;
    RecyclerView recyclerView;
    ArrayList<Profiles> profilesList;
    ProfilesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_other_user);

        ref = FirebaseDatabase.getInstance().getReference().child("UsersInformation2");

        recyclerView = findViewById(R.id.user_profiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchBar = findViewById(R.id.search_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ref != null){

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    profilesList = new ArrayList<>();

                    for(DataSnapshot ds: snapshot.getChildren()){

                        profilesList.add(ds.getValue(Profiles.class));

                    }
                    adapter = new ProfilesAdapter(SearchOtherUser.this,profilesList);
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(searchBar!=null){
            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String str) {

        ArrayList<Profiles> filteredList=new ArrayList<>();
        for(Profiles object:profilesList){

            if(object.getUsername().toLowerCase().contains(str.toLowerCase())){
                filteredList.add(object);
            }
        }

        ProfilesAdapter filterListAdapter = new ProfilesAdapter(this,filteredList);
        recyclerView.setAdapter(filterListAdapter);
    }
}
