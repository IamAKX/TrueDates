package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.neosao.truedates.R;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;

public class UploadProfileImage extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViews;
    UserModel user;
    LocalPref localPref;
    ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        localPref = new LocalPref(getBaseContext());
        user = localPref.getUser();
        imageViews = new ImageView[]{
                findViewById(R.id.profileImage1),
                findViewById(R.id.profileImage2),
                findViewById(R.id.profileImage3),
                findViewById(R.id.profileImage4),
                findViewById(R.id.profileImage5),
                findViewById(R.id.profileImage6)
        };
        for(ImageView iv : imageViews)
            iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(false)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setBackgroundColor("#ffffff")
                .setStatusBarColor("#E0E0E0")
                .setToolbarColor("#ffffff")
                .setToolbarTextColor("#FF6F8B")
                .setProgressBarColor("#FF6F8B")
                .setIndicatorColor("#FF6F8B")
                .setShowCamera(true)
                .setDoneTitle("Select")
                .setLimitMessage("You can select up to 10 images")
                .setSelectedImages(images)
                .setRequestCode(100)
                .start();
    }
}