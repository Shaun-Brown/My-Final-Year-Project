package com.example.myfinalyearproject.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myfinalyearproject.Models.PostModel;
import com.example.myfinalyearproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    
    private final ArrayList<PostModel> userPost;
    private final OnPostListener mOnPostListener;
    private final Context context;


    public PostAdapter(ArrayList<PostModel> userPost, OnPostListener mOnPostListener, Context context) {
        this.userPost = userPost;
        this.mOnPostListener = mOnPostListener;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_view, parent, false);
        return new PostViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {

        String userID = userPost.get(position).getUser_ID();
        final FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference imgStore = storageRef.getReference("users/"+ userID +"/profile.jpg");
        imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .error(R.drawable.account_circle_icon)
                        .into(holder.userImage);
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(context)
                        .load(R.drawable.account_circle_icon)
                        .fitCenter()
                        .into(holder.userImage);
            }
        });

        holder.user_name.setText(userPost.get(position).getUser_Name());
        holder.user_message.setText(userPost.get(position).getUser_Post_Name());
        holder.user_timestamp.setText(userPost.get(position).getUser_Post_Timestamp());
    }

    @Override
    public int getItemCount() { return userPost.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView userImage;
        TextView user_name, user_message, user_timestamp;
        OnPostListener mOnPostListener;

        PostViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            user_name = itemView.findViewById(R.id.userName);
            user_message = itemView.findViewById(R.id.userMessage);
            user_timestamp = itemView.findViewById(R.id.userPostTime);
            mOnPostListener = onPostListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnPostListener.onPostClick(v, getAbsoluteAdapterPosition());
        }
    }

    public interface OnPostListener {
        void onPostClick(View v, int position);
    }
}