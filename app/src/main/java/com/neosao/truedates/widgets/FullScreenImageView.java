package com.neosao.truedates.widgets;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;
import com.neosao.truedates.R;
import com.neosao.truedates.model.MessageModel;

public class FullScreenImageView extends AppCompatActivity {
    ZoomageView zoomageView;
    ProgressBar progressBar;
    MessageModel messageModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);

        zoomageView = findViewById(R.id.imageview);
        progressBar = findViewById(R.id.progress_bar);
        messageModel = (MessageModel) getIntent().getSerializableExtra("messageModel");

        Glide.with(getBaseContext())
                .load(messageModel.getMessage())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(getBaseContext(),"Failed to load image", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(zoomageView);

    }
}