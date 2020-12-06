package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String user_ID;
    private String user_email_address;
    private String user_name;
    private String user_password;
    private String user_image;

    public UserModel(){
    }

    public UserModel(String uid, String user_email_address, String user_name, String user_image){
        this.user_ID = uid;
        this.user_email_address = user_email_address;
        this.user_name = user_name;
        this.user_image = user_image;
    }

    protected UserModel(Parcel in) {
        user_ID = in.readString();
        user_email_address = in.readString();
        user_password = in.readString();
        user_name = in.readString();
        user_image = in.readString();
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

    public String getUser_ID(){
        return user_ID;
    }

    public void setUser_ID(String uid){
        this.user_ID = uid;
    }

    public String getUser_Email_Address() {
        return user_email_address;
    }

    public void setUser_Email_Address(String user_email_address) {
        this.user_email_address = user_email_address;
    }

    public String getPassword() {
        return user_password;
    }

    public void setPassword(String password) {
        this.user_password = password;
    }

    public String getUser_Name() {
        return user_name;
    }

    public void setUser_Name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_Image(){
        return user_image;
    }

    public void setUser_Image(String user_image){
        this.user_image = user_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_ID);
        dest.writeString(user_email_address);
        dest.writeString(user_password);
        dest.writeString(user_name);
        dest.writeString(user_image);
    }
}