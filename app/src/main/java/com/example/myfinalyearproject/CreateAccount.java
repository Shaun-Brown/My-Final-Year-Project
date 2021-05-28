package com.example.myfinalyearproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private DatabaseReference userRef;
    private EditText email, password, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_layout);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.userName);
        Button register = findViewById(R.id.btnRegister);
        Button signIn = findViewById(R.id.btnLogin);

        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount(){
            final String uEmail = email.getText().toString().trim();
            final String uPassword = password.getText().toString().trim();
            final String uName = userName.getText().toString();
            if (TextUtils.isEmpty(uEmail)||TextUtils.isEmpty(uPassword)) {
                toastMessage("Enter email address and password");
                return;
            }
            auth.createUserWithEmailAndPassword(uEmail, uPassword)
                    .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                toastMessage("An error has occurred, please try again");
                            } else {
                                toastMessage("User Created.");
                                String userID = auth.getCurrentUser().getUid();
                                DatabaseReference currentUserRef = userRef.child("users").child(userID);
                                currentUserRef.child("user_name").setValue(uName);
                                currentUserRef.child("user_email_address").setValue(uEmail);
                                currentUserRef.child("name").setValue("");
                                currentUserRef.child("password").setValue(uPassword);
                                currentUserRef.child("user_age").setValue("");
                                currentUserRef.child("user_game_tag").setValue("");
                                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}