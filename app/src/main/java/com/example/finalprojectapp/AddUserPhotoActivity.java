package com.example.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
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

public class AddUserPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    public Uri imgUri;
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

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages");
        mAuth =  FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        comment = (EditText) findViewById(R.id.comment);

        postImage = findViewById(R.id.image_to_add);
        postButton = findViewById(R.id.post_button);
        back = findViewById(R.id.back);

        postImage.setOnClickListener(this);
        postButton.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void chooseImage(){
        Intent chooseIntent = new Intent();
        chooseIntent.setType("image/*");
        chooseIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooseIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            postImage.setImageURI(imgUri);
        }
    }

    public String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadImage(){
        String  commentString = comment.getText().toString();

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

                DatabaseReference ref = firebaseDatabase.getReference().child(mAuth.getCurrentUser().getUid());
                final String newPhoto = ref.push().getKey();

                HashMap<String,Object> imageData = new HashMap<>();
                imageData.put("userImage",imagePath);

                ref.child(newPhoto).setValue(imageData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(AddUserPhotoActivity.this, "task complete", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(AddUserPhotoActivity.this, ViewUserPhoto.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(AddUserPhotoActivity.this, "can't post the photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image_to_add:
                chooseImage();
                break;
            case R.id.post_button:
                uploadImage();
                break;
            case R.id.back:
                startActivity(new Intent(AddUserPhotoActivity.this,ViewUserPhoto.class));
                finish();
        }
    }
}