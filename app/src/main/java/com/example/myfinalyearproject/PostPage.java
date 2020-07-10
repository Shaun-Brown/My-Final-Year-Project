package com.example.myfinalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page_layout);

        Intent intent = getIntent();
        final PostModel post = intent.getParcelableExtra("post");

        final String pName = post.getPost();

        final TextView postName = findViewById(R.id.postView);

        postName.setText(pName);
    }
}
