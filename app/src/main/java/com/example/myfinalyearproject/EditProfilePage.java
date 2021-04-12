package com.example.myfinalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfinalyearproject.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilePage extends AppCompatActivity {

    private static final String TAG = "EditProfilePage";
    private DatabaseReference userRef;
    private CircleImageView usrImg;
    private TextView usrName, usrEmail, name, pass, usrAge, usrGameTags;
    private String userID;
    private FirebaseUser fUser;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);

        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDB.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        userID = fUser.getUid();
        userRef = dataRef.child("users").child(userID);

        usrImg = findViewById(R.id.userImg);
        usrName = findViewById(R.id.editUserName);
        usrEmail = findViewById(R.id.editUserEmail);
        name = findViewById(R.id.editNameText);
        pass = findViewById(R.id.editPasswordText);
        usrAge = findViewById(R.id.editAgeText);
        usrGameTags = findViewById(R.id.editTagsText);
        Button saveEditBtn = findViewById(R.id.saveEditProfile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        usrImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1000);
            }
        });

        currentData();
    }

    private void editProfile(){

        final String usrN = usrName.getText().toString();
        final String usrE = usrEmail.getText().toString();
        final String nme = name.getText().toString();
        final String passW = pass.getText().toString();
        final String usrA = usrAge.getText().toString();
        final String gTag = usrGameTags.getText().toString();

        if(fUser.isEmailVerified()) {
            toastMessage("Enter a email address");
            return;
        }

        final Map<String, Object> user = new HashMap<>();
        user.put("user_name", usrN);
        user.put("user_email_address", usrE);
        user.put("name", nme);
        user.put("user_password", passW);
        user.put("user_age", usrA);
        user.put("user_game_tag", gTag);

        userRef.updateChildren(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        toastMessage("Success!");
                        fUser.updateEmail(usrE);
                        fUser.updatePassword(passW);
                        if (!usrN.equals(fUser.getDisplayName())) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usrN)
                                    .build();

                            fUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User Profile Updated");
                                                toastMessage("User Name Updated");
                                            }
                                        }
                                    });
                        }
                        finish();
                        Intent intent = new Intent(EditProfilePage.this, UserAccountPage.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        toastMessage("Error!");
                    }
                });
    }


    private void currentData() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StorageReference imgStore = storageRef.child("users/"+ userID +"/profile.jpg");
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

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imgUri = data.getData();
                handleUpload(imgUri);
            }
        }
    }

    private void handleUpload(Uri imgUri){

        final StorageReference imgRef = storageRef.child("users/"+ userID +"/profile.jpg");
        imgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toastMessage("Image Uploaded");
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(usrImg);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage("Didn't upload");
            }
        });
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imgUri)
                .build();

        fUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            toastMessage("Success!");
                        }
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
            finish();
            return true;
        } else if (item.getItemId() == R.id.friendListIcon) {
            Intent intent = new Intent(EditProfilePage.this, FriendListPage.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}