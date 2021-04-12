package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendModel implements Parcelable {

    private String user_ID, friend_user_ID, friend_user_name;

    public FriendModel(){}

    public FriendModel(String user_ID, String friend_user_ID, String friend_user_name){
        this.user_ID = user_ID;
        this.friend_user_ID = friend_user_ID;
        this.friend_user_name = friend_user_name;
    }

    protected FriendModel(Parcel in) {
        user_ID = in.readString();
        friend_user_ID = in.readString();
        friend_user_name = in.readString();
    }

    public static final Creator<FriendModel> CREATOR = new Creator<FriendModel>() {
        @Override
        public FriendModel createFromParcel(Parcel in) {
            return new FriendModel(in);
        }

        @Override
        public FriendModel[] newArray(int size) {
            return new FriendModel[size];
        }
    };

    public String getUser_ID() {
        return user_ID;
    }

    public String getFriend_User_ID() {
        return friend_user_ID;
    }

    public void setFriend_User_ID(String friend_user_ID) {
        this.friend_user_ID = friend_user_ID;
    }

    public String getFriend_User_Name() {
        return friend_user_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_ID);
        dest.writeString(friend_user_ID);
        dest.writeString(friend_user_name);
    }
}
