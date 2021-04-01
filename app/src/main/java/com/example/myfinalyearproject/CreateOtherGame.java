package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.GamePostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateOtherGame extends AppCompatActivity {

    private static final String TAG = "CreateOtherGame";
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private GameModel gMod;
    EditText otherGameName, otherGameDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_other_post_layout);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        otherGameName = findViewById(R.id.otherGameNameEdit);
        otherGameDesc = findViewById(R.id.otherGameDescEdit);
        Button btnCreate = findViewById(R.id.otherCreateGameBtn);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        gMod = intent.getParcelableExtra("gMod");

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGame();
            }
        });

    }

    private void createGame(){
        String oGName = otherGameName.getText().toString().trim();
        String oGDesc = otherGameDesc.getText().toString().trim();
        String userID = auth.getCurrentUser().getUid();
        Map<String, Object> oGame = new HashMap<>();
        oGame.put("other_game_post_name", oGName);
        oGame.put("other_game_post_description", oGDesc);
        oGame.put("game_post_ID", gMod.getGame_ID());
        oGame.put("game_post_name", gMod.getGame_Name());
        oGame.put("user_ID", userID);
        oGame.put("game_ID", gMod.getGame_ID());
        oGame.put("other_game_post_timestamp", FieldValue.serverTimestamp());

        fStore.collection("games")
                .document(gMod.getGame_ID())
                .collection("game_posts")
                .add(oGame)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Intent intent = new Intent(CreateOtherGame.this, OtherGamePage.class);
                        intent.putExtra("gPMod", gMod);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

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
                Intent intent = new Intent(CreateOtherGame.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(CreateOtherGame.this, ChooseLoginPage.class);
                startActivity(intent2);
                return true;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}