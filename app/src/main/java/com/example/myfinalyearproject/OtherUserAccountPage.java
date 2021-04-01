package com.example.myfinalyearproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinalyearproject.Models.PostModel;
import com.example.myfinalyearproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserAccountPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference dataRef, userRef, friendRef;
    private FirebaseStorage storageRef;
    private PostModel pModel;
    private CircleImageView otherUsrImage;
    private TextView otherUsrName, otherUsrGameTags;
    private Button privateMessage, addFriend, unFriend;
    private String otherUserID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_account_layout);

        otherUsrImage = findViewById(R.id.otherUserImg);
        otherUsrName = findViewById(R.id.otherUserNameText);
        otherUsrGameTags = findViewById(R.id.otherUserTags);
        privateMessage = findViewById(R.id.privateMessage);
        addFriend = findViewById(R.id.addFriend);
        unFriend = findViewById(R.id.unFriend);

        Intent intent = getIntent();
        pModel = intent.getParcelableExtra("userPost");

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        otherUserID = pModel.getUser_ID();

        firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();
        storageRef = FirebaseStorage.getInstance();

        userRef = dataRef.child("users");
        friendRef = dataRef.child("friends");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(userID).child(otherUserID).exists()){
                    unFriend.setVisibility(View.INVISIBLE);
                    addFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addFriend();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                } else {
                    addFriend.setVisibility(View.INVISIBLE);
                    unFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFriend();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        privateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherUserAccountPage.this, PrivateMessaging.class);
                intent.putExtra("userPost", pModel);
                startActivity(intent);
            }
        });

        showOtherUserDetails();
    }

    private void removeFriend() {
        friendRef.child(userID).child(otherUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessage("Friend removed");
            }
        });
    }

    private void addFriend() {
        HashMap<String, Object> newFriend = new HashMap<>();
        newFriend.put("user_name", pModel.getUser_Name());
        friendRef.child(userID).child(otherUserID).setValue(newFriend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessage("Friend added");
            }
        });
    }

    private void showOtherUserDetails(){
        userRef.child(otherUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        StorageReference imgStore = storageRef.getReference("users/"+ otherUserID +"/profile.jpg");
                        imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(otherUsrImage);
                            }
                        });

                        UserModel user = snapshot.getValue(UserModel.class);
                        String uName = user.getUser_Name();
                        String uGT = user.getUser_Game_Tag();

                        otherUsrName.setText(uName);
                        otherUsrGameTags.setText(uGT);
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent3 = new Intent(OtherUserAccountPage.this, MainActivity.class);
            startActivity(intent3);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
