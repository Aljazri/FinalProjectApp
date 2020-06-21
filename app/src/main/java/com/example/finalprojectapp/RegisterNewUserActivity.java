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

public class RegisterNewUserActivity extends AppCompatActivity implements View.OnClickListener{

    public Uri imgUri;
    ImageView imageToUpload;
    StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase1,firebaseDatabase2;
    StorageTask uploadTask;

    EditText userName,userEmail,userPassword;
    Button upload_Button;
    TextView redirectToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new_user);

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages");
        mAuth =  FirebaseAuth.getInstance();

        firebaseDatabase1 = FirebaseDatabase.getInstance();
        firebaseDatabase2 = FirebaseDatabase.getInstance();

        userName = (EditText) findViewById(R.id.user_name);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);

        upload_Button = (Button) findViewById(R.id.upload_button);
        imageToUpload = (ImageView)findViewById(R.id.image);
        redirectToLogin = findViewById(R.id.login_redirect);

        imageToUpload.setOnClickListener(this);
        upload_Button.setOnClickListener(this);
        redirectToLogin.setOnClickListener(this);
    }

    public void chooseImage(){
        Intent chooseIntent = new Intent();
        chooseIntent.setType("image/*");
        chooseIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooseIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            imageToUpload.setImageURI(imgUri);
        }
    }

    public String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadImage(){
        String  userEmailString = userEmail.getText().toString();
        String userPassString = userPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(userEmailString,userPassString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //final String userId = mAuth.getCurrentUser().getUid();
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
                            final String newUser = reference2.push().getKey();

                            HashMap<String,Object> userData = new HashMap<>();
                            userData.put("username",userName.getText().toString());
                            userData.put("userEmail",userEmail.getText().toString());
                            userData.put("userPassword",userPassword.getText().toString());
                            userData.put("profileImage",imagePath);
                            reference1.child(mAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterNewUserActivity.this, "task complete", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(RegisterNewUserActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(RegisterNewUserActivity.this, "can't go to mainActivity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            reference2.child(newUser).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }
                                    else{

                                    }
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(RegisterNewUserActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterNewUserActivity.this, "Error: "+e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image:
                chooseImage();
                break;
            case R.id.upload_button:
                uploadImage();
                break;
            case R.id.login_redirect:
                startActivity(new Intent(RegisterNewUserActivity.this,LoginUser.class));
        }
    }
}