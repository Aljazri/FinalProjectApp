package com.example.finalprojectapp;

//Photo class for loading photos of users

public class Photo {

    private String userImage;

    public Photo() {
    }

    public Photo(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setImgUrl(String imgUrl) {
        this.userImage = imgUrl;
    }
}
