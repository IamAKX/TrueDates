package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.Constants;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.MessageModel;
import com.neosao.truedates.widgets.FullScreenImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageModel> list;
    private static final int FREINDS_MESSAGE = 1;
    private static final int FREINDS_IMAGE = 2;
    private static final int FREINDS_PROFILE_LIKED = 3;
    private static final int MY_MESSAGE = 4;
    private static final int MY_IMAGE = 5;
    private static final int MY_PROFILE_LIKED = 6;

    public ChatAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType)
        {
            case MY_MESSAGE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_message_me, parent, false);
                return new MyMessageHolder(view);

            case MY_IMAGE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_image_me, parent, false);
                return new MyMessageImageHolder(view);

            case MY_PROFILE_LIKED:
                view = LayoutInflater.from(context).inflate(R.layout.chat_profile_liked_me, parent, false);
                return new MyMessageImageHolder(view);

            case FREINDS_MESSAGE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_message_other, parent, false);
                return new MyMessageHolder(view);

            case FREINDS_IMAGE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_image_other, parent, false);
                return new FriendMessageImageHolder (view);

            case FREINDS_PROFILE_LIKED:
                view = LayoutInflater.from(context).inflate(R.layout.chat_profile_liked_other, parent, false);
                return new FriendMessageImageHolder(view);
            default: return null;
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
            messageHolder.timeStamp.setText(TimeAgo.using(list.get(position).getMessageTimestamp().getTime()));
//            if(list.get(position).getMessage().equals("❤") || list.get(position).getMessage().equals("♥")){
//                messageHolder.message.setBackgroundDrawable(null);
//                messageHolder.message.setTextSize(45);
//                messageHolder.message.setPadding(3,3,100,3);
//            }
        }
        else if(holder instanceof FriendMessageHolder){
            FriendMessageHolder messageHolder = (FriendMessageHolder) holder;
            messageHolder.message.setText(list.get(position).getMessage());
            Glide.with(context)
                    .load(list.get(position).getSenderProfileImage())
                    .circleCrop()
                    .into(messageHolder.profileImage);
            messageHolder.timeStamp.setText(TimeAgo.using(list.get(position).getMessageTimestamp().getTime()));
//            if(list.get(position).getMessage().equals("❤") || list.get(position).getMessage().equals("♥")){
//                messageHolder.message.setBackgroundDrawable(null);
//                messageHolder.message.setTextSize(45);
//                messageHolder.message.setPadding(100,3,3,3);
//            }
        }
        else  if(holder instanceof MyMessageImageHolder)
        {
            MyMessageImageHolder messageHolder = (MyMessageImageHolder) holder;
            messageHolder.progress_bar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(list.get(position).getSenderProfileImage())
                    .circleCrop()
                    .into(messageHolder.profileImage);
            messageHolder.timeStamp.setText(TimeAgo.using(list.get(position).getMessageTimestamp().getTime()));
            Glide.with(context)
                    .load(list.get(position).getMessage())
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            messageHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            messageHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(messageHolder.message);
            messageHolder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, FullScreenImageView.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("messageModel",list.get(position)));

                }
            });

        }
        else if(holder instanceof FriendMessageImageHolder)
        {
            FriendMessageImageHolder messageHolder = (FriendMessageImageHolder) holder;
            messageHolder.progress_bar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(list.get(position).getSenderProfileImage())
                    .circleCrop()
                    .into(messageHolder.profileImage);
            messageHolder.timeStamp.setText(TimeAgo.using(list.get(position).getMessageTimestamp().getTime()));
            Glide.with(context)
                    .load(list.get(position).getMessage())
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            messageHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            messageHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(messageHolder.message);
            messageHolder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, FullScreenImageView.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("messageModel",list.get(position)));

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getSenderID().equals(new LocalPref(context).getUser().getUserId())){
            // Me
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_TEXT))
                return MY_MESSAGE;
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE))
                return MY_IMAGE;
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_PROFILE_LIKED))
                return MY_PROFILE_LIKED;
        }
        else
        {
            // Friend
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_TEXT))
                return FREINDS_MESSAGE;
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE))
                return FREINDS_IMAGE;
            if(list.get(position).getMessageType().equals(Constants.MESSAGE_TYPE_PROFILE_LIKED))
                return FREINDS_PROFILE_LIKED;
        }
        return 0;
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

    private class MyMessageImageHolder extends RecyclerView.ViewHolder {
        TextView  timeStamp;
        ImageView message,profileImage;
        ProgressBar progress_bar;
        public MyMessageImageHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
            timeStamp = view.findViewById(R.id.timeStamp);
            profileImage = view.findViewById(R.id.profileImage);
            progress_bar = view.findViewById(R.id.progress_bar);
        }
    }

    private class FriendMessageImageHolder extends RecyclerView.ViewHolder {
        TextView  timeStamp;
        ImageView message,profileImage;
        ProgressBar progress_bar;
        public FriendMessageImageHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
            timeStamp = view.findViewById(R.id.timeStamp);
            profileImage = view.findViewById(R.id.profileImage);
            progress_bar = view.findViewById(R.id.progress_bar);
        }
    }
}
