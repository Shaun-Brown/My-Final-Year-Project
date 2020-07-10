package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePage extends AppCompatActivity implements PostListAdapter.OnPostListener {

    private static final String TAG = "GameList";
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private RecyclerView gameRView;
    private PostListAdapter plAdapter;
    private ArrayList<PostModel> posts = new ArrayList<>();
    private ArrayList<GameModel> games = new ArrayList<>();
    private String userID, userName, userImage;
    private String gameID;
    private EditText editPost;
    private Button createPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_layout);
        final TextView gameName = findViewById(R.id.gameNameView);
        editPost = findViewById(R.id.createPostEdit);
        createPost = findViewById(R.id.createPostBtn);

        Intent intent = getIntent();
        final GameModel game = intent.getParcelableExtra("game");

        final String gName = game.getName();
        gameID = game.getID();
        gameName.setText(gName);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

//        createPost();

        createPost.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
        final String post = editPost.getText().toString();
        if (TextUtils.isEmpty(post)) {
            toastMessage("Enter a post");
            return;
        }
        userID = auth.getCurrentUser().getUid();
        Map<String, Object> posts = new HashMap<>();
        posts.put("post", post);
        posts.put("user", userID);
        posts.put("game", gameID);
        posts.put("userName", userName);
        posts.put("userImg", userImage);
        fStore.collection("posts")
                .add(posts)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
    });

    postList();

}

    private void postList() {
        fStore.collection("posts")
                .whereEqualTo("game", gameID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                PostModel post = document.toObject(PostModel.class);
                                posts.add(post);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        gameRView.scrollToPosition(posts.size() + 1);
                        plAdapter.notifyItemInserted(posts.size() + 1);
                    }
                });
    postRecyclerView();
    }

    private void postRecyclerView() {
        gameRView = findViewById(R.id.postListView);
        gameRView.setHasFixedSize(true);
        gameRView.setLayoutManager(new LinearLayoutManager(this));
        plAdapter = new PostListAdapter(posts, GamePage.this, this);
        gameRView.setAdapter(plAdapter);
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(GamePage.this, PostPage.class);
        intent.putExtra("post", posts.get(position));
        startActivity(intent);
        Log.d(TAG, "Error getting Listener");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
