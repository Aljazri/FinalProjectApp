package com.example.finalprojectapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewLargePhoto extends AppCompatActivity {

    TextView largeComment;
    ImageView largeImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_photo_view);
        largeComment = findViewById(R.id.large_comment);
        largeImageView = findViewById(R.id.large_image_view);

        largeComment.setText(getIntent().getStringExtra("comment"));
        Picasso.get().load(getIntent().getStringExtra("imageUrl")).into(largeImageView);

    }
}
