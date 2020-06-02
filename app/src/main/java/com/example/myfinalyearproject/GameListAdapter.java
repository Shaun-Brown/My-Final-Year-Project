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

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private ArrayList<GameModel> games;
    private Context gContext;

    GameListAdapter(ArrayList<GameModel> games, Context gContext) {
        this.games = games;
        this.gContext = gContext;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, final int position) {
        GameModel game = games.get(position);
        Glide.with(gContext)
                .asBitmap()
                .load(game.getImage())
                .into(holder.gameImage);

        holder.gameName.setText(game.getName());
        holder.gameDesc.setText(game.getDescription());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView gameImage;
        TextView gameName, gameDesc;
        private final Context gContext;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameName = itemView.findViewById(R.id.gameName);
            gameDesc = itemView.findViewById(R.id.gameDesc);
            gContext = itemView.getContext();

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            final Intent intent;
            switch (getAdapterPosition()){
                case 0:
                    intent =  new Intent(gContext, Dota2Page.class);
                    break;
                case 1:
                    intent =  new Intent(gContext, WarcraftPage.class);
                    break;
                case 2:
                    intent =  new Intent(gContext, LeaguePage.class);
                    break;
                case 3:
                    intent =  new Intent(gContext, HearthstonePage.class);
                    break;
                case 4:
                    intent =  new Intent(gContext, CoDMWPage.class);
                    break;
                case 5:
                    intent =  new Intent(gContext, Destiny2Page.class);
                    break;
                case 6:
                    intent =  new Intent(gContext, MinecraftPage.class);
                    break;
                case 7:
                    intent =  new Intent(gContext, OverwatchPage.class);
                    break;
                default:
                    intent =  new Intent(gContext, OtherGameList.class);
                    break;
                }
                gContext.startActivity(intent);
            }
        }
}