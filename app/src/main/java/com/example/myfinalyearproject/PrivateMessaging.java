package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Adapters.MessageAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrivateMessaging extends AppCompatActivity {

    private static final String TAG = "PrivateMessaging";

    private DatabaseReference messageRef;
    private final ArrayList<MessageModel> messages = new ArrayList<>();
    private PostModel pModel;
    private Button postMessage;
    private EditText editMessage;
    private String chatID, otherUserID, userID, messageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message_layout);

        editMessage = findViewById(R.id.edit_chat_message);
        postMessage = findViewById(R.id.chat_send_btn);

        Intent intent = getIntent();
        pModel = intent.getParcelableExtra("userPost");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDB.getReference();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        messageRef = dataRef.child("messages");
        otherUserID = pModel.getUser_ID();

        messageKey = messageRef.push().getKey();

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
        chatRoom.put("sender_ID", userID);
        chatRoom.put("receiver_ID", otherUserID);
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
        messageRef.child(userID).child(otherUserID).child("chatroom_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatID = Objects.requireNonNull(snapshot.getValue()).toString();
                    messageView(chatID);
                    postMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendMessage(chatID);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(final String ID) {
        String textMessage = editMessage.getText().toString();
            if (TextUtils.isEmpty(textMessage)) {
                toastMessage("Enter a name");
                return;
            }

            final Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("message_name", textMessage);
            userMessage.put("sender_ID", userID);
            userMessage.put("receiver_ID", otherUserID);
            userMessage.put("receiver_name", pModel.getUser_Name());
//            userMessage.put("message_date", FieldValue.serverTimestamp());
//            userMessage.put("message_timestamp", FieldValue.serverTimestamp());
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

    private void messageView(String ID){
        messageRef.child(userID).child(otherUserID).child(ID).child(messageKey).addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: id = " + dataSnapshot.getKey());
                            MessageModel messagePost = dataSnapshot.getValue(MessageModel.class);
                            assert messagePost != null;
                            messagePost.setMessage_ID(dataSnapshot.getKey());
                            messages.add(messagePost);
                            toastMessage("Message received");
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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
