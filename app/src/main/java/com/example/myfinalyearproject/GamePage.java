package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GamePage extends AppCompatActivity implements PostListAdapter.OnPostListener {

    private static final String TAG = "GameList";
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private RecyclerView gameRView;
    private PostListAdapter plAdapter;
//    private ArrayList<UserModel> users = new ArrayList<>();
    private ArrayList<PostModel> posts = new ArrayList<>();
    private GameModel games;
    private Button createPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_post_layout);
        final TextView gameName = findViewById(R.id.gameNameView);
        createPost = findViewById(R.id.createPostBtn);

        Intent intent = getIntent();
        games = intent.getParcelableExtra("game");

        gameName.setText(games.getName());

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        createPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GamePage.this, CreatePost.class);
                        intent.putExtra("game", games);
                        startActivity(intent);
                    }
                });

        postList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        MenuItem item = menu.findItem(R.id.accountIcon);
        MenuItem item2 = menu.findItem(R.id.signInBtn);
        if (auth.getCurrentUser() != null) {
            item2.setVisible(false);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountBtn:
                toastMessage("account selected");
                Intent intent = new Intent(GamePage.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                finish();
                return true;
            case R.id.signInBtn:
                toastMessage("sign in selected");
                Intent intent2 = new Intent(GamePage.this, LoginPage.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void postList() {
        fStore.collection("posts")
                .whereEqualTo("game_ID", games.getID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            PostModel post = document.toObject(PostModel.class);
                            post.setPost_ID(document.getId());
                            posts.add(post);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            gameRView.scrollToPosition(posts.size() + 1);
                            plAdapter.notifyItemInserted(posts.size() + 1);
                            plAdapter.notifyDataSetChanged();
                        }
                    }
                });
        postRecyclerView();
    }

    private void postRecyclerView() {
        gameRView = findViewById(R.id.postListView);
        gameRView.setHasFixedSize(true);
        gameRView.setLayoutManager(new LinearLayoutManager(this));
        plAdapter = new PostListAdapter(posts, this);
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
