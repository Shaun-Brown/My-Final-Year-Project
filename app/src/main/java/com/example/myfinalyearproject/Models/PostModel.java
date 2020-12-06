package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class PostModel implements Parcelable {

    private String post_ID;
    private String post_description;
    private String post_name;

    public PostModel(){
    }

    public PostModel(String post_ID, String post_description, String post_name){
        this.post_ID = post_ID;
        this.post_description = post_description;
        this.post_name = post_name;
    }

    protected PostModel(Parcel in) {
        post_ID = in.readString();
        post_description = in.readString();
        post_name = in.readString();
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

    public String getPost_ID() {
        return post_ID;
    }

    public void setPost_ID(String post_ID) {
        this.post_ID = post_ID;
    }

    public String getPost_Description(){
        return post_description;
    }

    public void setPost_Description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_Name(){
        return post_name;
    }

    public void setPost_Name(String post_name) { this.post_name = post_name; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post_ID);
        dest.writeString(post_description);
        dest.writeString(post_name);
    }
}
