package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.neosao.truedates.R;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.ViewMemberProfile;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Object> list;
    private static final int BANNER_TYPE = 100;
    private static final int MEMBER_TYPE = 200;

    public CardStackAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_TYPE:
                View cardView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_card, parent, false);

                return new AdViewHolder(cardView);
            default:
            case MEMBER_TYPE:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_spot, parent, false);

                return new MyViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof UserModel)
            return MEMBER_TYPE;
        else
            return BANNER_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case MEMBER_TYPE:
                MyViewHolder viewHolder = (MyViewHolder)holder;
                UserModel profile = (UserModel) list.get(position);
                viewHolder.name.setText(profile.getName());
                viewHolder.city.setText(profile.getDistance().substring(0, profile.getDistance().indexOf(".") + 3) + " Kms away");

                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, ViewMemberProfile.class).putExtra("member", profile));
                    }
                });
                Glide.with(context)
                        .load(profile.getDefaultPhoto())
                        .into(viewHolder.image);
                break;
            case BANNER_TYPE:
                AdViewHolder bannerHolder = (AdViewHolder) holder;

                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Object> getList() {
        return list;
    }

    public void setList(ArrayList<Object> list) {
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

    public class AdViewHolder extends RecyclerView.ViewHolder {
        CardView adCard;
        ProgressBar progressbar;
        public AdViewHolder(View cardView) {
            super(cardView);
            adCard = cardView.findViewById(R.id.adCard);
            progressbar = cardView.findViewById(R.id.progressbar);
            adCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    AdView adView = new AdView(context);
                    AdSize adSize = new AdSize(400, 700);

//                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//                    int targetAdHeight = (int) (cardView.getHeight() / displayMetrics.density);//targetAdWidth * 50 / 320;
//                    int targetAdWidth = targetAdHeight * 320 / 50;//(int) (cardView.getWidth() / displayMetrics.density);

//                    AdSize adSize = new AdSize(targetAdWidth, targetAdHeight);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId("ca-app-pub-7095480517399381/5987791915");
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.loadAd(adRequest);

                    adCard.addView(adView);
                }
            });

        }
    }
}
