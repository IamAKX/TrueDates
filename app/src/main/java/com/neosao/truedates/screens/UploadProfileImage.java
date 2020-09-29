package com.neosao.truedates.screens;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.AndroidMultiPartEntity;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.neosao.truedates.configs.Utils.getIndexOfImageView;

public class UploadProfileImage extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViews;
    UserModel user;
    LocalPref localPref;
    ArrayList<Image> images = new ArrayList<>();
    ImageView tappedImageView;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        localPref = new LocalPref(getBaseContext());
        user = localPref.getUser();
        continueBtn = findViewById(R.id.continueBtn);

        imageViews = new ImageView[]{
                findViewById(R.id.profileImage1),
                findViewById(R.id.profileImage2),
                findViewById(R.id.profileImage3),
                findViewById(R.id.profileImage4),
                findViewById(R.id.profileImage5),
                findViewById(R.id.profileImage6)
        };
        for (ImageView iv : imageViews)
            iv.setOnClickListener(this);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel newUser = localPref.getUser();
                if (null != newUser && null != newUser.getMemberPhotos() && Utils.getPhotoCount(newUser.getMemberPhotos()) < 4) {
                    Toast.makeText(getBaseContext(), "Select at least 4 images", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        if (null != user && null != user.getMemberPhotos())
            for (int i = 0; i < Utils.getPhotoCount(user.getMemberPhotos()) && i < 6; i++) {
                if (null != user.getMemberPhotos()[i] && null != user.getMemberPhotos()[i].getMemberPhoto())
                    Glide.with(getBaseContext())
                            .load(user.getMemberPhotos()[i].getMemberPhoto())
                            .into(imageViews[i]);
            }
    }


    @Override
    public void onClick(View view) {
        tappedImageView = (ImageView) view;
        new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        ImagePicker.with(UploadProfileImage.this)
                                .setFolderMode(true)
                                .setFolderTitle("Album")
                                .setDirectoryName("True Dates")
                                .setMultipleMode(false)
                                .setShowNumberIndicator(true)
                                .setMaxSize(1)
                                .setBackgroundColor("#ffffff")
                                .setStatusBarColor("#E0E0E0")
                                .setToolbarColor("#ffffff")
                                .setToolbarIconColor("#FF6F8B")
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
                })
                .whenPermissionsRefused(new PermissionsRefusedListener() {
                    @Override
                    public void onPermissionsRefused(String[] permissions) {
                        Toast.makeText(getBaseContext(), "We need your permission to read the image", Toast.LENGTH_LONG).show();
                    }
                })
                .execute(UploadProfileImage.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            images = ImagePicker.getImages(data);
            UCrop.of(images.get(0).getUri(), Uri.fromFile(new File(getCacheDir(), String.valueOf("TrueDates_edited_" + System.currentTimeMillis() + ".png"))))
                    .withOptions(getCropOption())
                    .start(UploadProfileImage.this);

        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Log.e("check", resultUri.getPath());
            Glide.with(getBaseContext())
                    .load(resultUri.getPath())
                    .into(tappedImageView);

            if(null != resultUri)
            {
                File imageToBeUploaded = new File(resultUri.getPath());
                if(null != imageToBeUploaded)
                    new UploadImageTask(imageToBeUploaded).execute();
            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("check", cropError.getLocalizedMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);

        options.setToolbarColor(ContextCompat.getColor(this, R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.grey));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.themePink));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.white));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.themePink));
        return options;
    }




    private class UploadImageTask extends AsyncTask<Void, Void, String> {

        File imageToBeUploaded;
        SweetAlertDialog dialog;

        public UploadImageTask(File imageToBeUploaded) {
            this.imageToBeUploaded = imageToBeUploaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(UploadProfileImage.this, "Uploading image...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(API.UPLOAD_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });



                // Adding file data to http body
                entity.addPart("file", new FileBody(imageToBeUploaded));

                // Extra parameters if you want to pass to server
                entity.addPart("userId",
                        new StringBody(user.getUserId()));
                entity.addPart("index",
                        new StringBody(String.valueOf(getIndexOfImageView(tappedImageView))));

                httppost.setEntity(entity);


                Log.e("check", "User id : " + user.getUserId());
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } finally {

                dialog.dismissWithAnimation();

            }
            Log.e("check", "upload response : " + responseString);

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            Log.e("check", "1 : " + responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
                if (null != obj && obj.has("message"))
                    Toast.makeText(getBaseContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                if (null != obj && obj.has("status") && obj.getString("status").equals("200")) {
                    Log.e("check", "status");
                    if (obj.has("result") && obj.getJSONObject("result").has("memberPhoto")) {
                        MemberPhotos photos = new Gson().fromJson(obj.getJSONObject("result").getJSONObject("memberPhoto").toString(), MemberPhotos.class);
                        if (null == user.getMemberPhotos() || user.getMemberPhotos().length < 9)
                            user.setMemberPhotos(new MemberPhotos[9]);

                        Log.e("check", "photo : " + photos.toString());

                        user.getMemberPhotos()[getIndexOfImageView(tappedImageView)] = photos;
                        Log.e("check", "photo arr length " + Utils.getPhotoCount(user.getMemberPhotos()));

                        localPref.saveUser(user);
                    }
                } else {
                    Glide.with(getBaseContext())
                            .load(R.drawable.dashed_border)
                            .into(tappedImageView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("check", "err: " + e.getLocalizedMessage());
            }
        }
    }
}