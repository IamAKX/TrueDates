package com.neosao.truedates.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.UserModel;


public class ViewProfile extends AppCompatActivity {

    private static int PROGRESS_COUNT = 1;

    private ImageView image;
    View reverse;
    View skip;

    TextView name, age, workIndustry, education, showMe, about;
    UserModel userModel;
    private int counter = 0;
    private String[] resources;
    LinearLayout storyProgressContainer;


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
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        workIndustry = findViewById(R.id.workIndustry);
        education = findViewById(R.id.education);
        showMe = findViewById(R.id.showMe);
        about = findViewById(R.id.about);
        storyProgressContainer = findViewById(R.id.storyProgressContainer);

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

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverse();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });

    }

    private void skip() {
        ++counter;
        if(counter == resources.length)
            counter = 0;
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);
        updateProgessInidicator();
    }

    private void reverse() {
        --counter;
       if(counter < 0)
           counter = resources.length-1;
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);
        updateProgessInidicator();
    }

    private void initStory() {
        resources = new String[userModel.getMemberPhotos().length];
        for (int i = 0; i < userModel.getMemberPhotos().length; i++) {
            if(null != userModel.getMemberPhotos()[i] && null != userModel.getMemberPhotos()[i].getMemberPhoto())
                resources[i] = (userModel.getMemberPhotos()[i].getMemberPhoto());
        }
        PROGRESS_COUNT = userModel.getMemberPhotos().length;
        storyProgressContainer.setWeightSum(PROGRESS_COUNT);
        for (int i = 0; i < PROGRESS_COUNT; i++) {
            View priceItemView = LayoutInflater.from(this).inflate(R.layout.story_progress_view, storyProgressContainer, false);
            TextView view = priceItemView.findViewById(R.id.tv);
            view.setBackgroundColor(getResources().getColor(R.color.hintColor));
            storyProgressContainer.addView(priceItemView);
        }
        counter = 0;
        updateProgessInidicator();
        image = (ImageView) findViewById(R.id.image);
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);

    }

    private void updateProgessInidicator() {
        for (int i = 0; i < PROGRESS_COUNT; i++) {
            View priceItemView = storyProgressContainer.getChildAt(i);
            TextView view = priceItemView.findViewById(R.id.tv);
            if(i == counter)
                view.setBackgroundColor(getResources().getColor(R.color.themePink));
            else
            view.setBackgroundColor(getResources().getColor(R.color.hintColor));
        }
    }

}