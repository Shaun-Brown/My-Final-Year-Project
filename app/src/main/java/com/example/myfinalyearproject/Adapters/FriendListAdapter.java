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
import com.example.myfinalyearproject.Models.FriendModel;
import com.example.myfinalyearproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private ArrayList<FriendModel> friends;
    private OnFriendListListener mOnFriendListListener;
    private Context context;

    public FriendListAdapter(ArrayList<FriendModel> friends, OnFriendListListener mOnFriendListListener, Context context) {
        this.friends = friends;
        this.mOnFriendListListener = mOnFriendListListener;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_page_view, parent, false);
        return new FriendListAdapter.FriendListViewHolder(view, mOnFriendListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendListViewHolder holder, int position) {

        final FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference imgStore = storageRef.getReference("users/"+ friends.get(position).getFriend_User_ID() +"/profile.jpg");
        imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .error(R.drawable.account_circle_icon)
                        .into(holder.friendUserImage);
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(context)
                        .load(R.drawable.account_circle_icon)
                        .fitCenter()
                        .into(holder.friendUserImage);
            }
        });
        holder.friendUserName.setText(friends.get(position).getFriend_User_Name());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView friendUserImage;
        TextView friendUserName;
        OnFriendListListener mOnFriendListListener;

        FriendListViewHolder(@NonNull View itemView, OnFriendListListener onFriendListListener){
            super(itemView);
            friendUserName = itemView.findViewById(R.id.friendUserName);
            friendUserImage = itemView.findViewById(R.id.friendUserImg);
            mOnFriendListListener = onFriendListListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnFriendListListener.onFriendClick(v, getAdapterPosition());
        }
    }

    public interface OnFriendListListener{
        void onFriendClick(View v, int position);
    }
}
