package com.example.myfinalyearproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.GameListAdapter;
import com.example.myfinalyearproject.Models.GameModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GameListAdapter.OnGameListener {
    private static final String TAG = "MainActivity";

    private DatabaseReference dataRef;
    private FirebaseAuth auth;
    private final ArrayList<GameModel> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();

        gameList();

    }

    private void gameList() {
        dataRef.child("games").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GameModel gameModel = dataSnapshot.getValue(GameModel.class);
                    assert gameModel != null;
                    gameModel.setGame_ID(dataSnapshot.getKey());
                    games.add(gameModel);
                }
                recyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toastMessage("There is an error");
            }
        });
    }

    private void recyclerView() {
        RecyclerView gameRView = findViewById(R.id.gameListView);
        gameRView.setHasFixedSize(true);
        gameRView.setLayoutManager(new LinearLayoutManager(this));
        GameListAdapter glAdapter = new GameListAdapter(games, this, this);
        gameRView.setAdapter(glAdapter);
        glAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGameClick(int position) {
            Intent intent = new Intent(MainActivity.this, GamePage.class);
            intent.putExtra("games", games.get(position));
            startActivity(intent);
            Log.d(TAG, "Error getting Listener");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountBtn:
                toastMessage("account selected");
                Intent intent = new Intent(MainActivity.this, UserAccountPage.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(MainActivity.this, ChooseLoginPage.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.friendListIcon:
                Intent intent3 = new Intent(MainActivity.this, FriendListPage.class);
                startActivity(intent3);
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