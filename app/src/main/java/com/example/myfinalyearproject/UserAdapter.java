package com.example.myfinalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.UserModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<UserModel> user;
    private Context context;

    public UserAdapter(ArrayList<UserModel> user, Context context){
        this.user = user;
        this.context = context;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_view, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        //        UserModel user = new UserModel();
//        Glide.with(pContext)
//                .asBitmap()
//                .load(users.getUserImage())
//                .into(holder.userImage);
//        holder.userName.setText(users.getUserName());
        holder.userName.setText(user.get(position).getUser_Name());
    }

    @Override
    public int getItemCount(){ return user.size(); }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        //        CircleImageView userImage;
        TextView userName;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
//            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);

        }
    }
}
