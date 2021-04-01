package com.example.myfinalyearproject.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class OtherGameModel implements Parcelable {

    private String other_game_ID;
    private String other_game_name;
    private String other_game_description;
    private Uri other_game_image;
    private String user_ID;
    private String game_ID;
    private String game_name;
    private @ServerTimestamp Date other_game_timestamp;

    public OtherGameModel(){
    }

    public OtherGameModel(String other_game_ID, String other_game_name, String other_game_description, Uri other_game_image, String user_ID, String game_ID, String game_name){
        this.other_game_ID = other_game_ID;
        this.other_game_name = other_game_name;
        this.other_game_description = other_game_description;
        this.other_game_image = other_game_image;
        this.user_ID = user_ID;
        this.game_ID = game_ID;
        this.game_name = game_name;
    }

    protected OtherGameModel(Parcel in) {
        other_game_ID = in.readString();
        other_game_name = in.readString();
        other_game_description = in.readString();
        user_ID = in.readString();
        game_ID = in.readString();
        game_name = in.readString();
    }

    public static final Creator<OtherGameModel> CREATOR = new Creator<OtherGameModel>() {
        @Override
        public OtherGameModel createFromParcel(Parcel in) {
            return new OtherGameModel(in);
        }

        @Override
        public OtherGameModel[] newArray(int size) {
            return new OtherGameModel[size];
        }
    };

    public String getOther_Game_ID() {
        return other_game_ID;
    }

    public void setOther_Game_ID(String other_game_post_ID) {
        this.other_game_ID = other_game_post_ID;
    }

    public String getOther_Game_Name(){
        return other_game_name;
    }

    public void setOther_Game_Name(String other_game_post_name) { this.other_game_name = other_game_post_name; }

    public String getOther_Game_Description(){
        return other_game_description;
    }

    public void setOther_Game_Description(String other_game_description) {
        this.other_game_description = other_game_description;
    }

    public Uri getOther_Game_Image(){
        return other_game_image;
    }

    public void setOther_Game_Image(Uri other_game_image) {
        this.other_game_image = other_game_image;
    }

    public String getUser_ID(){
        return user_ID;
    }

    public String getGame_ID() { return game_ID; }

    public String getGame_Name(){ return game_name; }

    public @ServerTimestamp Date getOther_Game_Timestamp() {
        return other_game_timestamp;
    }

    public void setOther_Game_Timestamp(Date other_game_timestamp) {
        this.other_game_timestamp = other_game_timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(other_game_ID);
        dest.writeString(other_game_name);
        dest.writeString(other_game_description);
        dest.writeString(user_ID);
        dest.writeString(game_ID);
        dest.writeString(game_name);
    }

}
