package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*Initial Activity where app starts */

public class InitialActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button initialToLoginRedirect,getInitialToRegisterRedirect;

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser!=null){

            //If user already logged in then go to user profile

            Intent intent= new Intent(InitialActivity.this, CurrentUserProfileActivity.class);
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

        initialToLoginRedirect = findViewById(R.id.initial_to_login);// Login button
        getInitialToRegisterRedirect = findViewById(R.id.initial_to_register);// Register new user button

        initialToLoginRedirect.setOnClickListener(this);
        getInitialToRegisterRedirect.setOnClickListener(this);
    }


    //If user not already logged in

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.initial_to_login:
                //Go to login page
                startActivity(new Intent(InitialActivity.this, LoginUserActivity.class));
                finish();
                break;
            case R.id.initial_to_register:
                // Go to register new user page
                startActivity(new Intent(InitialActivity.this,RegisterNewUserActivity.class));
                finish();
                break;
        }
    }
}
