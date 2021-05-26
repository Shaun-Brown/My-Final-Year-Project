package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.PostAdapter;
import com.example.myfinalyearproject.Models.GamePostModel;
import com.example.myfinalyearproject.Models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostPage extends AppCompatActivity implements PostAdapter.OnPostListener {
    private static final String TAG = "PostPage";

    private FirebaseAuth auth;
    private FirebaseUser fUser;
    private DatabaseReference dataRef;
    private final ArrayList<PostModel> uPost = new ArrayList<>();
    private GamePostModel gPosts;
    private EditText uPostEdit;
    private String userID;
    private String gameID;
    private String gamePostID;
    private TextView postName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_post_layout);
        uPostEdit = findViewById(R.id.edit_chat_message);
        Button userPostBtn = findViewById(R.id.chat_send_btn);
        postName = findViewById(R.id.postTitleView);
        TextView postDesc = findViewById(R.id.postDescriptionView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        dataRef = firebaseDB.getReference();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        gPosts = intent.getParcelableExtra("gPost");

        postName.setText(gPosts.getGame_Post_Name());
        postDesc.setText(gPosts.getGame_Post_Description());

        gameID = gPosts.getGame_ID();
        gamePostID = gPosts.getGame_Post_ID();

        userPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                createUserPost();
                toastMessage("Message Sent");
            }

        });

        userPostList();
    }

    private void createUserPost() {
        final String uPost = uPostEdit.getText().toString();
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String postTime = timeFormat.format(new Date());
        if (TextUtils.isEmpty(uPost)) {
            toastMessage("Enter a message");
            return;
        }
        final Map<String, Object> userPosts = new HashMap<>();
        userPosts.put("user_post_name", uPost);
        userPosts.put("user_ID", userID);
        userPosts.put("user_name", fUser.getDisplayName());
        userPosts.put("user_post_timestamp", postTime);
        userPosts.put("game_post_ID", gamePostID);

        final String key = dataRef.child("user_posts").push().getKey();

        DatabaseReference postRef = dataRef.child("user_posts").child(gamePostID).child(key);

        postRef.setValue(userPosts).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        toastMessage("Post Sent");
                        finish();
                        startActivity(getIntent());
                    }
                });

    }

    private void userPostList() {
        DatabaseReference postRef = dataRef.child("user_posts").child(gamePostID);
        postRef.orderByChild("user_post_timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: id = " + dataSnapshot.getKey());
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    assert postModel != null;
                    postModel.setUser_Post_ID(dataSnapshot.getKey());
                    uPost.add(postModel);
                }
                userPostRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toastMessage("Error this did not work :'(");
            }

        });
    }

    private void userPostRecyclerView() {
        RecyclerView userPView = findViewById(R.id.userPostListView);
        userPView.setHasFixedSize(true);
        userPView.setLayoutManager(new LinearLayoutManager(this));
        PostAdapter upAdapter = new PostAdapter(uPost, this, this);
        userPView.setAdapter(upAdapter);
        upAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostClick(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, v, position);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        MenuInflater menuInflater2 = popupMenu.getMenuInflater();
        if (uPost.get(position).getUser_ID().equals(userID)) {
            menuInflater2.inflate(R.menu.current_user_menu, popupMenu.getMenu());
        } else {
            menuInflater.inflate(R.menu.user_menu, popupMenu.getMenu());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.currentUserAccountView:
                        Intent intent = new Intent(PostPage.this, UserAccountPage.class);
                        startActivity(intent);
                        return true;
                    case R.id.otherUserAccountItemView:
                        Intent intent2 = new Intent(PostPage.this, OtherUserAccountPage.class);
                        intent2.putExtra("userPost", uPost.get(position));
                        startActivity(intent2);
                        return true;
                    case R.id.privateMessageItemView:
                        Intent intent3 = new Intent(PostPage.this, PrivateMessaging.class);
                        intent3.putExtra("userPost", uPost.get(position));
                        startActivity(intent3);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountBtn:
                toastMessage("account selected");
                Intent intent = new Intent(PostPage.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(PostPage.this, ChooseLoginPage.class);
                startActivity(intent2);
                return true;
            case R.id.friendListIcon:
                Intent intent3 = new Intent(PostPage.this, FriendListPage.class);
                startActivity(intent3);
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
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
