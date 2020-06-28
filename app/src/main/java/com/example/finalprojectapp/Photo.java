package com.example.finalprojectapp;

//Photo class for loading photos of users

public class Photo {

    private String userImage;
    private String comment;

    public Photo() {
    }

    public Photo(String userImage, String comment) {
        this.userImage = userImage;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
