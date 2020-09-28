package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.UserModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ViewProfile extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static int PROGRESS_COUNT = 1;

    private StoriesProgressView storiesProgressView;
    private ImageView image;
    View reverse;
    View skip;

    TextView name, age, workIndustry, education, showMe, about;
    UserModel userModel;
    private int counter = 0;
    private String[] resources;

    private final long[] durations = new long[]{
            500L, 1000L, 1500L, 4000L, 5000L, 1000,
    };

    long pressTime = 0L;
    long limit = 500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_profile);


        initializeComponents();
    }

    private void initializeComponents() {
        userModel = new LocalPref(getBaseContext()).getUser();
        storiesProgressView = findViewById(R.id.stories);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        workIndustry = findViewById(R.id.workIndustry);
        education = findViewById(R.id.education);
        showMe = findViewById(R.id.showMe);
        about = findViewById(R.id.about);


        resources = new String[userModel.getMemberPhotos().length];
        for (int i = 0; i < userModel.getMemberPhotos().length; i++) {
            if(null != userModel.getMemberPhotos()[i] && null != userModel.getMemberPhotos()[i].getMemberPhoto())
            resources[i] = (userModel.getMemberPhotos()[i].getMemberPhoto());
        }
        counter = resources.length;
        // bind reverse view
        reverse = findViewById(R.id.reverse);
        // bind skip view
        skip = findViewById(R.id.skip);

        initStory();

        name.setText(userModel.getName() + ",");
        age.setText(userModel.getAge());
        workIndustry.setText(userModel.getMemberWork().get(0).getIndustryName());
        education.setText(userModel.getMemberWork().get(0).getUniversityName());
        showMe.setText(userModel.getMembersettings().get(0).getShowMe());
        about.setText(userModel.getAbout());

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), EditProfile.class));
            }
        });

    }

    private void initStory() {
        PROGRESS_COUNT = userModel.getMemberPhotos().length;
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
//        storiesProgressView.startStories();
        counter = 0;
        storiesProgressView.startStories(counter);

        image = (ImageView) findViewById(R.id.image);
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    public void onNext() {

        Glide.with(getBaseContext())
                .load(resources[++counter])
                .into(image);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        Glide.with(getBaseContext())
                .load(resources[--counter])
                .into(image);
    }

    @Override
    public void onComplete() {
        counter = 0;
        storiesProgressView.destroy();
        initStory();
    }


    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }
}