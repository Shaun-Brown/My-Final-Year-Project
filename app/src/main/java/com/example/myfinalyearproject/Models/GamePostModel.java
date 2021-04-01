package com.example.myfinalyearproject.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class GamePostModel implements Parcelable {

    private String game_post_ID, game_post_name, game_post_description, user_ID, game_ID;
    private Uri game_post_image;
    private Long game_post_timestamp;

    public GamePostModel(){
    }

    public GamePostModel(String game_post_ID, String game_post_name, String game_post_description, String user_ID, String game_ID){
        this.game_post_ID = game_post_ID;
        this.game_post_name = game_post_name;
        this.game_post_description = game_post_description;
        this.user_ID = user_ID;
        this.game_ID = game_ID;
    }

    protected GamePostModel(Parcel in) {
        game_post_ID = in.readString();
        game_post_name = in.readString();
        game_post_description = in.readString();
        user_ID = in.readString();
        game_ID = in.readString();
        game_post_image = in.readParcelable(Uri.class.getClassLoader());
        game_post_timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<GamePostModel> CREATOR = new Creator<GamePostModel>() {
        @Override
        public GamePostModel createFromParcel(Parcel in) {
            return new GamePostModel(in);
        }

        @Override
        public GamePostModel[] newArray(int size) {
            return new GamePostModel[size];
        }
    };

    public String getGame_Post_ID() {
        return game_post_ID;
    }

    public void setGame_Post_ID(String game_post_ID) {
        this.game_post_ID = game_post_ID;
    }

    public String getGame_Post_Name(){
        return game_post_name;
    }

    public void setGame_Post_Name(String post_name) { this.game_post_name = post_name; }

    public String getGame_Post_Description(){
        return game_post_description;
    }

    public void setGame_Post_Description(String game_post_description) {
        this.game_post_description = game_post_description;
    }

    public Uri getGame_Post_Image(){
        return game_post_image;
    }

    public void setGame_Post_Image(Uri game_post_image) {
        this.game_post_image = game_post_image;
    }

    public String getUser_ID(){
        return user_ID;
    }

    public String getGame_ID() { return game_ID; }

    public Long getGame_Post_Timestamp() {
        return game_post_timestamp;
    }

    public void setGame_Post_Timestamp(Long game_post_timestamp) {
        this.game_post_timestamp = game_post_timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(game_post_ID);
        dest.writeString(game_post_name);
        dest.writeString(game_post_description);
        dest.writeString(user_ID);
        dest.writeString(game_ID);
        dest.writeParcelable(game_post_image, flags);
        dest.writeLong(game_post_timestamp);
    }
}
