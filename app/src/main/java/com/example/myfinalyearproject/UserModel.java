package com.example.myfinalyearproject;

public class UserModel {

    private int id;
    private String userName;
    private String password;
    private String name;
    private String emailAddress;

    public UserModel(){

    }

    public UserModel(String userName, String emailAddress, String name){
        this.userName = userName;
        this.name = name;
        this.emailAddress = emailAddress;
    }

//    public UserModel(String userName, String emailAddress, String name) {
//    }

    public int getID(){
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}