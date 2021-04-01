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

import com.example.myfinalyearproject.Adapters.OtherGameAdapter;
import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.OtherGameModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class OtherGamePage extends AppCompatActivity implements OtherGameAdapter.OnOtherGamePostListener{

    private static final String TAG = "OtherGamePostPage";

    private ArrayList<OtherGameModel> oGames = new ArrayList<>();
    private GameModel gMod;
    private RecyclerView oGameRView;
    private OtherGameAdapter oGamePostAdapter;

    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private FirebaseStorage storageRef;

    private TextView otherTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_game_post_layout);

        toastMessage("Other Game Page Retrieved");

        otherTitleView = findViewById(R.id.otherGameNameView);
        Button otherCreateGame = findViewById(R.id.createOtherGamePostBtn);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        gMod = intent.getParcelableExtra("gPost");

        otherTitleView.setText(gMod.getGame_Name());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        otherCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherGamePage.this, CreateOtherGame.class);
                intent.putExtra("gMod", gMod);
                startActivity(intent);
            }
        });

        OtherGamePostPage();

    }

    private void OtherGamePostPage() {

        fStore.collection("games")
                .document(gMod.getGame_ID())
                .collection("other_game_posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OtherGameModel oGMod = document.toObject(OtherGameModel.class);
                                oGames.add(oGMod);
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        oGameRView.scrollToPosition(oGames.size() + 1);
                        oGamePostAdapter.notifyItemInserted(oGames.size() + 1);
                        oGamePostAdapter.notifyDataSetChanged();
                        recyclerView();
                    }
                });

    }

    private void recyclerView() {
        oGameRView = findViewById(R.id.otherGamePostListView);
        oGameRView.setHasFixedSize(true);
        oGameRView.setLayoutManager(new LinearLayoutManager(this));
        oGamePostAdapter = new OtherGameAdapter(oGames, this, OtherGamePage.this);
        oGameRView.setAdapter(oGamePostAdapter);
    }

    @Override
    public void OnOtherGamePostClick(int position) {
        Intent intent = new Intent(OtherGamePage.this, GamePage.class);
        intent.putExtra("oGame", oGames.get(position));
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
                Intent intent = new Intent(OtherGamePage.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(OtherGamePage.this, ChooseLoginPage.class);
                startActivity(intent2);
                return true;
            case android.R.id.home:
                Intent intent3 = new Intent(OtherGamePage.this, MainActivity.class);
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