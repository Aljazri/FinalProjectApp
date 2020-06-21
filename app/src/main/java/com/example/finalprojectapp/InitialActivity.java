package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button initialToLoginRedirect,getInitialToRegisterRedirect;

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser!=null){
            Intent intent= new Intent(InitialActivity.this, ProfileActivity.class);
            //intent.putExtra("Key",currentUser.getUid());
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_activity);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        initialToLoginRedirect = findViewById(R.id.initial_to_login);
        getInitialToRegisterRedirect = findViewById(R.id.initial_to_register);

        initialToLoginRedirect.setOnClickListener(this);
        getInitialToRegisterRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.initial_to_login:
                startActivity(new Intent(InitialActivity.this,LoginUser.class));
                finish();
                break;
            case R.id.initial_to_register:
                startActivity(new Intent(InitialActivity.this,RegisterNewUserActivity.class));
                finish();
                break;
        }
    }
}
