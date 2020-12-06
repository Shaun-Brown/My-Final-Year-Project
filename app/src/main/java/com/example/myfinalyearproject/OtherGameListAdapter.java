package com.example.myfinalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.GameModel;

import java.util.ArrayList;
import java.util.List;

public class OtherGameListAdapter extends RecyclerView.Adapter<OtherGameListAdapter.OtherGameViewHolder> {

    private List<GameModel> cGame;
    private Context gContext;

    public OtherGameListAdapter(ArrayList<GameModel> cGame, Context gContext) {
        this.cGame= cGame;
        this.gContext = gContext;
    }

    @Override
    public OtherGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_game_layout, parent, false);
        return new OtherGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OtherGameViewHolder holder, int position) {
        GameModel createGame = cGame.get(position);
        holder.cGameName.setText(createGame.getName());
        holder.cGameDesc.setText(createGame.getDescription());
    }

    @Override
    public int getItemCount() {
        return (cGame == null) ? 0 : cGame.size();
    }

    static class OtherGameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cGameName, cGameDesc;
        RelativeLayout gameListViewParent;

        OtherGameViewHolder(@NonNull View itemView) {
            super(itemView);
            cGameName = itemView.findViewById(R.id.otherGameName);
            cGameDesc = itemView.findViewById(R.id.otherGameDesc);
            gameListViewParent = itemView.findViewById(R.id.otherGameListViewParent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}