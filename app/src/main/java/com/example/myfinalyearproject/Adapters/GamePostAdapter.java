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
import com.example.myfinalyearproject.Models.GamePostModel;
import com.example.myfinalyearproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GamePostAdapter extends RecyclerView.Adapter<GamePostAdapter.GamePostViewHolder> {

    private final ArrayList<GamePostModel> gPost;
    private final OnGamePostListener mOnGamePostListener;
    private final Context context;

    public GamePostAdapter(ArrayList<GamePostModel> gPost, OnGamePostListener onGamePostListener, Context context) {
        this.gPost = gPost;
        this.mOnGamePostListener = onGamePostListener;
        this.context = context;
    }

    @NonNull
    @Override
    public GamePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_post_view, parent, false);
        return new GamePostViewHolder(view, mOnGamePostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final GamePostViewHolder holder, int position) {

        final FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference imgStore = storageRef.getReference("game_posts/"+ gPost.get(position).getGame_Post_ID() +"/image.jpg");
        imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .error(R.drawable.account_circle_icon)
                        .into(holder.gamePostImage);
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(context)
                        .load(R.drawable.account_circle_icon)
                        .fitCenter()
                        .into(holder.gamePostImage);
            }
        });
        holder.postName.setText(gPost.get(position).getGame_Post_Name());
        holder.postDesc.setText(gPost.get(position).getGame_Post_Description());
        holder.postTime.setText(gPost.get(position).getGame_Post_Timestamp().toString());
    }

    @Override
    public int getItemCount() {
        return gPost.size();
    }

    static class GamePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView gamePostImage;
        TextView postName, postDesc, postTime;
        OnGamePostListener mOnGamePostListener;

        GamePostViewHolder(@NonNull View itemView, OnGamePostListener onGamePostListener) {
            super(itemView);
            gamePostImage = itemView.findViewById(R.id.gamePostImage);
            postName = itemView.findViewById(R.id.gamePostName);
            postDesc = itemView.findViewById(R.id.gamePostDesc);
            postTime = itemView.findViewById(R.id.gamePostTime);
            mOnGamePostListener = onGamePostListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnGamePostListener.onGamePostClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnGamePostListener{
        void onGamePostClick(int position);
    }
}