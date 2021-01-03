package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText  userName, password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.login_page_layout);

        userName = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button login = findViewById(R.id.loginBtn);
        Button createAccount = findViewById(R.id.createAccountBtn);

        auth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, CreateAccount.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userN = userName.getText().toString();
                String uPassword = password.getText().toString();
                if (TextUtils.isEmpty(userN)) {
                    toastMessage("Enter email address!");
                    return;
                }
                if (TextUtils.isEmpty(uPassword)) {
                    toastMessage("Enter password!");
                    return;
                }
                auth.signInWithEmailAndPassword(userN, uPassword)
                    .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                password.setError(getString(R.string.minimum_password));
                            } else {
                                toastMessage(getString(R.string.auth_failed));
                            }
                        } else {
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        }
                    });
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}