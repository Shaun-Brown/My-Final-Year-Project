package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class GameModel implements Parcelable {

    private String game_id, game_name, game_description, game_image;

    public GameModel(){
    }

    public GameModel (String game_id, String game_name, String game_description, String game_image) {
        this.game_id = game_id;
        this.game_name = game_name;
        this.game_description = game_description;
        this.game_image = game_image;
    }

    protected GameModel(Parcel in) {
        game_id = in.readString();
        game_name = in.readString();
        game_description = in.readString();
        game_image = in.readString();
    }

    public static final Creator<GameModel> CREATOR = new Creator<GameModel>() {
        @Override
        public GameModel createFromParcel(Parcel in) {
            return new GameModel(in);
        }

        @Override
        public GameModel[] newArray(int size) {
            return new GameModel[size];
        }
    };

    @Exclude
    public String getGame_ID() { return game_id;}

    public void setGame_ID(String game_id) { this.game_id = game_id;}

    public String getGame_Name() {
        return game_name;
    }

    public void setGame_Name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_Description() {
        return game_description;
    }

    public void setGame_Description(String description) {
        this.game_description = description;
    }

    public String getGame_Image() {
        return game_image;
    }

    public void setGame_Image(String game_image) {
        this.game_image = game_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(game_id);
        dest.writeString(game_name);
        dest.writeString(game_description);
        dest.writeString(game_image);
    }
}
