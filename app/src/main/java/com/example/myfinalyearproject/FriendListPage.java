package com.example.myfinalyearproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.FriendListAdapter;
import com.example.myfinalyearproject.Models.FriendModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendListPage extends AppCompatActivity implements FriendListAdapter.OnFriendListListener{

    private FirebaseAuth auth;
    private DatabaseReference friendRef;
    private final ArrayList<FriendModel> friendList = new ArrayList<>();
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_page_layout);

        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDB.getReference();
        auth = FirebaseAuth.getInstance();
        friendRef = dataRef.child("friends");
        userID = auth.getCurrentUser().getUid();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        friendList();

    }

    private void friendList() {
        friendRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FriendModel friends = dataSnapshot.getValue(FriendModel.class);
                    String friendID = dataSnapshot.getKey();
                    friends.setFriend_User_ID(friendID);
                    friendList.add(friends);
                }
                friendRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void friendRecyclerView(){
        RecyclerView flRecyclerView = findViewById(R.id.friendListRecyclerView);
        flRecyclerView.setHasFixedSize(true);
        flRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FriendListAdapter flAdapter = new FriendListAdapter(friendList, this, this);
        flRecyclerView.setAdapter(flAdapter);
        flAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFriendClick(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, v, position);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.privateMessageItemView:
                        Intent intent = new Intent(FriendListPage.this, PrivateMessaging.class);
                        intent.putExtra("friend", friendList.get(position));
                        startActivity(intent);
                        return true;
                    case R.id.otherUserAccountItemView:
                        Intent intent2 = new Intent(FriendListPage.this, OtherUserAccountPage.class);
                        intent2.putExtra("friend", friendList.get(position));
                        startActivity(intent2);
                        return true;
                    default:
                        return false;
                }
            }
        });
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
                Intent intent = new Intent(FriendListPage.this, UserAccountPage.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(FriendListPage.this, ChooseLoginPage.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.home:
                onBackPressed();
                startActivity(new Intent(this, MainActivity.class));
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