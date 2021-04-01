package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String user_ID;
    private String user_name;
    private String user_email_address;
    private String name;
    private String password;
    private String user_age;
    private String user_game_tag;

    public UserModel(){
    }

    public UserModel(String user_ID, String user_name){
        this.user_ID = user_ID;
        this.user_name = user_name;
    }

    public UserModel(String user_ID, String user_name, String user_email_address, String name, String password, String user_age, String user_game_tag){
        this.user_ID = user_ID;
        this.user_name = user_name;
        this.user_email_address = user_email_address;
        this.name = name;
        this.password = password;
        this.user_age = user_age;
        this.user_game_tag = user_game_tag;
    }

    protected UserModel(Parcel in) {
        user_ID = in.readString();
        user_name = in.readString();
        user_email_address = in.readString();
        name = in.readString();
        password = in.readString();
        user_age = in.readString();
        user_game_tag = in.readString();
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

    public String getUser_Name() {
        return user_name;
    }

    public void setUser_Name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_Email_Address() {
        return user_email_address;
    }

    public void setUser_Email_Address(String user_email_address) {
        this.user_email_address = user_email_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_Age() {
        return user_age;
    }

    public void setUser_Age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_Game_Tag() {
        return user_game_tag;
    }

    public void setGame_Tag(String user_game_tag) {
        this.user_game_tag = user_game_tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_ID);
        dest.writeString(user_name);
        dest.writeString(user_email_address);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(user_age);
        dest.writeString(user_game_tag);
    }
}