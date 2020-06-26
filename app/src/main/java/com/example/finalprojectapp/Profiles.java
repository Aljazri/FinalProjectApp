package com.example.finalprojectapp;

//Profile class for loading profiles of users into recycler view used in search other user activity

public class Profiles {
    String profileImage,userEmail,userPassword,username,lastName,firstName,Uid;


    public Profiles() {
    }

    //constructor
    public Profiles(String profileImage, String userEmail, String userPassword,
                    String username,String lastName,String firstName,String Uid) {

        this.profileImage = profileImage;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.Uid = Uid;
    }

    //Getters and setters for variables
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
