package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.ViewMemberProfile;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserModel> list;

    public CardStackAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CardStackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spot, parent, false);

        return new CardStackAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.MyViewHolder holder, int position) {
        UserModel profile = list.get(position);
        holder.name.setText(profile.getName());
        holder.city.setText(profile.getDistance().substring(0,profile.getDistance().indexOf(".")+3) + " Kms away");

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ViewMemberProfile.class).putExtra("member",profile));
            }
        });
        Glide.with(context)
                .load(profile.getDefaultPhoto())
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<UserModel> getList() {
        return list;
    }

    public void setList(ArrayList<UserModel> list) {
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView city;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            city = itemView.findViewById(R.id.item_city);
            image = itemView.findViewById(R.id.item_image);
        }
    }
}
