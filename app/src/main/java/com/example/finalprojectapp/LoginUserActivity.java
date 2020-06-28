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


/* Activity Description: Login user Activity*/

public class LoginUserActivity extends AppCompatActivity implements View.OnClickListener{

    //instantiating variables

    FirebaseAuth mAuth;

    EditText loginUserEmail,loginUserPass;
    TextView redirectToRegister,invalidUserText;
    Button loginButton;
    AlertDialogBox dialogBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        mAuth = FirebaseAuth.getInstance();
        dialogBox = new AlertDialogBox(LoginUserActivity.this);

        invalidUserText = findViewById(R.id.invalid_user_text);
        loginUserEmail = findViewById(R.id.login_email);
        loginUserPass = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        redirectToRegister = findViewById(R.id.register_redirect);

        invalidUserText.setVisibility(View.INVISIBLE);
        redirectToRegister.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_redirect:
                //Go to register new user
                startActivity(new Intent(LoginUserActivity.this,RegisterNewUserActivity.class));
                break;
            case R.id.login_button:
                //go to login user
                loginUser();
                break;
        }
    }

    private void loginUser() {
        // logging in an already registered user
        String loginUserEmailString = loginUserEmail.getText().toString();
        String loginUserPassString = loginUserPass.getText().toString();

        if(loginUserEmailString.length()==0){
            loginUserEmail.setError("Please enter email id");
            loginUserEmail.requestFocus();
        }else if(loginUserPassString.length()<7){
            loginUserPass.setError("Password must be at least 7 digit long");
            loginUserPass.requestFocus();
        }
        else{

            dialogBox.startDialogBox();
            //Validating user
            mAuth.signInWithEmailAndPassword(loginUserEmailString,loginUserPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UsersInformation");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Go to current user profile if user validates
                                dialogBox.dismissDialog();
                                Intent intent = new Intent(LoginUserActivity.this, CurrentUserProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                dialogBox.dismissDialog();
                            }
                        });
                    }else{

                        //if user doesn't exist

                        dialogBox.dismissDialog();
                        invalidUserText.setVisibility(View.VISIBLE);
                        invalidUserText.setText("Invalid User, Please try again!!!");
                    }
                }
            });
        }

    }
}
