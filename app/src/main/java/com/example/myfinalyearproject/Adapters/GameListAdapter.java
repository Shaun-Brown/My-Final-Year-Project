package com.example.myfinalyearproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private final ArrayList<GameModel> games;
    private final OnGameListener mOnGameListener;
    private final Context gContext;

    public GameListAdapter(ArrayList<GameModel> games, Context gContext, OnGameListener onGameAdapter) {
        this.games = games;
        this.mOnGameListener = onGameAdapter;
        this.gContext = gContext;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view, parent, false);
        return new GameViewHolder(view, mOnGameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Glide.with(gContext)
                .asBitmap()
                .load(games.get(position).getGame_Image())
                .into(holder.gameImage);

        holder.gameName.setText(games.get(position).getGame_Name());
        holder.gameDesc.setText(games.get(position).getGame_Description());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView gameImage;
        TextView gameName, gameDesc;
        OnGameListener mOnGameListener;

        GameViewHolder(@NonNull View itemView, OnGameListener onGameListener) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameName = itemView.findViewById(R.id.gameName);
            gameDesc = itemView.findViewById(R.id.gameDesc);
            mOnGameListener = onGameListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnGameListener.onGameClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnGameListener{
        void onGameClick(int position);
    }
}