package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.ChatMetadataModel;
import com.neosao.truedates.model.MessageItem;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.Chat;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    private Context context;
    private List<MessageItem> messageList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, content, count, text_time;
        ImageView thumbnail;
        RelativeLayout viewIndicator,layout_message_content;


        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.text_name);
            content = view.findViewById(R.id.text_content);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewIndicator = view.findViewById(R.id.layout_dot_indicator);
            text_time = view.findViewById(R.id.text_time);
            layout_message_content = view.findViewById(R.id.layout_message_content);
        }
    }


    public MessageListAdapter(Context context, List<MessageItem> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MessageItem item = messageList.get(position);
        UserBasicDetails friend = null;
        for(UserBasicDetails u : item.getParticipants())
        {
            if(u.getUserID() != new LocalPref(context).getUser().getUserId())
            {
                friend = u;
                break;
            }
        }
        holder.name.setText(friend.getUserName());
        holder.content.setText(item.getLastMessage());

        holder.viewIndicator.setVisibility(View.INVISIBLE);


        holder.text_time.setText(TimeAgo.using(item.getLastUpdateTimeStamp().getTime()));
        Glide.with(context)
                .load(friend.getProfileImage())
                .circleCrop()
                .into(holder.thumbnail);

        UserBasicDetails finalFriend = friend;
        holder.layout_message_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatroomId = item.getChatRoomId();
                ChatMetadataModel chatMetadataModel = new ChatMetadataModel(chatroomId,new Date(), finalFriend, new ArrayList<>());

                context.startActivity(new Intent(context, Chat.class).putExtra("chatMetadata", chatMetadataModel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void updateList(List<MessageItem> list){
        messageList = list;
        notifyDataSetChanged();
    }
}