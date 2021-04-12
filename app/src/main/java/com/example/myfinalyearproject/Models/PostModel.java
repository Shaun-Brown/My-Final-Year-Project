package com.example.myfinalyearproject.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class PostModel implements Parcelable {

    private String user_post_ID, user_post_name, user_ID, user_name, user_post_timestamp;
    private Uri user_img;

    public PostModel(){
    }

    public PostModel(String user_post_name, String user_ID, String user_name){
        this.user_post_name = user_post_name;
        this.user_ID = user_ID;
        this.user_name = user_name;
    }

    protected PostModel(Parcel in) {
        user_post_ID = in.readString();
        user_post_name = in.readString();
        user_ID = in.readString();
        user_name = in.readString();
        user_post_timestamp = in.readString();
        user_img = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    public String getUser_Post_ID() {
        return user_post_ID;
    }

    public void setUser_Post_ID(String user_post_ID) { this. user_post_ID = user_post_ID; }

    public String getUser_Post_Name(){
        return user_post_name;
    }

    public void setUser_Post_Name(String user_post_name) { this.user_post_name = user_post_name; }

    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_Name() {
        return user_name;
    }

    public Uri getUser_Image() {
        return user_img;
    }

    public String getUser_Post_Timestamp(){
        return user_post_timestamp;
    }

    public void setUser_Post_Timestamp(String user_post_timestamp){
        this.user_post_timestamp = user_post_timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_post_ID);
        dest.writeString(user_post_name);
        dest.writeString(user_ID);
        dest.writeString(user_name);
        dest.writeString(user_post_timestamp);
        dest.writeParcelable(user_img, flags);
    }
}
