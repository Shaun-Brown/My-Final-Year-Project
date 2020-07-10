package com.example.myfinalyearproject;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String id;
    private String emailAddress;
    private String password;
    private String userName;
    private String phoneNumber;
    private String uImage;

    public UserModel(){

    }

    public UserModel(String emailAddress, String userName, String phoneNumber, String uImage){
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.uImage = uImage;
    }

    protected UserModel(Parcel in) {
        id = in.readString();
        emailAddress = in.readString();
        password = in.readString();
        userName = in.readString();
        phoneNumber = in.readString();
        uImage = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getID(){
        return id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getUserImage(){
        return uImage;
    }

    public void setUserImage(String uImage){
        this.uImage = uImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(emailAddress);
        dest.writeString(password);
        dest.writeString(userName);
        dest.writeString(phoneNumber);
        dest.writeString(uImage);
    }
}