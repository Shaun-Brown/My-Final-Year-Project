package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.GameModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OtherGameList extends AppCompatActivity {

    private static final String TAG = "OtherGameList";

    ArrayList<GameModel> cGame = new ArrayList<>();
    private RecyclerView ogameRView;
    private OtherGameListAdapter oglAdapter;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_game_layout);
        Button signOut = findViewById(R.id.signOutBtn);
        Button createGame = findViewById(R.id.createGameBtn);
        auth = FirebaseAuth.getInstance();

        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherGameList.this, CreateGame.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(OtherGameList.this, LoginPage.class));
                finish();
            }
        });

        otherGameList();

}

    private void otherGameList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("other_games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GameModel game = document.toObject(GameModel.class);
                                cGame.add(game);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ogameRView.scrollToPosition(cGame.size() - 1);
                                oglAdapter.notifyItemInserted(cGame.size() - 1);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        recyclerView();
    }

    private void recyclerView() {
        ogameRView = findViewById(R.id.gameListView);
        ogameRView.setHasFixedSize(true);
        ogameRView.setLayoutManager(new LinearLayoutManager(this));
        oglAdapter = new OtherGameListAdapter(cGame, OtherGameList.this);
        ogameRView.setAdapter(oglAdapter);
    }
}