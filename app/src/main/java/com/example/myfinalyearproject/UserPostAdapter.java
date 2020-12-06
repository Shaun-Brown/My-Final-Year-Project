package com.example.myfinalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.UserModel;
import com.example.myfinalyearproject.Models.UserPostModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder> {

    private Context context;
    private ArrayList<UserPostModel> userPost;
    private ArrayList<UserModel> user;

    UserPostAdapter(Context context, ArrayList<UserPostModel> post, ArrayList<UserModel> user) {
        this.userPost = post;
        this.user = user;
        this.context = context;

    }

    @Override
    public UserPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_view, parent, false);
        return new UserPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostViewHolder holder, final int position) {

//        UserModel user = new UserModel();
//        Glide.with(pContext)
//                .asBitmap()
//                .load(users.getUserImage())
//                .into(holder.userImage);
//        holder.userName.setText(users.getUserName());
        holder.postName.setText(userPost.get(position).getUser_Post_Name());
        holder.userName.setText(user.get(position).getUser_Name());
    }

    @Override
    public int getItemCount() { return userPost.size(); }

    static class UserPostViewHolder extends RecyclerView.ViewHolder {

//        CircleImageView userImage;
        TextView postName, userName;

        UserPostViewHolder(@NonNull View itemView) {
            super(itemView);
//            userImage = itemView.findViewById(R.id.userImage);
            postName = itemView.findViewById(R.id.userPost);
            userName = itemView.findViewById(R.id.userName);

        }
    }
}