package com.example.myfinalyearproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private ArrayList<PostModel> post;
    private Context pContext;
    private OnPostListener mOnPostListener;

    PostListAdapter(ArrayList<PostModel> post, Context pContext, OnPostListener onPostListener) {
        this.post = post;
        this.mOnPostListener = onPostListener;
        this.pContext = pContext;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new PostViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        Glide.with(pContext)
                .asBitmap()
                .load(post.get(position).getUserImage())
                .into(holder.userImage);
        holder.userName.setText(post.get(position).getUserName());
        holder.postName.setText(post.get(position).getPost());
    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView userImage;
        TextView postName, userName;
        OnPostListener mOnPostListener;

        PostViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            postName = itemView.findViewById(R.id.postName);
            mOnPostListener = onPostListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }
}