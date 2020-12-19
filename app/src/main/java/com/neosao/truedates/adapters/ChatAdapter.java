package com.neosao.truedates.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageModel> list;
    private static final int FREINDS_MESSAGE = 1;
    private static final int MY_MESSAGE = 2;

    public ChatAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_me, parent, false);
            return new MyMessageHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_other, parent, false);
            return new FriendMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, hh:mm a");

        if(holder instanceof MyMessageHolder)
        {
            MyMessageHolder messageHolder = (MyMessageHolder) holder;
            messageHolder.message.setText(list.get(position).getMessage());
            Glide.with(context)
                    .load(list.get(position).getSenderProfileImage())
                    .circleCrop()
                    .into(messageHolder.profileImage);
            messageHolder.timeStamp.setText(formatter.format(list.get(position).getMessageTimestamp()));
            if(list.get(position).getMessage().equals("❤")){
                messageHolder.message.setBackgroundDrawable(null);
                messageHolder.message.setTextSize(45);
                messageHolder.message.setPadding(3,3,100,3);
            }
        }
        else if(holder instanceof FriendMessageHolder){
            FriendMessageHolder messageHolder = (FriendMessageHolder) holder;
            messageHolder.message.setText(list.get(position).getMessage());
            Glide.with(context)
                    .load(list.get(position).getSenderProfileImage())
                    .circleCrop()
                    .into(messageHolder.profileImage);
            messageHolder.timeStamp.setText(formatter.format(list.get(position).getMessageTimestamp()));
            if(list.get(position).getMessage().equals("❤")){
                messageHolder.message.setBackgroundDrawable(null);
                messageHolder.message.setTextSize(45);
                messageHolder.message.setPadding(100,3,3,3);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getSenderID().equals(new LocalPref(context).getUser().getUserId()) ? MY_MESSAGE : FREINDS_MESSAGE;
    }

    private class MyMessageHolder extends RecyclerView.ViewHolder {
        TextView message, timeStamp;
        ImageView profileImage;

        public MyMessageHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
            timeStamp = view.findViewById(R.id.timeStamp);
            profileImage = view.findViewById(R.id.profileImage);
        }
    }

    private class FriendMessageHolder extends RecyclerView.ViewHolder {
        TextView message, timeStamp;
        ImageView profileImage;
        public FriendMessageHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
            timeStamp = view.findViewById(R.id.timeStamp);
            profileImage = view.findViewById(R.id.profileImage);
        }
    }
}
