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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;



// *********Activity Description: Activity to upload photos of current user***********//


public class AddUserPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    // instantiating variables

    AlertDialogBox db;
    public Uri imgUri = null;
    StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    StorageTask uploadTask;

    EditText comment;
    ImageView postImage,postButton;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_photo);

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages");//storage reference to all images
        mAuth =  FirebaseAuth.getInstance();//Authentication object

        firebaseDatabase = FirebaseDatabase.getInstance();//Database object
        db = new AlertDialogBox(AddUserPhotoActivity.this);//Dialog box to show progress of image upload
        comment = (EditText) findViewById(R.id.comment);//photo comment

        postImage = findViewById(R.id.image_to_add);
        postButton = findViewById(R.id.post_button);
        back = findViewById(R.id.back);

        postImage.setOnClickListener(this);
        postButton.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void chooseImage(){

        //function to choose image from user phone storage

        Intent chooseIntent = new Intent();
        chooseIntent.setType("image/*");
        chooseIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooseIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //handling result of choosing image

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            postImage.setImageURI(imgUri);
        }
    }

    public String getExtension(Uri uri){

        // generating extension of uploading image

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadImage(){
        final String  commentString = comment.getText().toString();

        if(commentString.length()==0){
            comment.setError("Put some comment to photo");
            comment.requestFocus();
        }else if(imgUri==null){
            Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
        }else{

            db.startDialogBox();//Starting dialog box
            final StorageReference filePath =  mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imgUri));
            uploadTask = filePath.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    //uploading image in the storage

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUrl = task.getResult();
                    String imagePath = downloadUrl.toString();

                    DatabaseReference ref = firebaseDatabase.getReference().child("UsersImages").child(mAuth.getCurrentUser().getUid());
                    final String newPhoto = ref.push().getKey();

                    HashMap<String,Object> imageData = new HashMap<>();
                    imageData.put("userImage",imagePath);
                    imageData.put("comment",commentString);

                    ref.child(newPhoto).setValue(imageData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //saving image url to database

                            if(task.isSuccessful()){

                                // if image uploading is successful then back to previous activity

                                db.dismissDialog();//dismissing alert box
                                Toast.makeText(AddUserPhotoActivity.this, "task complete", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(AddUserPhotoActivity.this, ViewCurrentUserPhotoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{

                                //If image upload is unsuccessful

                                db.dismissDialog();//dismissing dialog box

                                //unsuccessful image upload message
                                Toast.makeText(AddUserPhotoActivity.this, "can't post the photo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image_to_add:

                //choose image button
                chooseImage();
                break;
            case R.id.post_button:

                // uploading image button

                uploadImage();
                break;
            case R.id.back:

                //button to go back to previous activity

                startActivity(new Intent(AddUserPhotoActivity.this, ViewCurrentUserPhotoActivity.class));
                finish();
        }
    }
}