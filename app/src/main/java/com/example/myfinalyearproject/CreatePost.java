package com.example.myfinalyearproject;

import com.google.firebase.database.core.Context;

public class CreatePost {

    private String postName;

    public CreatePost(String postName){
        this.postName = postName;
    }

    public CreatePost(Context context){}

    public String getPostName(){
        return postName;
    }

    public void setPostName(String gameName){
        this.postName = gameName;
    }

}
