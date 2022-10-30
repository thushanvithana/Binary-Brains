package com.example.artzoneapp;

public class AppUsers {
    private String Email;
    private String UID;
    private String UserType;
    private String userName;

    public AppUsers(){

    }

    public AppUsers(String email, String UID, String userType, String userName) {
        Email = email;
        this.UID = UID;
        UserType = userType;
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
