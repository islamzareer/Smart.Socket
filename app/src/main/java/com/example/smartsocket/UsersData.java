package com.example.smartsocket;

public class UsersData {
    private String userID;
    private String username;
    private String txt_email;
    private String txt_mobile;
    private String imageURL;

    public UsersData(String userID, String txt_password, String txt_email, String txt_mobile, String imageURL) {
        this.userID = userID;
        this.username = txt_password;
        this.txt_email = txt_email;
        this.txt_mobile = txt_mobile;
        this.imageURL = imageURL;
    }

    public UsersData() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTxt_email() {
        return txt_email;
    }

    public void setTxt_email(String txt_email) {
        this.txt_email = txt_email;
    }

    public String getTxt_mobile() {
        return txt_mobile;
    }

    public void setTxt_mobile(String txt_mobile) {
        this.txt_mobile = txt_mobile;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
