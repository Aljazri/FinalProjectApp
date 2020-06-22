package com.example.finalprojectapp;

public class Profiles {
    String profileImage,userEmail,userPassword,username,Uid;


    public Profiles() {
    }

    public Profiles(String profileImage, String userEmail, String userPassword, String username,String Uid) {
        this.profileImage = profileImage;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.username = username;
        this.Uid = Uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
