package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.utility.VerticalSpacingItemDecorator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


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
        Button signIn = findViewById(R.id.signInBtn);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser() != null) {
                    auth.signOut();
                    finish();
                    startActivity(getIntent());
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);
                }
            }
        });

        gameList();

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
                                gameRView.scrollToPosition(games.size() - 1);
                                glAdapter.notifyItemInserted(games.size() - 1);
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