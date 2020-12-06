package com.example.myfinalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfinalyearproject.Models.GameModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private ArrayList<GameModel> games;
    private OnGameListener gOnGameListener;
    private Context gContext;

    GameListAdapter(ArrayList<GameModel> games, Context gContext, OnGameListener onGameListener) {
        this.games = games;
        this.gOnGameListener = onGameListener;
        this.gContext = gContext;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view, parent, false);
        return new GameViewHolder(view, gOnGameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Glide.with(gContext)
                .asBitmap()
                .load(games.get(position).getImage())
                .into(holder.gameImage);

        holder.gameName.setText(games.get(position).getName());
        holder.gameDesc.setText(games.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView gameImage;
        TextView gameName, gameDesc;
        OnGameListener gOnGameListener;

        GameViewHolder(@NonNull View itemView, OnGameListener onGameListener) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameName = itemView.findViewById(R.id.gameName);
            gameDesc = itemView.findViewById(R.id.gameDesc);
            gOnGameListener = onGameListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            gOnGameListener.onGameClick(getAdapterPosition());
        }
    }

    public interface OnGameListener{
        void onGameClick(int position);
    }
}