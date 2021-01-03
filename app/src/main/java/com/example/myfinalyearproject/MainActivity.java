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

import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.utility.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GameListAdapter.OnGameListener {
    private static final String TAG = "GameList";

    private RecyclerView gameRView;
    private GameListAdapter glAdapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private ArrayList<GameModel> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        gameList();

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
                Intent intent = new Intent(MainActivity.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.signInBtn:
                toastMessage("sign in selected");
                Intent intent2 = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void gameList() {

        fStore.collection("games")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            GameModel game = document.toObject(GameModel.class);
                            game.setID(document.getId());
                            games.add(game);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            gameRView.scrollToPosition(games.size() + 1);
                            glAdapter.notifyItemInserted(games.size() + 1);
                        }
                    }
                });

        recyclerView();
    }

    private void recyclerView() {
        gameRView = findViewById(R.id.gameListView);
        gameRView.setHasFixedSize(true);
        gameRView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        gameRView.addItemDecoration(itemDecorator);
        glAdapter = new GameListAdapter(games, MainActivity.this, this);
        gameRView.setAdapter(glAdapter);
    }

    @Override
    public void onGameClick(int position) {
        Intent intent = new Intent(MainActivity.this, GamePage.class);
        intent.putExtra("game", games.get(position));
        startActivity(intent);
        Log.d(TAG, "Error getting Listener");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}