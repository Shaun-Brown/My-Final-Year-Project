package com.example.myfinalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinalyearproject.Models.GameModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGamePost extends AppCompatActivity {

    private static final String TAG = "CreatePost";
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private GameModel games;
    private CircleImageView gPostImage;
    private EditText editPostName, editPostDesc;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private String user_ID;
    private String game_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_layout);

        gPostImage = findViewById(R.id.postImageView);
        editPostName = findViewById(R.id.postNameEdit);
        editPostDesc = findViewById(R.id.postDescEdit);
        Button btnCreate = findViewById(R.id.createPostBtn);

        firebaseDB = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dataRef = firebaseDB.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        user_ID = auth.getCurrentUser().getUid();

        final Intent intent = getIntent();
        games = intent.getParcelableExtra("games");

        game_ID = games.getGame_ID();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createPost();
            }
        });

        gPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void createPost() {
        final String postN = editPostName.getText().toString().trim();
        final String postD = editPostDesc.getText().toString().trim();
        if (TextUtils.isEmpty(postN)) {
            toastMessage("Enter a name");
            return;
        }

        Map<String, Object> gamePosts = new HashMap<>();
        gamePosts.put("game_post_name", postN);
        gamePosts.put("game_post_description", postD);
        gamePosts.put("user_ID", user_ID);
        gamePosts.put("game_ID", game_ID);
        gamePosts.put("game_post_timestamp", ServerValue.TIMESTAMP);
        final String key = dataRef.child("games").child(game_ID).child("game_posts").push().getKey();
        DatabaseReference newGamePostRef = dataRef.child("games").child(game_ID).child("game_posts").child(key);
        newGamePostRef.setValue(gamePosts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: id = " + getTaskId());
                        uploadImage(key);
                        finish();
                    }
                });

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                gPostImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String id) {
        if (filePath != null) {

            StorageReference ref = storageRef.child("game_posts/" + id + "/image.jpg");

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            toastMessage("Image uploaded!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastMessage("Failed" + e.getMessage());
                        }
                    });
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}