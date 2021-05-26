package com.example.myfinalyearproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinalyearproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccountPage extends AppCompatActivity {
    private static final String TAG = "UserAccountPage";


    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference dataRef;
    private FirebaseUser fUser;
    private FirebaseStorage storageRef;
    private CircleImageView usrImg;
    private TextView usrName, usrEmail, name, pass, usrAge, usrGameTags;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_page_layout);

        usrImg = findViewById(R.id.userImg);
        usrName = findViewById(R.id.userNameText);
        usrEmail = findViewById(R.id.userEmail);
        name = findViewById(R.id.name);
        pass = findViewById(R.id.password);
        usrAge = findViewById(R.id.age);
        usrGameTags = findViewById(R.id.userTags);
        FloatingActionButton editBtn = findViewById(R.id.editProfileBtn);



        firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance();
        userID = fUser.getUid();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAccountPage.this, EditProfilePage.class));
            }
        });

        showUserDetails();
    }

    private void showUserDetails(){

        DatabaseReference userRef = dataRef.child("users").child(userID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        StorageReference imgStore = storageRef.getReference("users/" + userID + "/profile.jpg");
                        imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(usrImg);
                            }
                        });
                        UserModel users = snapshot.getValue(UserModel.class);
                        usrName.setText(users.getUser_Name());
                        usrEmail.setText(users.getUser_Email_Address());
                        name.setText(users.getName());
                        pass.setText(users.getPassword());
                        usrAge.setText(users.getUser_Age());
                        usrGameTags.setText(users.getUser_Game_Tag());
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getDetails());
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
            onBackPressed();
            Intent intent = new Intent(UserAccountPage.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.friendListIcon) {
            Intent intent3 = new Intent(UserAccountPage.this, FriendListPage.class);
            startActivity(intent3);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
