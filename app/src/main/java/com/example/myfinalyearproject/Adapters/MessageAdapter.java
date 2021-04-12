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
import com.example.myfinalyearproject.Models.MessageModel;
import com.example.myfinalyearproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_MESSAGE_SENT = 0;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;

    private static final String TAG = "MessageAdapter";
    private ArrayList<MessageModel> messages;
    private Context context;

    public MessageAdapter(ArrayList<MessageModel> messages, Context context){
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        String user_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (messages.get(position).getSender_ID().equals(user_ID)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_me, parent, false);
            return new SendMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_other, parent, false);
            return new ReceiveMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final FirebaseStorage storageRef = FirebaseStorage.getInstance();

        int type = getItemViewType(position);

        if (type == VIEW_TYPE_MESSAGE_SENT) {
            final SendMessageHolder sendMessageHolder = (SendMessageHolder) holder;
            sendMessageHolder.message_me.setText(messages.get(position).getMessage_Name());
            sendMessageHolder.text_timestamp_me.setText(messages.get(position).getMessage_Timestamp());
        } else {
            final ReceiveMessageHolder receiveMessageHolder = (ReceiveMessageHolder) holder;

            receiveMessageHolder.user_name_other.setText(messages.get(position).getReceiver_Name());
            receiveMessageHolder.message_other.setText(messages.get(position).getMessage_Name());
            receiveMessageHolder.text_timestamp_other.setText(messages.get(position).getMessage_Timestamp());

            StorageReference imgStore = storageRef.getReference("users/"+ messages.get(position).getReceiver_ID() +"/profile.jpg");
            imgStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                            .error(R.drawable.account_circle_icon)
                            .into(receiveMessageHolder.user_image_other);
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Glide.with(context)
                            .load(R.drawable.account_circle_icon)
                            .fitCenter()
                            .into(receiveMessageHolder.user_image_other);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SendMessageHolder extends RecyclerView.ViewHolder {

        TextView message_me, text_timestamp_me;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            message_me = itemView.findViewById(R.id.message_me);
            text_timestamp_me = itemView.findViewById(R.id.text_timestamp_me);
        }
    }

    static class ReceiveMessageHolder extends RecyclerView.ViewHolder {

        CircleImageView user_image_other;
        TextView user_name_other, message_other, text_timestamp_other;

        public ReceiveMessageHolder(@NonNull View itemView) {
            super(itemView);
            user_image_other = itemView.findViewById(R.id.user_image_other);
            user_name_other = itemView.findViewById(R.id.user_name_other);
            message_other = itemView.findViewById(R.id.message_other);
            text_timestamp_other = itemView.findViewById(R.id.text_timestamp_other);
        }
    }
}
