package com.example.myfinalyearproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText email, password, userName, image;
    private Button register;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_layout);

        email = findViewById(R.id.emailEdit);
        password = findViewById(R.id.passwordEdit);
        userName = findViewById(R.id.userNameEdit);
        image = findViewById(R.id.imageEdit);
        register = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount(){
            final String uEmail = email.getText().toString().trim();
            String uPassword = password.getText().toString().trim();
            final String uName = userName.getText().toString();
            final String uImage = image.getText().toString();
            if (TextUtils.isEmpty(uEmail)||TextUtils.isEmpty(uPassword)) {
                toastMessage("Enter email address and password");
                return;
            }
            auth.createUserWithEmailAndPassword(uEmail, uPassword)
                    .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    password.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(CreateAccount.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                toastMessage( "User Created.");
                                userID = auth.getCurrentUser().getUid();
                                DocumentReference docRef = db.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("user_ID", userID);
                                user.put("user_email_address", uEmail);
                                user.put("user_name", uName);
                                user.put("user_image", uImage);
                                docRef.set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        Log.d(TAG, "on Success: user was created for: " + userID);
                                    }
                                });
                            }
                        }
                    });

        };

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}