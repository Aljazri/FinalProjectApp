package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUser extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;

    EditText loginUserEmail,loginUserPass;
    TextView redirectToRegister;
    Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        mAuth = FirebaseAuth.getInstance();

        loginUserEmail = findViewById(R.id.login_email);
        loginUserPass = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        redirectToRegister = findViewById(R.id.register_redirect);

        redirectToRegister.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_redirect:
                startActivity(new Intent(LoginUser.this,RegisterNewUserActivity.class));
                break;
            case R.id.login_button:
                loginUser();
                break;
        }
    }

    private void loginUser() {

        String loginUserEmailString = loginUserEmail.getText().toString();
        String loginUserPassString = loginUserPass.getText().toString();

        mAuth.signInWithEmailAndPassword(loginUserEmailString,loginUserPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UsersInformation");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Intent intent = new Intent(LoginUser.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(LoginUser.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
