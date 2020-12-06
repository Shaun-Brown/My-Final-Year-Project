package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    private static final String TAG = "CreatePost";
    private Button btnCreate;
    private EditText editPostName, editPostDesc;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private PostModel postM = new PostModel();
    private GameModel game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_layout);

        editPostName = findViewById(R.id.postNameEdit);
        editPostDesc = findViewById(R.id.postDescEdit);
        btnCreate = findViewById(R.id.createPostBtn);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        game = intent.getParcelableExtra("game");

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

    }

    private void createPost() {
        final String postN = editPostName.getText().toString();
        final String postD = editPostDesc.getText().toString();
        if (TextUtils.isEmpty(postN)) {
            toastMessage("Enter a name");
            return;
        }
        postM.setPost_Name(postN);
        postM.setPost_Description(postD);
        userId = auth.getCurrentUser().getUid();
        Map<String, Object> posts = new HashMap<>();
        posts.put("post_name", postN);
        posts.put("post_description", postD);
        posts.put("user_ID", userId);
        posts.put("game_ID", game.getID());
        fStore.collection("posts")
                .add(posts)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        finish();
                        Intent intent = new Intent(CreatePost.this, PostPage.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}