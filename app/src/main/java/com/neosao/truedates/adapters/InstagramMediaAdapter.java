package com.neosao.truedates.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.neosao.truedates.R;
import com.neosao.truedates.model.InstagramMediaModel;

import java.util.ArrayList;

public class InstagramMediaAdapter extends RecyclerView.Adapter<InstagramMediaAdapter.ViewHolder>{
    private Context context;
    private ArrayList<InstagramMediaModel> list;

    public InstagramMediaAdapter(Context context, ArrayList<InstagramMediaModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstagramMediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_image_view, parent, false);

        return new InstagramMediaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstagramMediaAdapter.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

        Glide.with(context)
                .load(list.get(position).getMedia_url())
                .apply(requestOptions)
                .into(holder.simpleImageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView simpleImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleImageView = itemView.findViewById(R.id.simpleImageView);
        }
    }
}
