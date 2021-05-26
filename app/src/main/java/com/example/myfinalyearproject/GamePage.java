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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.GamePostAdapter;
import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.GamePostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GamePage extends AppCompatActivity implements GamePostAdapter.OnGamePostListener {
    private static final String TAG = "GameList";

    private FirebaseAuth auth;
    private DatabaseReference dataRef;
    private GamePostAdapter gpAdapter;
    private GameModel games;
    private final ArrayList<GamePostModel> gPosts = new ArrayList<>();
    private String gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_post_layout);

        final TextView gameName = findViewById(R.id.gameNameView);
        Button createGamePost = findViewById(R.id.createGamePostBtn);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        games = intent.getParcelableExtra("games");

        gameName.setText(games.getGame_Name());
        gameID = games.getGame_ID();

        createGamePost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GamePage.this, CreateGamePost.class);
                        intent.putExtra("games", games);
                        startActivity(intent);
                    }
                });

        gamePostList();


    }

    private void gamePostList() {
        DatabaseReference gamePostRef = dataRef.child("games").child(gameID).child("game_posts");
        gamePostRef.orderByChild("game_post_timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gPosts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            GamePostModel gamePostModel = dataSnapshot.getValue(GamePostModel.class);
                            assert gamePostModel != null;
                            gamePostModel.setGame_Post_ID(dataSnapshot.getKey());
                            gPosts.add(gamePostModel);
                        }
                        gamePostRecyclerView();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        toastMessage("Error this did not work :'(");
                    }

                });
    }

    private void gamePostRecyclerView() {
        RecyclerView gpRecyclerView = findViewById(R.id.gamePostListView);
        gpRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mGamePageLayoutAdapter = new LinearLayoutManager(this);
        gpRecyclerView.setLayoutManager(mGamePageLayoutAdapter);
        gpAdapter = new GamePostAdapter(gPosts, this, this);
        gpRecyclerView.setAdapter(gpAdapter);
        gpAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGamePostClick(int position) {
            Intent intent = new Intent(GamePage.this, PostPage.class);
            intent.putExtra("gPost", gPosts.get(position));
            startActivity(intent);
            Log.d(TAG, "Error getting Listener");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
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
                Intent intent2 = new Intent(GamePage.this, ChooseLoginPage.class);
                startActivity(intent2);
                return true;
            case R.id.friendListIcon:
                Intent intent3 = new Intent(GamePage.this, FriendListPage.class);
                startActivity(intent3);
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
