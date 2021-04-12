package com.example.myfinalyearproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageModel implements Parcelable {

    String message_ID, message_name, sender_ID, receiver_ID, receiver_name, message_timestamp, message_date;

    public MessageModel(){

    }

    public MessageModel(String message_name, String sender_ID, String receiver_ID, String receiver_name){
        this.message_name = message_name;
        this.sender_ID = sender_ID;
        this.receiver_ID = receiver_ID;
        this.receiver_name = receiver_name;
    }

    protected MessageModel(Parcel in) {
        message_name = in.readString();
        sender_ID = in.readString();
        receiver_ID = in.readString();
        receiver_name = in.readString();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getMessage_ID(){
        return message_ID;
    }

    public void setMessage_ID(String message_ID){
        this.message_ID = message_ID;
    }

    public String getMessage_Name(){
        return message_name;
    }

    public void setMessage_Name(String message_name){
        this.message_name = message_name;
    }

    public String getSender_ID(){
        return sender_ID;
    }

    public void setSender_ID(String sender_ID){
        this.sender_ID = sender_ID;
    }

    public String getReceiver_ID(){
        return receiver_ID;
    }

    public void setReceiver_ID(String receiver_ID){
        this.receiver_ID = receiver_ID;
    }

    public String getReceiver_Name(){
        return receiver_name;
    }

    public void setReceiver_Name(String receiver_name){
        this.receiver_name = receiver_name;
    }

    public String getMessage_Timestamp(){
        return message_timestamp;
    }

    public void setMessage_Timestamp(String message_timestamp){
        this.message_timestamp = message_timestamp;
    }

    public String getMessage_Date(){
        return message_date;
    }

    public void setMessage_Date(String message_date){
        this.message_date = message_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message_name);
        dest.writeString(sender_ID);
        dest.writeString(receiver_ID);
        dest.writeString(receiver_name);
    }
}
