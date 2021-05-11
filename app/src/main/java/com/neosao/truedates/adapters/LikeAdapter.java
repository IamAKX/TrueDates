package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.ChatMetadataModel;
import com.neosao.truedates.model.Like;
import com.neosao.truedates.model.Match;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.Chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ContactViewHolder>{

    private Context context;
    private List<Match> likeList;


    public LikeAdapter(Context context, List<Match> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    @Override
    public LikeAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_like_item, parent, false);

        return new LikeAdapter.ContactViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return likeList.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        LinearLayout likeLayout;
        TextView likeName;
        ImageView likeImage;


        ContactViewHolder(View itemView) {

            super(itemView);
            likeLayout = itemView.findViewById(R.id.layout_like);
            likeName = itemView.findViewById(R.id.text_like_name);
            likeImage = itemView.findViewById(R.id.circle_image_like);

        }
    }



    @Override
    public void onBindViewHolder(LikeAdapter.ContactViewHolder holder, final int position) {
        final Match item = likeList.get(position);
        holder.likeName.setText(item.getName());

        Glide.with(context)
                .load(item.getDefaultPhoto())
                .into(holder.likeImage);

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
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
}

