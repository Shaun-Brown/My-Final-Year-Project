package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.PostModel;
import com.example.myfinalyearproject.Models.UserModel;
import com.example.myfinalyearproject.Models.UserPostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostPage extends AppCompatActivity {

    private static final String TAG = "PostPage";
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private RecyclerView userPView, userRView;
    private UserPostAdapter upAdapter;
    private UserAdapter uAdapter;
    private ArrayList<UserPostModel> uPostM;
    private ArrayList<UserModel> user;
    private PostModel posts;
    private EditText uPMEdit;
    private Button userPostBtn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page_layout);
        uPMEdit = findViewById(R.id.userPostMessageEdit);
        userPostBtn = findViewById(R.id.userPostBtn);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        posts = intent.getParcelableExtra("post");

        final TextView postName = findViewById(R.id.postView);
        final TextView postDesc = findViewById(R.id.descriptionView);
        postName.setText(posts.getPost_Name());
        postDesc.setText(posts.getPost_Description());

        uPostM = new ArrayList<>();
        user = new ArrayList<>();

        userPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                createUserPost();
                toastMessage("Message Sent");
            }

        });
        userPostList();
        postUsersList();
    }

    private void postUsersList(){
        fStore.collection("users")
//                .whereEqualTo("post_id", posts.getPost_ID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            UserModel uModel = document.toObject(UserModel.class);
                            user.add(uModel);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            userRView.scrollToPosition(user.size() + 1);
                            uAdapter.notifyItemInserted(user.size() + 1);
                            uAdapter.notifyDataSetChanged();
                        }
                    }
                });
        postUserRecyclerView();
    }

    private void postUserRecyclerView() {
        userRView = findViewById(R.id.postUsers);
        userRView.setHasFixedSize(true);
        userRView.setLayoutManager(new LinearLayoutManager(this));
        uAdapter = new UserAdapter(user, this);
        userRView.setAdapter(uAdapter);
    }

    private void createUserPost() {
        final String uPostName = uPMEdit.getText().toString();
        if (TextUtils.isEmpty(uPostName)) {
            toastMessage("Enter a name");
            return;
        }
        userID = auth.getCurrentUser().getUid();

        fStore.collection("users")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot queryDocumentSnapshots) {
                        Map<String, Object> userPosts = new HashMap<>();
                        userPosts.put("user_post_name", uPostName);
                        userPosts.put("user_name", queryDocumentSnapshots.get("user_name"));
                        userPosts.put("post_id", posts.getPost_ID());
                        userPosts.put("user_ID", userID);
                        fStore.collection("user_posts")
                                .add(userPosts)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        startActivity(getIntent());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }

        });
//        UserPostModel uPostM = new UserPostModel();
    }

    private void userPostList() {
        fStore.collection("user_posts")
                .whereEqualTo("post_id", posts.getPost_ID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            UserPostModel userPost = document.toObject(UserPostModel.class);
                            UserModel userM = document.toObject(UserModel.class);
                            uPostM.add(userPost);
                            user.add(userM);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            userPView.scrollToPosition(uPostM.size() - 1);
                            upAdapter.notifyItemInserted(uPostM.size() - 1);
                            upAdapter.notifyDataSetChanged();
                        }
                    }
                });
        userPostRecyclerView();
    }

    private void userPostRecyclerView() {
        userPView = findViewById(R.id.userPosts);
        userPView.setHasFixedSize(true);
        userPView.setLayoutManager(new LinearLayoutManager(this));
        upAdapter = new UserPostAdapter(this, uPostM, user);
        userPView.setAdapter(upAdapter);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
