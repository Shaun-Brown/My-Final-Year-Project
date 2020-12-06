package com.example.myfinalyearproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.PostModel;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private ArrayList<PostModel> post;
    private OnPostListener mOnPostListener;

    PostListAdapter(ArrayList<PostModel> post, OnPostListener onPostListener) {
        this.post = post;
        this.mOnPostListener = onPostListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_post_view, parent, false);
        return new PostViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
//        Glide.with(pContext)
//                .asBitmap()
//                .load(user.get(position).getUserImage())
//                .into(holder.userImage);
        holder.postName.setText(post.get(position).getPost_Name());
        holder.postDesc.setText(post.get(position).getPost_Description());
    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        CircleImageView userImage;
        TextView postName, postDesc;
        OnPostListener mOnPostListener;

        PostViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
//            userImage = itemView.findViewById(R.id.userImage);
            postName = itemView.findViewById(R.id.postName);
            postDesc = itemView.findViewById(R.id.postDesc);
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