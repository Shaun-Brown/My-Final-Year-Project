package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.MessageAdapter;
import com.example.myfinalyearproject.Models.FriendModel;
import com.example.myfinalyearproject.Models.MessageModel;
import com.example.myfinalyearproject.Models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrivateMessaging extends AppCompatActivity {

    private static final String TAG = "PrivateMessaging";

    private FirebaseAuth auth;
    private DatabaseReference messageRef;
    private final ArrayList<MessageModel> messages = new ArrayList<>();
    private Button postMessage;
    private EditText editMessage;
    private String chatID, otherUserID, otherUserName, userID, messageKey, messageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message_layout);
        editMessage = findViewById(R.id.edit_chat_message);
        postMessage = findViewById(R.id.chat_send_btn);

        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDB.getReference();
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        messageRef = dataRef.child("messages");

        messageKey = messageRef.push().getKey();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (intent != null) {
            if(intent.getParcelableExtra("friend")!=null&&intent.getParcelableExtra("userPost")==null){
                FriendModel friendMod = intent.getParcelableExtra("friend");
                otherUserID = friendMod.getFriend_User_ID();
                otherUserName = friendMod.getFriend_User_Name();
            } else if(intent.getParcelableExtra("userPost")!=null&&intent.getParcelableExtra("friend")==null){
                PostModel pModel = intent.getParcelableExtra("userPost");
                otherUserID = pModel.getUser_ID();
                otherUserName = pModel.getUser_Name();
            } else {
                toastMessage("Its broken fam");
            }
        }

        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        messageTime = timeFormat.format(new Date());

        ifCreateChatRoom();
    }

    private void ifCreateChatRoom(){
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(userID).child(otherUserID).exists()||!snapshot.child(otherUserID).child(userID).exists()){
                    createChatRoom();
                    toastMessage("Created new one");
                } else {
                    getChatID();
                    toastMessage("Already there");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createChatRoom() {
        final String key = messageRef.push().getKey();
        final Map<String, Object> chatRoom = new HashMap<>();
        chatRoom.put("chatroom_key", key);
        messageRef.child(userID).child(otherUserID).setValue(chatRoom).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                messageRef.child(otherUserID).child(userID).setValue(chatRoom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        toastMessage("Chatroom Set!");
                        postMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendMessage(key);
                            }
                        });

                    }
                });
            }
        });

    }

    private void getChatID(){
        messageRef.child(userID).child(otherUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatID = snapshot.child("chatroom_key").getValue().toString();
                    toastMessage(chatID);
                    messageView(chatID);
                    postMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendMessage(chatID);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(final String ID) {
        String textMessage = editMessage.getText().toString();
        if (TextUtils.isEmpty(textMessage)) {
            toastMessage("Enter a message");
            return;
        }

        final Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("message_name", textMessage);
        userMessage.put("sender_ID", userID);
        userMessage.put("receiver_ID", otherUserID);
        userMessage.put("receiver_name", otherUserName);
        userMessage.put("message_timestamp", messageTime);
        messageRef.child(userID).child(otherUserID).child(ID).child(messageKey).setValue(userMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                messageRef.child(otherUserID).child(userID).child(ID).child(messageKey).setValue(userMessage);
                toastMessage("Successfully added!");
                finish();
                startActivity(getIntent());
            }
        });

    }

    private void messageView(final String ID){
        messageRef.child(userID).child(otherUserID).child(ID).orderByChild("message_timestamp").addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel messagePost = dataSnapshot.getValue(MessageModel.class);
                            assert messagePost != null;
                            messagePost.setMessage_ID(dataSnapshot.getKey());
                            messages.add(messagePost);
//                            Log.d(TAG, "onDataChange: id = " + dataSnapshot.getKey());
//                            toastMessage("Message received");
                            toastMessage(messagePost.getSender_ID());
                        }
                        messageRecyclerView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void messageRecyclerView() {
        RecyclerView mRView = findViewById(R.id.messaging_recyclerview);
        mRView.setHasFixedSize(true);
        mRView.setLayoutManager(new LinearLayoutManager(this));
        MessageAdapter mAdapter = new MessageAdapter(messages, this);
        mRView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
                Intent intent = new Intent(PrivateMessaging.this, UserAccountPage.class);
                startActivity(intent);
                return true;
            case R.id.signOutBtn:
                toastMessage("sign out selected");
                auth.signOut();
                Intent intent2 = new Intent(PrivateMessaging.this, ChooseLoginPage.class);
                startActivity(intent2);
                return true;
            case R.id.friendListIcon:
                Intent intent3 = new Intent(PrivateMessaging.this, FriendListPage.class);
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
