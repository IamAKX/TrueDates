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
import com.neosao.truedates.model.Like;
import com.neosao.truedates.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MemberLikedYouAdapter extends RecyclerView.Adapter<MemberLikedYouAdapter.LikeViewHolder>{

    private Context context;
    private ArrayList<UserModel> likeList;

    public MemberLikedYouAdapter(Context context, ArrayList<UserModel> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    @NonNull
    @Override
    public MemberLikedYouAdapter.LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_like_card, parent, false);
        return new MemberLikedYouAdapter.LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberLikedYouAdapter.LikeViewHolder holder, int position) {
        UserModel userModel = likeList.get(position);

        if(null != holder.item_image)
        Glide.with(context)
                .load(userModel.getDefaultPhoto())
                .into(holder.item_image);

        if(null != holder.likeName)
            holder.likeName.setText(userModel.getName());
        holder.item_city.setText(userModel.getAge()+" years old");
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public class LikeViewHolder extends RecyclerView.ViewHolder {

        TextView likeName;
        TextView item_city;
        ImageView item_image;
        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            likeName = itemView.findViewById(R.id.item_name);
            item_image = itemView.findViewById(R.id.item_image);
            item_city = itemView.findViewById(R.id.item_city);
        }
    }
}
