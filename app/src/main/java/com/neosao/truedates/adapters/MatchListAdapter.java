package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.ChatMetadataModel;
import com.neosao.truedates.model.Match;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.Chat;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
    private Context context;
    private List<Match> matchList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, location;
        ImageView imgProfile, imgContent, img_chat;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.text_name);
            date = view.findViewById(R.id.text_date);
            location = view.findViewById(R.id.text_location);
            imgProfile = view.findViewById(R.id.img_profile);
            imgContent = view.findViewById(R.id.img_content);
            img_chat = view.findViewById(R.id.img_chat);

        }
    }


    public MatchListAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout_match, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Match item = matchList.get(position);
        holder.name.setText(item.getName());
        holder.date.setText(item.getAge()+" years");
        holder.location.setText(item.getDistance().substring(0,item.getDistance().indexOf('.'))+" kms");

        Glide.with(context)
                .load(item.getDefaultPhoto())
                .into(holder.imgProfile);

        Glide.with(context)
                .load(item.getDefaultPhoto())
                .into(holder.imgContent);

        holder.img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel currentUser = new LocalPref(context).getUser();
                String chatroomId = new Utils().generateChatRoomId(currentUser.getUserId(), item.getUserId());
                UserBasicDetails user2 = new UserBasicDetails(item.getUserId(),item.getName(),item.getDefaultPhoto(), item.getEmail());


                ChatMetadataModel chatMetadataModel = new ChatMetadataModel(chatroomId,new Date(), user2, new ArrayList<>());

                context.startActivity(new Intent(context, Chat.class).putExtra("chatMetadata", chatMetadataModel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

}