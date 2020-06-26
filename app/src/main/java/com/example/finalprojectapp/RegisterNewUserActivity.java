package com.example.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

//Activity Description : Activity to register new user

public class RegisterNewUserActivity extends AppCompatActivity implements View.OnClickListener{

    //instantiating variables
    AlertDialogBox db;
    public Uri imgUri = null;
    ImageView imageToUpload;
    StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase1,firebaseDatabase2;
    StorageTask uploadTask;

    EditText firstName,lastName,userName,userEmail,userPassword,confirmPassword;
    Button upload_Button;
    TextView redirectToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new_user);

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages");
        mAuth =  FirebaseAuth.getInstance();
        db = new AlertDialogBox(RegisterNewUserActivity.this);

        firebaseDatabase1 = FirebaseDatabase.getInstance();
        firebaseDatabase2 = FirebaseDatabase.getInstance();

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        userName = (EditText) findViewById(R.id.user_name);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        upload_Button = (Button) findViewById(R.id.upload_button);
        imageToUpload = (ImageView)findViewById(R.id.image);
        redirectToLogin = findViewById(R.id.login_redirect);

        imageToUpload.setOnClickListener(this);
        upload_Button.setOnClickListener(this);
        redirectToLogin.setOnClickListener(this);
    }

    public void chooseImage(){

        //Choose image from phone storage

        Intent chooseIntent = new Intent();
        chooseIntent.setType("image/*");
        chooseIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooseIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //handling result of choosing image
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            imageToUpload.setImageURI(imgUri);
        }
    }

    public String getExtension(Uri uri){

        // creating extension of image selected

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void registerUser(){

        // Registering new user

        String firstNameString = firstName.getText().toString();
        String lastNameString = lastName.getText().toString();
        String  userEmailString = userEmail.getText().toString();
        String userPassString = userPassword.getText().toString();
        String confirmPassString = confirmPassword.getText().toString();

        // validating entries

        if(firstNameString.isEmpty()){
            error(firstName,"first name is empty");
        }

        else if(lastNameString.isEmpty()){
            error(lastName,"last name is empty");
        }
        else if(userEmailString.isEmpty()){
            error(userEmail,"Email is empty");
        }
        else if(userPassString.isEmpty()){
            error(userPassword,"password is empty");
        }
        else if(confirmPassString.isEmpty()){
            error(confirmPassword,"confirm password is empty");
        }
        else if(!userEmailString.contains("@")){
            error(userEmail,"enter a valid email address");
        }
        else if(userPassString.length()<7){
            error(userPassword,"password should be 7 digits long");
        }
        else if (!confirmPassString.equals(userPassString)){
            error(confirmPassword,"password doesn't match");
        }
        else if(imgUri == null){
            Toast.makeText(this, "Choose Profile Image", Toast.LENGTH_SHORT).show();
        }
        else {

            db.startDialogBox();

            // Creating new user
            mAuth.createUserWithEmailAndPassword(userEmailString,userPassString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        final StorageReference filePath =  mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imgUri));
                        uploadTask = filePath.putFile(imgUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {

                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUrl = task.getResult();
                                String imagePath = downloadUrl.toString();

                                DatabaseReference reference1 = firebaseDatabase1.getReference().child("UsersInformation1");
                                DatabaseReference reference2 = firebaseDatabase1.getReference().child("UsersInformation2");
                                final String key = reference2.push().getKey();

                                HashMap<String,Object> userData1 = new HashMap<>();
                                userData1.put("firstName",firstName.getText().toString());
                                userData1.put("lastName",lastName.getText().toString());
                                userData1.put("username",userName.getText().toString());
                                userData1.put("userEmail",userEmail.getText().toString());
                                userData1.put("userPassword",userPassword.getText().toString());
                                userData1.put("profileImage",imagePath);

                                HashMap<String,Object> userData2 = new HashMap<>();
                                userData2.put("Uid",mAuth.getCurrentUser().getUid());
                                userData1.put("firstName",firstName.getText().toString());
                                userData1.put("lastName",lastName.getText().toString());
                                userData2.put("username",userName.getText().toString());
                                userData2.put("userEmail",userEmail.getText().toString());
                                userData2.put("userPassword",userPassword.getText().toString());
                                userData2.put("profileImage",imagePath);

                                // saving user details

                                reference1.child(mAuth.getCurrentUser().getUid()).setValue(userData1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            db.dismissDialog();
                                            Intent intent= new Intent(RegisterNewUserActivity.this, CurrentUserProfileActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(RegisterNewUserActivity.this, "can't go to mainActivity", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                reference2.child(key).setValue(userData2);
                            }
                        });
                    }else{
                        db.dismissDialog();
                        Toast.makeText(RegisterNewUserActivity.this, "Unable To Register User", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    db.dismissDialog();

                    // handling errors
                    Toast.makeText(RegisterNewUserActivity.this, "Error: "+e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void error(EditText input, String str) {
        input.setError(str);
        input.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image:
                // choosing image as profile photo
                chooseImage();
                break;
            case R.id.upload_button:
                // register user
                registerUser();
                break;
            case R.id.login_redirect:
                //Go to login activity
                startActivity(new Intent(RegisterNewUserActivity.this, LoginUserActivity.class));
        }
    }
}