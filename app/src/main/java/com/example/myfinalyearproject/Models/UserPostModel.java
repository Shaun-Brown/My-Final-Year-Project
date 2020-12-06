package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserPostModel implements Parcelable {

    private String user_post_ID;
    private String user_post_name;
    private UserModel user;
    private @ServerTimestamp Date postTimestamp;

    public UserPostModel(){
    }

    public UserPostModel(UserModel user){
        this.user = user;
    }

    public UserPostModel(String user_post_ID, String user_post_name, UserModel user, Date postTimestamp){
        this.user_post_ID = user_post_ID;
        this.user_post_name = user_post_name;
        this.user = user;
        this.postTimestamp = postTimestamp;
    }

    protected UserPostModel(Parcel in) {
        user_post_ID = in.readString();
        user_post_name = in.readString();
    }

    public static final Creator<UserPostModel> CREATOR = new Creator<UserPostModel>() {
        @Override
        public UserPostModel createFromParcel(Parcel in) {
            return new UserPostModel(in);
        }

        @Override
        public UserPostModel[] newArray(int size) {
            return new UserPostModel[size];
        }
    };

    public String getUser_Post_ID() {
        return user_post_ID;
    }

    public String getUser_Post_Name(){
        return user_post_name;
    }

    public void setUser_Post_Name(String user_post_name) { this.user_post_name = user_post_name; }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user){
        this.user = user;
    }

    public Date getPost_Timestamp(){
        return postTimestamp;
    }

    public void setPost_Timestamp(Date postTimestamp){
        this.postTimestamp = postTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
