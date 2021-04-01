package com.example.myfinalyearproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinalyearproject.Models.GameModel;
import com.example.myfinalyearproject.Models.OtherGameModel;
import com.example.myfinalyearproject.R;

import java.util.ArrayList;
import java.util.List;

public class OtherGameAdapter extends RecyclerView.Adapter<OtherGameAdapter.OtherGamePostViewHolder> {

    private final List<OtherGameModel> oGModel;
    private final OnOtherGamePostListener mOnOtherGamePostListener;
    private final Context oGContext;

    public OtherGameAdapter(ArrayList<OtherGameModel> oGModel, OnOtherGamePostListener mOnOtherGamePostListener, Context oGContext) {
        this.oGModel= oGModel;
        this.mOnOtherGamePostListener = mOnOtherGamePostListener;
        this.oGContext = oGContext;
    }

    @Override
    public OtherGamePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_game_post_view, parent, false);
        return new OtherGamePostViewHolder(view, mOnOtherGamePostListener);
    }

    @Override
    public void onBindViewHolder(OtherGamePostViewHolder holder, int position) {

        holder.oGPostName.setText(oGModel.get(position).getOther_Game_Name());
        holder.oGPostDesc.setText(oGModel.get(position).getOther_Game_Description());
        holder.oGPostTime.setText(oGModel.get(position).getOther_Game_Timestamp().toString());
    }

    @Override
    public int getItemCount() {
        return oGModel.size();
    }

    static class OtherGamePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView oGPostName, oGPostDesc, oGPostTime;
        OnOtherGamePostListener mOnOtherGamePostListener;

        OtherGamePostViewHolder(@NonNull View itemView, OnOtherGamePostListener OnOtherGamePostListener) {
            super(itemView);
            oGPostName = itemView.findViewById(R.id.otherGamePostName);
            oGPostDesc = itemView.findViewById(R.id.otherGamePostDesc);
            oGPostTime = itemView.findViewById(R.id.otherGamePostTime);
            mOnOtherGamePostListener = OnOtherGamePostListener;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnOtherGamePostListener.OnOtherGamePostClick(getAdapterPosition());
        }
    }

    public interface OnOtherGamePostListener{
        void OnOtherGamePostClick(int position);
    }
}