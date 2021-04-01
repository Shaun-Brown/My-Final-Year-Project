package com.example.myfinalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myfinalyearproject.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity {

    private FirebaseDatabase firebaseDB;
    private DatabaseReference dataRef;
    private DatabaseReference friendRef;
    private FirebaseAuth auth;
    private ArrayList<UserModel> friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_layout);

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();
        friendRef = dataRef.child("users").child(auth.getCurrentUser().getUid()).child("friends");

        FriendsList();
    }

    private void FriendsList(){
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}