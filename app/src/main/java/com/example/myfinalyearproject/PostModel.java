package com.example.myfinalyearproject;

import android.os.Parcel;
import android.os.Parcelable;

public class PostModel implements Parcelable {

    private String post;
    private String userID;
    private String userName;
    private String userImage;

    public PostModel(){

    }

    public PostModel(String post, String userID, String userName, String userImage){
        this.post = post;
        this.userID = userID;
        this.userName = userName;
        this.userImage = userImage;
    }

    protected PostModel(Parcel in) {
        post = in.readString();
        userID = in.readString();
        userName = in.readString();
        userImage = in.readString();
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String setUserID() { return userID; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public String getUserImage(){
        return userImage;
    }

    public void setUserImage(String uImage) {
        this.userImage = uImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post);
        dest.writeString(userName);
        dest.writeString(userImage);
    }
}
